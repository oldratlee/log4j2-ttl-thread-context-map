#!/bin/bash

cd "$(dirname "$(readlink -f "$0")")"
source ./common.sh

junit_test_cases() {
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
    yellowEcho "skip run junit: log4j2 $LOG4J2_VERSION not support java $JAVA_VERSION"
    exit
fi

class_path="$(getClasspath)"
for test_case in $(junit_test_cases); do
    runCmd "${JAVA_CMD[@]}" -cp "$class_path" \
        org.junit.runner.JUnitCore $test_case
done
