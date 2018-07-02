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

fatal() {
    redEcho "$@" 1>&2
    exit 1
}

headInfo() {
    colorEcho "0;34;46" ================================================================================
    echo "$@"
    colorEcho "0;34;46" ================================================================================
    echo
}

################################################################################
# auto adjust pwd to project dir, and set project to BASE var
################################################################################

while true; do
    [ -f pom.xml ] && {
        readonly BASE="$PWD"
        yellowEcho "Find project base dir: $PWD"
        break
    }
    [ / = "PWD" ] &&  fatal "fail to detect project directory!"

    cd ..
done

#################################################################################
# project common info
#################################################################################

readonly version=`grep '<version>.*</version>' pom.xml | awk -F'</?version>' 'NR==1{print $2}'`
readonly aid=`grep '<artifactId>.*</artifactId>' pom.xml | awk -F'</?artifactId>' 'NR==1{print $2}'`

readonly -a JAVA_CMD=( "$JAVA_HOME/bin/java" -Xmx128m -Xms128m -ea -Duser.language=en -Duser.country=US )
readonly -a JAVA_DEBUG_OPTS=( -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 )

readonly -a MVN_CMD=( ./mvnw ${LOG4J2_VERSION:+-Dlog4j2.version=$LOG4J2_VERSION} )


isLog4j2NotSupportedByJdk() {
    local java_version=$("${JAVA_CMD[@]}" -version 2>&1 | awk '-F"' 'NR==1{print $2}')
    local log4j2_minor_version="$(echo "${LOG4J2_VERSION:-2.11.0}" | awk -F'[.]' '{print $2}')"

    [[ "$java_version" == 1.6.* ]] && (( log4j2_minor_version > 4 ))
}

#################################################################################
# maven operation functions
#################################################################################

mvnClean() {
    runCmd "${MVN_CMD[@]}" clean || fatal "fail to mvn clean!"
}

mvnBuildJar() {
    runCmd "${MVN_CMD[@]}" install -Dmaven.test.skip || fatal "fail to build jar!"
}

mvnCompileTest() {
    runCmd "${MVN_CMD[@]}" test-compile || fatal "fail to mvn test-compile!"
}

readonly dependencies_dir="target/dependency"

mvnCopyDependencies() {
    runCmd "${MVN_CMD[@]}" dependency:copy-dependencies -DincludeScope=test || fatal "fail to mvn copy-dependencies!"

    # remove repackaged and shaded javassist lib
    rm $dependencies_dir/javassist-*
}

getClasspathOfDependencies() {
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

runCmd "${MVN_CMD[@]}" --version

if [ "$1" != "skipClean" ]; then 
    mvnClean
fi
