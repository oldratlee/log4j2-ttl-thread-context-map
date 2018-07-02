#!/bin/bash

cd "$(dirname "$(readlink -f "$0")")"
source ./common.sh

junit_test_case() {
    (
        cd target/test-classes &&
        find . -iname '*Test.class' | sed '
                s%^\./%%
                s/\.class$//
                s%/%.%g
            '
    )
}

if isLog4j2NotSupportedByJdk; then
    yellowEcho "skip run junit: log4j2 $LOG4J2_VERSION not support java 6"
    exit
fi

runCmd "${JAVA_CMD[@]}" -cp "$(getClasspath)" \
    org.junit.runner.JUnitCore $(junit_test_case)
