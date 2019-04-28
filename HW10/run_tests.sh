#!/bin/bash

shopt -s expand_aliases
source ~/.bashrc

alias java="java.exe"
alias javac="javac.exe"
#alias python="python.exe"

doOnly="$1"

javac NeuralNet.java

testcases="$(ls testcases)"
for testcase in $testcases; do
    if [ "${testcase: -4}" == ".exp" ]; then
        testcase="$(basename $testcase .exp)"
        args=$(echo "$testcase" | tr _ \ )
        if [ ! -z $doOnly ]; then
            if [[ $(echo $args | awk '{print $1}') -lt $doOnly ]]; then
                continue
            elif [[ $(echo $args | awk '{print $1}') -gt $doOnly ]]; then
                break
            fi
        fi
        java NeuralNet $(< testcases/$testcase.args) > testcases/$testcase.out
        echo "did $args"
    fi
done

cd testcases
python3 run_tests.py $doOnly