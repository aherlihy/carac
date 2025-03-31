#!/usr/bin/env bash

if [[ $# -ne 2 ]] ; then
    echo 'USAGE: <server #> <output file name (no extension)>'
    exit 0
fi
 
scp herlihy@diascld$1.iccluster.epfl.ch:/scratch2/herlihy/carac/bench/benchmark_out.csv results/$2$1.csv
scp herlihy@diascld$1.iccluster.epfl.ch:/scratch2/herlihy/carac/bench.out results/$2$1.out

cat results/$2$1.csv | sed \
-e 's/carac\.benchmarks\.BenchRQB_//g' \
-e 's/_carac\.warm_/,warm,/g' \
-e 's/_embedded\.interpreted_/,embedded,interp,/g' \
-e 's/_embedded\.jit_/,embedded,lambda,/g' \
-e 's/ddb_sel__0____EOL/ddb,idx/g' \
-e 's/ddbnidx_sel__0____EOL/ddb,nidx/g' \
-e 's/ddb_sel__0_blocking_DELTA_lambda_EOL/ddb,idx/g' \
-e 's/ddbnidx_sel__0_blocking_DELTA_lambda_EOL/ddb,nidx/g' \
-e 's/indexed_sel__0_blocking_DELTA_lambda_EOL/coll,idx/g' \
-e 's/indexed_sel__0____EOL/coll,idx/g' \
-e 's/_souffle\.souffle_/,souffle,/g' \
-e 's/_compile/,compile,/g' \
-e 's/_interp/,interp,/g' \
-e 's/_ddbidx/,ddb,idx/g' \
-e 's/_ddbn/,ddb,nidx/g' \
-e 's/_collidx/,coll,idx/g' \
-e 's/\"//g' > results/$2$1_clean.csv