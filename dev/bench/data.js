window.BENCHMARK_DATA = {
  "lastUpdate": 1676910334255,
  "repoUrl": "https://github.com/aherlihy/carac",
  "entries": {
    "Benchmark": [
      {
        "commit": {
          "author": {
            "email": "herlihyap@gmail.com",
            "name": "aherlihy",
            "username": "aherlihy"
          },
          "committer": {
            "email": "herlihyap@gmail.com",
            "name": "aherlihy",
            "username": "aherlihy"
          },
          "distinct": true,
          "id": "ac6df8c732e94a53eec2a2928cbf9b7da07d19fe",
          "message": "use staged for CI",
          "timestamp": "2023-02-20T16:32:59+01:00",
          "tree_id": "cf32bcd78c698d2dadc83a32440399c05c175897",
          "url": "https://github.com/aherlihy/carac/commit/ac6df8c732e94a53eec2a2928cbf9b7da07d19fe"
        },
        "date": 1676910333568,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 155.65209852795687,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 227.80144563915405,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 283.0488486406624,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 63.84454593694959,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 468.03310000898637,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 692.9100077162512,
            "unit": "ops/s",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.ci_staged_compiled",
            "value": 49.17535207454546,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.ci_staged_interpreted",
            "value": 20.4789202472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.ci_staged_compiled",
            "value": 220.42727755343245,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.ci_staged_interpreted",
            "value": 0.44940006544023553,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.ci_staged_compiled",
            "value": 615.68443818,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.ci_staged_interpreted",
            "value": 211.189325384,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.ci_staged_compiled",
            "value": 421.8182904660122,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.ci_staged_interpreted",
            "value": 7.001069216393249,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.ci_staged_compiled",
            "value": 327.702198802815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.ci_staged_interpreted",
            "value": 12.937647342965018,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.ci_staged_compiled",
            "value": 290.15200462533977,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.ci_staged_interpreted",
            "value": 1.5144350804990188,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.ci_staged_compiled",
            "value": 455.8175089468177,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.ci_staged_interpreted",
            "value": 48.14634744904786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.ci_staged_compiled",
            "value": 216.95695261454262,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.ci_staged_interpreted",
            "value": 1.3143616003054601,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.ci_staged_compiled",
            "value": 115.05763629403698,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.ci_staged_interpreted",
            "value": 0.22625367211745742,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.ci_staged_compiled",
            "value": 3351.726477037605,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.ci_staged_interpreted",
            "value": 8.988631716191279,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.ci_staged_compiled",
            "value": 244.8822656925751,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.ci_staged_interpreted",
            "value": 44.9896771276376,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.ci_staged_compiled",
            "value": 139.74695127335258,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.ci_staged_interpreted",
            "value": 6.940329852286678,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.ci_staged_compiled",
            "value": 9.211349770070619,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.ci_staged_interpreted",
            "value": 0.20638887692679186,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.ci_staged_compiled",
            "value": 159.6430602950019,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.ci_staged_interpreted",
            "value": 0.9131988952379217,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.ci_staged_compiled",
            "value": 332.5881127948278,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.ci_staged_interpreted",
            "value": 35.923878406257025,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.ci_staged_compiled",
            "value": 268.61229641,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.ci_staged_interpreted",
            "value": 94.24144420333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.ci_staged_compiled",
            "value": 662.8361018360699,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.ci_staged_interpreted",
            "value": 47.44755557426485,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.ci_staged_compiled",
            "value": 399.7846623416753,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.ci_staged_interpreted",
            "value": 3.7586457230144332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.ci_staged_compiled",
            "value": 133.25709994438068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.ci_staged_interpreted",
            "value": 0.6686757713978346,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.ci_staged_compiled",
            "value": 127.4653410987888,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.ci_staged_interpreted",
            "value": 0.2749132362970364,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.ci_staged_compiled",
            "value": 283.98665657206647,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.ci_staged_interpreted",
            "value": 0.8109693643661575,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.ci_staged_compiled",
            "value": 126.16396039036064,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.ci_staged_interpreted",
            "value": 1.0600242362967862,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.ci_staged_compiled",
            "value": 298.9749519375135,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.ci_staged_interpreted",
            "value": 0.6188876522647762,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.ci_staged_compiled",
            "value": 38.864835074615385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.ci_staged_interpreted",
            "value": 16.438734177419356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.ci_staged_compiled",
            "value": 117.13524687572371,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.ci_staged_interpreted",
            "value": 0.38563318954044334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.ci_staged_compiled",
            "value": 318.32161387795014,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.ci_staged_interpreted",
            "value": 0.9144131574922053,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}