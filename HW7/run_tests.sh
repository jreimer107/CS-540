#!/bin/bash
stopAt="$1"

javac Chatbot.java

testcases="$(ls testcases)"
for testcase in $testcases; do
    if [ "${testcase: -4}" == ".exp" ]; then
        testcase="$(echo "$testcase" | cut -f 1 -d '.')"
        args=$(echo "$testcase" | tr _ \ )
        if [ ! -z $stopAt ]; then
            if [[ $(echo $args | awk '{print $1}') -gt $stopAt ]]; then
                break
            fi
        fi
        java Chatbot $args > testcases/$testcase.out
        echo "did $args"
    fi
done