window.BENCHMARK_DATA = {
  "lastUpdate": 1677248644573,
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
      },
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
          "id": "65976f5931229d584385adb5ced0c769f91d087f",
          "message": "Use avg mode for Bench",
          "timestamp": "2023-02-20T16:39:48+01:00",
          "tree_id": "6a83797042f5a41f516fbdc074f91d1700ff27dd",
          "url": "https://github.com/aherlihy/carac/commit/65976f5931229d584385adb5ced0c769f91d087f"
        },
        "date": 1676910638654,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 7.7835378197028975,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.0548289766267542,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 2.2739359833339456,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 17.32242988161476,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 1.7952061849358956,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.0514034039389275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.ci_staged_compiled",
            "value": 69.12085619749999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.ci_staged_interpreted",
            "value": 18.951565812592595,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.ci_staged_compiled",
            "value": 200.20976254757934,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.ci_staged_interpreted",
            "value": 0.4704677133719112,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.ci_staged_compiled",
            "value": 486.30827936000003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.ci_staged_interpreted",
            "value": 213.665207706,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.ci_staged_compiled",
            "value": 430.3190193791459,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.ci_staged_interpreted",
            "value": 6.5559564003807775,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.ci_staged_compiled",
            "value": 315.1864375560308,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.ci_staged_interpreted",
            "value": 12.90811947721897,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.ci_staged_compiled",
            "value": 293.87546592476525,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.ci_staged_interpreted",
            "value": 1.5914578099686385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.ci_staged_compiled",
            "value": 464.26647540489574,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.ci_staged_interpreted",
            "value": 44.770910098150864,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.ci_staged_compiled",
            "value": 230.70053642091133,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.ci_staged_interpreted",
            "value": 1.2709774597691352,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.ci_staged_compiled",
            "value": 116.17197968767641,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.ci_staged_interpreted",
            "value": 0.2206747122695441,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.ci_staged_compiled",
            "value": 3551.245088656685,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.ci_staged_interpreted",
            "value": 9.202478880206677,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.ci_staged_compiled",
            "value": 218.1033523775881,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.ci_staged_interpreted",
            "value": 42.58487099394207,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.ci_staged_compiled",
            "value": 140.8898772519547,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.ci_staged_interpreted",
            "value": 5.865228912253855,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.ci_staged_compiled",
            "value": 9.899483364091585,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.ci_staged_interpreted",
            "value": 0.18913527896208343,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.ci_staged_compiled",
            "value": 166.18094808607617,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.ci_staged_interpreted",
            "value": 0.9779676516368625,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.ci_staged_compiled",
            "value": 336.50579444692437,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.ci_staged_interpreted",
            "value": 31.69687546077959,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.ci_staged_compiled",
            "value": 248.15545000666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.ci_staged_interpreted",
            "value": 77.6492447057143,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.ci_staged_compiled",
            "value": 669.2511521074141,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.ci_staged_interpreted",
            "value": 45.254089958055324,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.ci_staged_compiled",
            "value": 418.6273662476989,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.ci_staged_interpreted",
            "value": 3.6501384847330165,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.ci_staged_compiled",
            "value": 140.5154266003523,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.ci_staged_interpreted",
            "value": 0.6548056812269447,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.ci_staged_compiled",
            "value": 130.06357799895392,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.ci_staged_interpreted",
            "value": 0.2916589978335159,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.ci_staged_compiled",
            "value": 275.47947608690504,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.ci_staged_interpreted",
            "value": 0.8106590612583107,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.ci_staged_compiled",
            "value": 121.86296031813231,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.ci_staged_interpreted",
            "value": 1.0619234028825624,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.ci_staged_compiled",
            "value": 322.2105189034013,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.ci_staged_interpreted",
            "value": 0.6281045804119656,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.ci_staged_compiled",
            "value": 38.56166089373626,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.ci_staged_interpreted",
            "value": 14.961203867522281,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.ci_staged_compiled",
            "value": 114.26652646256616,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.ci_staged_interpreted",
            "value": 0.40260572408449435,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.ci_staged_compiled",
            "value": 339.6216148759878,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.ci_staged_interpreted",
            "value": 0.9237897893501786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
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
          "id": "aedd3d7ce119fb2e49a3c2fe038ffbf938b0a523",
          "message": "benchmark join order",
          "timestamp": "2023-02-23T00:05:17+01:00",
          "tree_id": "de4f78a854e1a256ee17d6c3ccda8d31717fe671",
          "url": "https://github.com/aherlihy/carac/commit/aedd3d7ce119fb2e49a3c2fe038ffbf938b0a523"
        },
        "date": 1677107493112,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 2.9546435513654647,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.0614513972424975,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.015212664849324,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 22.06371962893912,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.5870039493001507,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 0.9851969350885804,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "smarter@ubuntu.com",
            "name": "Guillaume Martres",
            "username": "smarter"
          },
          "committer": {
            "email": "noreply@github.com",
            "name": "GitHub",
            "username": "web-flow"
          },
          "distinct": true,
          "id": "3cc5b16edc0ea7c966345baac476d10ad3e37da0",
          "message": "Significantly improve benchmark compilation speed. (#21)\n\nOn my machine, this brings down `bench/Jmh/compile` from 1 minute to 7 seconds.",
          "timestamp": "2023-02-22T23:23:44+01:00",
          "tree_id": "b1b03290f0b7d00fd5ae89176741f739bae45ddb",
          "url": "https://github.com/aherlihy/carac/commit/3cc5b16edc0ea7c966345baac476d10ad3e37da0"
        },
        "date": 1677107895463,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 6.473815912174507,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.095347472677359,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 3.8356155026260894,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 23.61053454893208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 1.941778537082224,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.2939672021241215,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.ci_staged_compiled",
            "value": 58.64649775749999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.ci_staged_interpreted",
            "value": 20.39527913620769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.ci_staged_compiled",
            "value": 311.7025753180141,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.ci_staged_interpreted",
            "value": 0.6381471858414954,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.ci_staged_compiled",
            "value": 743.7157005,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.ci_staged_interpreted",
            "value": 286.677304768,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.ci_staged_compiled",
            "value": 688.1220950970581,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.ci_staged_interpreted",
            "value": 9.790950826260369,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.ci_staged_compiled",
            "value": 502.2122865499426,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.ci_staged_interpreted",
            "value": 17.215301488493402,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.ci_staged_compiled",
            "value": 455.9559709681581,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.ci_staged_interpreted",
            "value": 2.1065861977206315,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.ci_staged_compiled",
            "value": 644.2060358834763,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.ci_staged_interpreted",
            "value": 77.02325153699938,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.ci_staged_compiled",
            "value": 337.75909423912447,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.ci_staged_interpreted",
            "value": 1.701234974004461,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.ci_staged_compiled",
            "value": 171.40700024140773,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.ci_staged_interpreted",
            "value": 0.3060402151751235,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.ci_staged_compiled",
            "value": 4928.261402955555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.ci_staged_interpreted",
            "value": 11.386277799934525,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.ci_staged_compiled",
            "value": 303.7146242079985,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.ci_staged_interpreted",
            "value": 56.7069245928653,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.ci_staged_compiled",
            "value": 209.25246479947722,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.ci_staged_interpreted",
            "value": 8.802722072307132,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.ci_staged_compiled",
            "value": 13.456993346442673,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.ci_staged_interpreted",
            "value": 0.3256623649620245,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.ci_staged_compiled",
            "value": 248.65856203795366,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.ci_staged_interpreted",
            "value": 1.211303786263333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.ci_staged_compiled",
            "value": 474.89655503866214,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.ci_staged_interpreted",
            "value": 52.689908351981366,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.ci_staged_compiled",
            "value": 233.68933579333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.ci_staged_interpreted",
            "value": 94.81493227133333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.ci_staged_compiled",
            "value": 870.6941346469608,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.ci_staged_interpreted",
            "value": 57.644208651030645,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.ci_staged_compiled",
            "value": 571.4333223016324,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.ci_staged_interpreted",
            "value": 4.145345332853935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.ci_staged_compiled",
            "value": 194.36596379933442,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.ci_staged_interpreted",
            "value": 0.8330235344121327,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.ci_staged_compiled",
            "value": 171.56972289689966,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.ci_staged_interpreted",
            "value": 0.37143741323009205,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.ci_staged_compiled",
            "value": 400.17959099266307,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.ci_staged_interpreted",
            "value": 0.9821903481505142,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.ci_staged_compiled",
            "value": 190.94687172768988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.ci_staged_interpreted",
            "value": 1.3721456057181933,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.ci_staged_compiled",
            "value": 413.03291971808056,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.ci_staged_interpreted",
            "value": 0.774924184669155,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.ci_staged_compiled",
            "value": 40.3233185,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.ci_staged_interpreted",
            "value": 17.86356562689418,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.ci_staged_compiled",
            "value": 168.7415359427868,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.ci_staged_interpreted",
            "value": 0.5229524993824766,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.ci_staged_compiled",
            "value": 417.94889116085477,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.ci_staged_interpreted",
            "value": 1.210650213915811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
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
          "id": "82bda0b8e7bdd6335a40cf605edab669271693d6",
          "message": "do online swap for join orders",
          "timestamp": "2023-02-23T19:06:21+01:00",
          "tree_id": "f64301c018b2d1db31faa27b0ca6a572f005509a",
          "url": "https://github.com/aherlihy/carac/commit/82bda0b8e7bdd6335a40cf605edab669271693d6"
        },
        "date": 1677175981114,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 5.598367847001745,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.335954260647997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 8.012047960528841,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 33.34029540250334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.3931031834742535,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9693139644574784,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
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
          "id": "734e03b8ca0844beec978b2ef483a749270f81e2",
          "message": "Use foldLeft not reduceLeft to avoid mutation+view",
          "timestamp": "2023-02-23T20:50:58+01:00",
          "tree_id": "6c3429f99ad944d83915f61589b33f61c77feb2e",
          "url": "https://github.com/aherlihy/carac/commit/734e03b8ca0844beec978b2ef483a749270f81e2"
        },
        "date": 1677182219331,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.299776843817565,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.384240435099931,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.522555874909406,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 21.739791073647392,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.6763608520034774,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.393624764834208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
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
          "id": "1c56522d7874bd0cd481be6193452654e546d137",
          "message": "Add benchmarks for view/reduce/sort",
          "timestamp": "2023-02-23T23:03:28+01:00",
          "tree_id": "0fbd7c259c85d20a46174430cd35cac8c16e96b4",
          "url": "https://github.com/aherlihy/carac/commit/1c56522d7874bd0cd481be6193452654e546d137"
        },
        "date": 1677190179163,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.31047447221803,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.153102493543036,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.504078161881867,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 20.6518787556212,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.6839216957117547,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.2279866712015322,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
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
          "id": "39845d64e617d41d229952d5baa5405ca82c8ae1",
          "message": "Use Array for atom; add getSorted to JoinIndexes; remove JoinIndexPass",
          "timestamp": "2023-02-24T15:17:41+01:00",
          "tree_id": "45ecfdd05951ba405562d0c7c78dd46ea2825989",
          "url": "https://github.com/aherlihy/carac/commit/39845d64e617d41d229952d5baa5405ca82c8ae1"
        },
        "date": 1677248643963,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.097079797943915,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.418202032488094,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.759019089104719,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 24.162667782322735,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 1.9257148286744763,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9118487227288852,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}