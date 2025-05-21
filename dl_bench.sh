#!/usr/bin/env bash

if [[ $# -ne 2 ]] ; then
    echo 'USAGE: <server #> <output file name (no extension)>'
    exit 0
fi
 
scp herlihy@diascld$1.iccluster.epfl.ch:/scratch2/herlihy/carac/bench/benchmark_out.csv results/$2$1.csv
scp herlihy@diascld$1.iccluster.epfl.ch:/scratch2/herlihy/carac/bench.out results/$2$1.out

cat results/$2$1.csv | sed \
-e 's/carac\.benchmarks\.BenchRQB_//g' \
-e 's/embedded\./embedded,/g' \
-e 's/warm\./warm,/g' \
-e 's/souffle\./souffle,/g' \
-e 's/_/,/g' \
-e 's/,ddbn/-ddbn/g' \
-e 's/,collidx/-collidx/g' \
-e 's/\"//g' > results/$2$1_clean.csv