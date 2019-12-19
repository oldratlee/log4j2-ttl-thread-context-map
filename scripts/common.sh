#!/bin/bash

set -o pipefail
set -e

# https://stackoverflow.com/questions/64786/error-handling-in-bash
error() {
    local parent_lineno="$1"
    local message="$2"
    local code="${3:-1}"
    if [[ -n "$message" ]] ; then
        redEcho "Error on or near line $(caller): ${message}; exiting with status ${code}"
    else
        redEcho "Error on or near line $(caller); exiting with status ${code}"
    fi
    exit "${code}"
}
trap 'error ${LINENO}' ERR

################################################################################
# util functions
################################################################################

# NOTE: $'foo' is the escape sequence syntax of bash
readonly nl=$'\n'
readonly ec=$'\033' # escape char
readonly eend=$'\033[0m' # escape end

colorEcho() {
    local color=$1
    shift

    # if stdout is console, turn on color output.
    [ -t 1 ] && echo "$ec[1;${color}m$@$eend" || echo "$@"
}

redEcho() {
     colorEcho 31 "$@"
}

yellowEcho() {
    colorEcho 33 "$@"
}

runCmd() {
    colorEcho "36" "Run under work directory $PWD :$nl$@"
    "$@"
}

die() {
    redEcho "Error: $@" 1>&2
    exit 1
}

headInfo() {
    colorEcho "0;34;46" ================================================================================
    echo "$@"
    colorEcho "0;34;46" ================================================================================
    echo
}

#################################################################################
# auto adjust pwd to project root dir, and set PROJECT_ROOT_DIR var
#################################################################################
adjustPwdToProjectRootDir() {
    while true; do
        [ / = "$PWD" ] && die "fail to detect project directory!"

        [ -f pom.xml ] && {
            readonly PROJECT_ROOT_DIR="$PWD"
            yellowEcho "Find project root dir: $PWD"
            break
        }
        cd ..
    done
}

adjustPwdToProjectRootDir

#################################################################################
# project common info
#################################################################################

readonly version=`grep '<version>.*</version>' pom.xml | awk -F'</?version>' 'NR==1{print $2}'`
readonly aid=`grep '<artifactId>.*</artifactId>' pom.xml | awk -F'</?artifactId>' 'NR==1{print $2}'`
readonly log4j2_version_in_pom="$(awk -F'</?log4j2.version>' '/<log4j2.version>/{print $2}' pom.xml)"

readonly -a JAVA_CMD=(
    "$JAVA_HOME/bin/java" -Xmx128m -Xms128m -ea -Duser.language=en -Duser.country=US
    ${ENABLE_JAVA_RUN_DEBUG+
        -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005
    }
)
readonly JAVA_VERSION=$("${JAVA_CMD[@]}" -version 2>&1 | awk '-F"' 'NR==1{print $2}')

readonly -a MVN_CMD=( ./mvnw ${LOG4J2_VERSION:+-Dlog4j2.version=$LOG4J2_VERSION} )

isLog4j2NotSupportedByJdk() {
    local log4j2_minor_version="$(echo "${LOG4J2_VERSION:-"$log4j2_version_in_pom"}" | awk -F'[.]' '{print $2}')"

    # log4j2 Changelog
    # https://logging.apache.org/log4j/2.x/changelog.html
    #   Log4j 2.13 and greater requires Java 8
    #   Log4j 2.4 and greater requires Java 7
    #   versions 2.0-alpha1 to 2.3 required Java 6.

    if (( log4j2_minor_version >= 13 )) && [[ "$JAVA_VERSION" == 1.6.* || "$JAVA_VERSION" == 1.7.* ]]; then
        return 0 # true
    elif (( log4j2_minor_version >= 4 )) && [[ "$JAVA_VERSION" == 1.6.* ]] ; then
        return 0 # true
    else
      return 1 # false
    fi
}

#################################################################################
# maven operation functions
#################################################################################

mvnClean() {
    runCmd "${MVN_CMD[@]}" clean || die "fail to mvn clean!"
}

mvnBuildJar() {
    # ! build jar do not modify the pom(log4j2 verion) by ${LOG4J2_VERSION:+-Dlog4j2.version=$LOG4J2_VERSION}
    runCmd "${MVN_CMD[@]}" install -Dmaven.test.skip || die "fail to build jar!"
}

mvnCompileTest() {
    runCmd "${MVN_CMD[@]}" test-compile || die "fail to mvn test-compile!"
}

mvnCopyDependencies() {
    runCmd "${MVN_CMD[@]}" dependency:copy-dependencies -DincludeScope=test || die "fail to mvn copy-dependencies!"
}

getClasspathOfDependencies() {
    local -r dependencies_dir="target/dependency"

    [ -e "$dependencies_dir" ] || mvnCopyDependencies 1>&2

    echo $dependencies_dir/*.jar | tr ' ' :
}

getClasspathWithoutTargetJar() {
    [ ! -e "target/test-classes/"  -o  "target/test-classes/" -ot src/  ] &&
        mvnCompileTest 1>&2

    echo "target/test-classes:$(getClasspathOfDependencies)"
}

getTargetJarPath() {
    local -r target_jar="target/$aid-$version.jar"

    [ ! -e "$target_jar"  -o  "$target_jar" -ot src/ ] &&
        mvnBuildJar 1>&2

    echo "$target_jar"
}

getClasspath() {
    echo "$(getTargetJarPath):$(getClasspathWithoutTargetJar)"
}

#################################################################################
# maven actions
#################################################################################

if [ "$1" != "skipClean" ]; then
    runCmd "${MVN_CMD[@]}" --version
    mvnClean
fi
