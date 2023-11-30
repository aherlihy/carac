#!/usr/bin/env bash

if [[ $# -ne 2 ]] ; then
    echo 'USAGE: <server #> <output file name (no extension)>'
    exit 0
fi
 
scp herlihy@diascld$1.iccluster.epfl.ch:/scratch/herlihy/carac/bench/benchmark_out.csv results/$2$1.csv
scp herlihy@diascld$1.iccluster.epfl.ch:/scratch/herlihy/carac/bench.out results/$2$1.out

#cat results/$2$1.csv | sed -e 's/"datalog\.benchmarks\.examples\.\([^\.]*\)\./\1_/' | sed -e 's/_EOL\([^"]*\)"*/,/' | sed -e "1s/Benchmark/Benchmark_mode_storage_sort_onlineSort_fuzzy_compileSync_granularity_backend,/" | sed -e "1s/\"//g" > results/$2$1_clean.csv
cat results/$2$1.csv |
 sed -e 's/"datalog\.benchmarks\.BenchMacro(.*)\.//' |
 sed -e 's/jit_lambda/jit-lambda_nothing/' |
 sed -e 's/interpreter_optimized/interpreter-hand-optimized_nothing/' |
 sed -e 's/interpreter_worst/interpreter_nothing/' |
 sed -e 's/zzz//' |
 sed -e 's/runtimefacts/rules/' |
 sed -e 's/aot/facts+rules/' |
 sed -e "1s/Benchmark/Benchmark_mode_stage_runtimeopt/" |
 sed -e 's/line\([^"]*\)"*/line/' |
 sed -e "1s/\"//g" > results/$2$1_clean.csv
