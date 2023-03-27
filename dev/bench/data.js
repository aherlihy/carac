window.BENCHMARK_DATA = {
  "lastUpdate": 1679947611590,
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
          "id": "045c265f3ddb2e09a7d116b5adb8595909e8aa4e",
          "message": "Add benchmarks for staged",
          "timestamp": "2023-02-24T16:00:23+01:00",
          "tree_id": "b9f4fac22fe5fce7bf7600efabd21b523a340734",
          "url": "https://github.com/aherlihy/carac/commit/045c265f3ddb2e09a7d116b5adb8595909e8aa4e"
        },
        "date": 1677251210150,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.467991644517466,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 2.9995098833108145,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.240666541100275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 21.578626622899293,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 1.922564661565022,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.207579867238167,
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
          "id": "a25c0fbdfd8629b0770ef861ca8e7e76bad2a1a1",
          "message": "Use hashmap in sm for storing idx",
          "timestamp": "2023-02-24T19:44:56+01:00",
          "tree_id": "d9012a040d2da24e0c9ceb8ecdec44a266bc3a65",
          "url": "https://github.com/aherlihy/carac/commit/a25c0fbdfd8629b0770ef861ca8e7e76bad2a1a1"
        },
        "date": 1677264707652,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.713134755782692,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.377515185000716,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 8.741097349804452,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 21.609114765315223,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.6991189979414196,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.6793326212207809,
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
          "id": "bd1c244a9ffe236d199088d04c1601c52d401084",
          "message": "start bench for spju",
          "timestamp": "2023-02-24T22:19:54+01:00",
          "tree_id": "9cc3b46623a3b5d03263d48d76a8d5afe80bb1e8",
          "url": "https://github.com/aherlihy/carac/commit/bd1c244a9ffe236d199088d04c1601c52d401084"
        },
        "date": 1677273982659,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.045470514762659,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.7403839577489775,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.737712363146342,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 14.882849858980288,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.4654677815638615,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.119819256449626,
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
          "id": "56fc5ad12012630b691d3b75db75de7346b24e5a",
          "message": "Remove views",
          "timestamp": "2023-02-26T01:12:37+01:00",
          "tree_id": "f0672547f18843c9ccf509a5e88f770d6a454361",
          "url": "https://github.com/aherlihy/carac/commit/56fc5ad12012630b691d3b75db75de7346b24e5a"
        },
        "date": 1677370746148,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.41667788913407,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 2.7918846148531555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.562744639403544,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 14.59209463217751,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.8850477471848968,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.2710014297970695,
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
          "id": "2a91e70687f5bbee1bab23ddb30474da363e0ec5",
          "message": "Add bench for dotty",
          "timestamp": "2023-02-26T13:06:27+01:00",
          "tree_id": "738ffaa6de921d8da1a0b023bb184454b5f399de",
          "url": "https://github.com/aherlihy/carac/commit/2a91e70687f5bbee1bab23ddb30474da363e0ec5"
        },
        "date": 1677413586965,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.225606444742354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.413894983976667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.181645202074208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 15.049913699842795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.187520254253948,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.5988371664035905,
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
          "id": "f61b220f6a6b3b173db08a6fbd4115f00e1556b3",
          "message": "cleanup bench",
          "timestamp": "2023-02-26T18:22:12+01:00",
          "tree_id": "34a1f1d65e3fdee263855147b84c289233dbaffd",
          "url": "https://github.com/aherlihy/carac/commit/f61b220f6a6b3b173db08a6fbd4115f00e1556b3"
        },
        "date": 1677432519906,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.299122484387382,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.152106560941083,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.895770850214652,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 12.896829800038214,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.353926558563097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.513867498606616,
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
          "id": "ba954c2bd8f405b1c6edd0c6913f55247b9544e2",
          "message": "Add check for recompile",
          "timestamp": "2023-02-27T00:14:29+01:00",
          "tree_id": "a6fd65ca444314fe2fbac2fc8399fad28bb80af5",
          "url": "https://github.com/aherlihy/carac/commit/ba954c2bd8f405b1c6edd0c6913f55247b9544e2"
        },
        "date": 1677453725881,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.050913166726362,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.5329313748047975,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.747018291707198,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 19.85929613895548,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.363092609878694,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.872236872282412,
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
          "id": "0fc6816e78cfa08596a883a2f98b69dbf94e73eb",
          "message": "ackerman specific bench",
          "timestamp": "2023-02-28T11:33:42+01:00",
          "tree_id": "6f8948def535b63004d91fe07a948ea2dc0a4c3b",
          "url": "https://github.com/aherlihy/carac/commit/0fc6816e78cfa08596a883a2f98b69dbf94e73eb"
        },
        "date": 1677580840795,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.6242209434493056,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.151677327329927,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.861189642822522,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 14.94961540033241,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.924962422409162,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.632557456002004,
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
          "id": "9e10ac9b3cf20762902e41d6b5540d914797a5a9",
          "message": "remove loopbody from ack bench",
          "timestamp": "2023-02-28T13:47:27+01:00",
          "tree_id": "410dd186050fa4336a2a833f698f0566243da45c",
          "url": "https://github.com/aherlihy/carac/commit/9e10ac9b3cf20762902e41d6b5540d914797a5a9"
        },
        "date": 1677588866370,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.344257354943295,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.0445868902401,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.324674639507883,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 15.759824666977886,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.086833138522195,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.6658230026991128,
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
          "id": "5b9862e188a2cf6800619bd2f9a5723ff29330d7",
          "message": "bench for jit at evalrule",
          "timestamp": "2023-02-28T14:22:36+01:00",
          "tree_id": "ddd6bf248c1d2e3fd13ec83e052a5d3ba06be5df",
          "url": "https://github.com/aherlihy/carac/commit/5b9862e188a2cf6800619bd2f9a5723ff29330d7"
        },
        "date": 1677590973843,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.214232555428572,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.0986843865728035,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.183385897526981,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 13.358364824421889,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.8463354276119994,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.701458595827176,
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
          "id": "18c7340ec95a450cb77c77c18242fddca2134f16",
          "message": "remove pl",
          "timestamp": "2023-02-28T14:24:47+01:00",
          "tree_id": "3a1388c7df767cfcd445ff7f5519213141388c81",
          "url": "https://github.com/aherlihy/carac/commit/18c7340ec95a450cb77c77c18242fddca2134f16"
        },
        "date": 1677591182437,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.231486889330656,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 7.318558313698242,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.335433944465269,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 27.278684584473428,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.961588753485002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.7598692835359508,
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
          "id": "f872604c45ffbbf989b29f1430a7e4cacd58ee70",
          "message": "reuse compiler for bench",
          "timestamp": "2023-02-28T15:16:52+01:00",
          "tree_id": "a7d8c5cfb5fbbc41c1dff5902f29602f8c4b927e",
          "url": "https://github.com/aherlihy/carac/commit/f872604c45ffbbf989b29f1430a7e4cacd58ee70"
        },
        "date": 1677594169474,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 2.9794264634751286,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.053021258421565,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.642051485744287,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.3506044333055707,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.3892664536662562,
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
          "id": "7ee27a8797eadc5cd18bc1d701515a47f9e87810",
          "message": "try warmup invocation",
          "timestamp": "2023-02-28T15:29:57+01:00",
          "tree_id": "eb2ea67d7f2d1660e8987498ba23fa74fa8f8246",
          "url": "https://github.com/aherlihy/carac/commit/7ee27a8797eadc5cd18bc1d701515a47f9e87810"
        },
        "date": 1677594964717,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.559355517574363,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.9728102796993054,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.356201547331362,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.352604920296712,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.546457258174625,
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
          "id": "910690df5d81f111ef46c72a71a1e83f0df638ec",
          "message": "bench at gran ack",
          "timestamp": "2023-02-28T15:55:09+01:00",
          "tree_id": "e727f7872ca65f2704f1055221b128f64afddac3",
          "url": "https://github.com/aherlihy/carac/commit/910690df5d81f111ef46c72a71a1e83f0df638ec"
        },
        "date": 1677596465105,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.6480911368150393,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.88160938406516,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.8041100665848555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.0734094343894527,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.2021617965151417,
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
          "id": "8e65c3525aa16502bcb059ed464b6cb4a61330e2",
          "message": "Add equal bm",
          "timestamp": "2023-02-28T16:36:24+01:00",
          "tree_id": "9ab5a90cc5f947cf45b92ed38a720e449944027f",
          "url": "https://github.com/aherlihy/carac/commit/8e65c3525aa16502bcb059ed464b6cb4a61330e2"
        },
        "date": 1677598976453,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.3408712355829815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.597779516377304,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.140340840052811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.7893128643556073,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9341434649908074,
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
          "id": "359dcd75d51be151ce8e843983d8edb30f11bb60",
          "message": "Fix typo",
          "timestamp": "2023-02-28T16:47:10+01:00",
          "tree_id": "eaec4381f803c654cba9c91e96a365eca94bb154",
          "url": "https://github.com/aherlihy/carac/commit/359dcd75d51be151ce8e843983d8edb30f11bb60"
        },
        "date": 1677599594449,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.173499165793038,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.809051778083417,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.3204066020188066,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.154716840094208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.6348767688998183,
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
          "id": "4197b0cbbd7268f3e297532441f1b75f9ee202bb",
          "message": "store ordered children in IROp",
          "timestamp": "2023-02-28T17:13:33+01:00",
          "tree_id": "e6d321968be7b514026c8d99e943337c63f14e7e",
          "url": "https://github.com/aherlihy/carac/commit/4197b0cbbd7268f3e297532441f1b75f9ee202bb"
        },
        "date": 1677601219285,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.732008821094631,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.787729939686557,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.988351005061266,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.725195770685195,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9513940759658606,
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
          "id": "21fe6b8767102f7be76b9cabcf220a88785eb9da",
          "message": "bugfix",
          "timestamp": "2023-02-28T18:29:45+01:00",
          "tree_id": "73779d9782aae7aa01030771e084abf11469b5ef",
          "url": "https://github.com/aherlihy/carac/commit/21fe6b8767102f7be76b9cabcf220a88785eb9da"
        },
        "date": 1677605793861,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.394916698820355,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.289003127063559,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.4956229961016305,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 20.00251453617964,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.172453346470061,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9176154577111373,
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
          "id": "1c8d6160112fe984ec95b6be867e5a9249dbee55",
          "message": "revert child array",
          "timestamp": "2023-02-28T18:39:11+01:00",
          "tree_id": "8499f13f15ca8ffdc223b362ddff1a8d45427247",
          "url": "https://github.com/aherlihy/carac/commit/1c8d6160112fe984ec95b6be867e5a9249dbee55"
        },
        "date": 1677606348165,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.227078196162808,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.547427422998345,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.471415111634931,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 18.077432119055622,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.5415930554274824,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.782304943791432,
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
          "id": "3827c87ff70fb7a7b10c2b0c8ce8e4797daa79d3",
          "message": "bugfic",
          "timestamp": "2023-02-28T20:10:48+01:00",
          "tree_id": "d4e08c7a3203a4c4d40df9998afd27c5c2fe914e",
          "url": "https://github.com/aherlihy/carac/commit/3827c87ff70fb7a7b10c2b0c8ce8e4797daa79d3"
        },
        "date": 1677611843651,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.3412809347244803,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.1992866207819075,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.658562636308504,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 14.743355681778576,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.758417664483342,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.2870565820396984,
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
          "id": "66637d08c290b4ab723db8eb8a785c3a57bf04f6",
          "message": "add x to bench",
          "timestamp": "2023-02-28T20:35:23+01:00",
          "tree_id": "47e3ef9aacb6f6e2cf28f08a625a5aa538826b4b",
          "url": "https://github.com/aherlihy/carac/commit/66637d08c290b4ab723db8eb8a785c3a57bf04f6"
        },
        "date": 1677613366592,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 5.425299871726316,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 6.591775780316324,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.778676319234003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 22.43397812338238,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.281536855855183,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.6440016992820012,
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
          "id": "048a45668eae18d4c55a62a475e0940e5ccd058b",
          "message": "Add array gen",
          "timestamp": "2023-02-28T22:59:28+01:00",
          "tree_id": "308e9e2d536904c5a3c1fc6f4e6ef9910365ba9b",
          "url": "https://github.com/aherlihy/carac/commit/048a45668eae18d4c55a62a475e0940e5ccd058b"
        },
        "date": 1677621965660,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.106184289294198,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.189983789110798,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.230505793402569,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 13.248207415335978,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.847804876168607,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.120576105588222,
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
          "id": "45484aa7b2ad2aa13bc9861d3516598a212eaab0",
          "message": "remove println",
          "timestamp": "2023-02-28T23:03:56+01:00",
          "tree_id": "03f24fb1a2243d3959555e5c6f03affa52447f8c",
          "url": "https://github.com/aherlihy/carac/commit/45484aa7b2ad2aa13bc9861d3516598a212eaab0"
        },
        "date": 1677622238437,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.1809871481272696,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.04936089658826,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.893099203681511,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 15.81259260275662,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.9423131183454485,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.8063121697433637,
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
          "id": "dbf713ddb96925b077857265dfbb2a0c2254815b",
          "message": "clean up",
          "timestamp": "2023-02-28T23:22:54+01:00",
          "tree_id": "c7bc2ac2aca7c0bf20528fed80391ff5ca3327a2",
          "url": "https://github.com/aherlihy/carac/commit/dbf713ddb96925b077857265dfbb2a0c2254815b"
        },
        "date": 1677623380816,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.684454292061867,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.236123088017843,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.8052593727865895,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 17.638085165428556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.5946853492284903,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.4491483212528566,
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
          "id": "3e1bd3b6a859a91c47c8c3e40279d45ce8eafe80",
          "message": "add back sort in interp",
          "timestamp": "2023-02-28T23:59:03+01:00",
          "tree_id": "343ac5c77dc3c45a1943de1bb16e8ee0b5c7fa45",
          "url": "https://github.com/aherlihy/carac/commit/3e1bd3b6a859a91c47c8c3e40279d45ce8eafe80"
        },
        "date": 1677625574237,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 5.868466012711335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.475179589596755,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 8.272814423954033,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 21.110218852461365,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 6.14231369378701,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9569786351321103,
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
          "id": "54999c231801d2d5abfa23700ca1af050d547571",
          "message": "pl",
          "timestamp": "2023-03-01T00:00:41+01:00",
          "tree_id": "6c760f83ac8d8bca15d283eafc5303666caa880a",
          "url": "https://github.com/aherlihy/carac/commit/54999c231801d2d5abfa23700ca1af050d547571"
        },
        "date": 1677625637747,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.88348679050848,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.536348030882621,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.230684621887853,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 12.114576250647715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.373110605713682,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9909851291510647,
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
          "id": "a5fdf3a50f5dd0c9d7d527baac44f024e00cb196",
          "message": "aot for array",
          "timestamp": "2023-03-01T00:12:38+01:00",
          "tree_id": "930cab0cb44499e70b4538b9130fe05ceaf69f7a",
          "url": "https://github.com/aherlihy/carac/commit/a5fdf3a50f5dd0c9d7d527baac44f024e00cb196"
        },
        "date": 1677626355557,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.259146536972242,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.954366591263849,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.582139463619891,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 13.43342246610419,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.060574512342015,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.7446924433134385,
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
          "id": "3bce86a4446d8ce30595b96c033d6dff8bc0ab2e",
          "message": "Add bench",
          "timestamp": "2023-03-01T00:18:32+01:00",
          "tree_id": "4e5553ded8dc251527b3fe03fd1cd5b290e018a4",
          "url": "https://github.com/aherlihy/carac/commit/3bce86a4446d8ce30595b96c033d6dff8bc0ab2e"
        },
        "date": 1677626706821,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.552014769793823,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.673707788709507,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 3.984996133001438,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 15.570181799536769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.934663389228901,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.3685008550703215,
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
          "id": "2ef3770a14fefe633e6eed79e959c09da102fc0d",
          "message": "fix typo",
          "timestamp": "2023-03-01T00:40:52+01:00",
          "tree_id": "e8f4e5c9fca258cb9287d8c80b9019a3374d01e7",
          "url": "https://github.com/aherlihy/carac/commit/2ef3770a14fefe633e6eed79e959c09da102fc0d"
        },
        "date": 1677628048662,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.415796648236019,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.064045153939658,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.521552439349255,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 17.3651937775697,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.018859167062104,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.685965913632512,
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
          "id": "7729ec3fa6825f22b7971d49bbedc7a9f90b14f5",
          "message": "clean",
          "timestamp": "2023-03-01T01:11:33+01:00",
          "tree_id": "7249c1e2e5ab1a0ff655f08332b2c5779dcc226f",
          "url": "https://github.com/aherlihy/carac/commit/7729ec3fa6825f22b7971d49bbedc7a9f90b14f5"
        },
        "date": 1677629916105,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.134741393201918,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.006737655173247,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 8.190502998592242,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 16.36003379016329,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.059182861819464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 2.1452961187199615,
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
          "id": "25ab62ee4b04f4bbd8b77978b838c7bf99ba9a6d",
          "message": "Try new compiler",
          "timestamp": "2023-03-01T01:12:01+01:00",
          "tree_id": "a3aa06d3224ef2281c2edc885cb602d20af83a5a",
          "url": "https://github.com/aherlihy/carac/commit/25ab62ee4b04f4bbd8b77978b838c7bf99ba9a6d"
        },
        "date": 1677629969259,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 5.245228909238662,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.845155365821767,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.604258542528529,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 19.74338636495684,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.572595770288271,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 2.7729271623058582,
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
          "id": "34f1e11f46cade0efb99d2548bf2a379713e3997",
          "message": "Revert new dotty",
          "timestamp": "2023-03-01T01:40:21+01:00",
          "tree_id": "edd2958702bb6ea12e65846f5bb9288135ef73c3",
          "url": "https://github.com/aherlihy/carac/commit/34f1e11f46cade0efb99d2548bf2a379713e3997"
        },
        "date": 1677631684620,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 6.3451926562334275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.634313968136057,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.566817520171253,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 22.97683648723787,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.4827931544017487,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 2.5462152071310227,
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
          "id": "7461940e5e7c200c5166046726afeb0aa830d760",
          "message": "skip future for blocking",
          "timestamp": "2023-03-01T02:01:42+01:00",
          "tree_id": "bf9d083a77b8a6ae4ef5d0889272b6529e759a0e",
          "url": "https://github.com/aherlihy/carac/commit/7461940e5e7c200c5166046726afeb0aa830d760"
        },
        "date": 1677632904250,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.418457733716493,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.860750647807076,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.031897404564811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 17.732823416092344,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.807959514533495,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.8551250774876151,
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
          "id": "b43e2e88bea065189a10e34c276808fa830a911d",
          "message": "Add back invocation",
          "timestamp": "2023-03-01T02:02:35+01:00",
          "tree_id": "86d442bd7ecd6393cea1a1098ea2cde47b31aff4",
          "url": "https://github.com/aherlihy/carac/commit/b43e2e88bea065189a10e34c276808fa830a911d"
        },
        "date": 1677632949709,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.5039899464688498,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.915765193644392,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.440025766292658,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 15.943998352978056,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.8105855058367295,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.6669439358158187,
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
          "id": "955dc1315880813473c37895bce80ac03d7a32e7",
          "message": "bench",
          "timestamp": "2023-03-01T02:06:11+01:00",
          "tree_id": "0cdd9bc52913a1d8c8ac8d821b9d72ea5a19a98e",
          "url": "https://github.com/aherlihy/carac/commit/955dc1315880813473c37895bce80ac03d7a32e7"
        },
        "date": 1677633171825,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.227023532876336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.815741288631488,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.977095746129235,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 15.903441727304749,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.785984536277527,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.7235639302140051,
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
          "id": "eb98f185dfedfbd332c35181995861c661588a03",
          "message": "Add the rest of the benchmarks",
          "timestamp": "2023-03-01T02:24:34+01:00",
          "tree_id": "552cfdef1460c7d7f7616a7725cb532d416e5b5a",
          "url": "https://github.com/aherlihy/carac/commit/eb98f185dfedfbd332c35181995861c661588a03"
        },
        "date": 1677636217961,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 5.369959040635949,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.442411154051976,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.128061058572956,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 18.834658293105853,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 4.343158065003507,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 2.2104534220580416,
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
          "id": "6d342253d9a5a08328d68daf10e35c16fa9a250e",
          "message": "All ack bench",
          "timestamp": "2023-03-01T14:14:31+01:00",
          "tree_id": "2ae7633ff13ed3e22c3013447aed1ed8a593e82f",
          "url": "https://github.com/aherlihy/carac/commit/6d342253d9a5a08328d68daf10e35c16fa9a250e"
        },
        "date": 1677679086673,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.410365524432057,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.236578772832472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 7.787828883400337,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 22.047424249663823,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.6383277348374827,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.9783563291144741,
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
          "id": "ff51510e72c1c486a75d17cda1603ecbae749030",
          "message": "Add readme",
          "timestamp": "2023-03-08T12:01:50+01:00",
          "tree_id": "8eb267cf913c5d5398ac8e852f5fdcf627c8e79c",
          "url": "https://github.com/aherlihy/carac/commit/ff51510e72c1c486a75d17cda1603ecbae749030"
        },
        "date": 1678275292531,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.708061928086659,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.7762419953133515,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 4.717946136086849,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 13.053544117548288,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 3.876321284758222,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.2479992357248277,
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
          "id": "1121ff6dc4d832661cb2749c561f5f004f2732e8",
          "message": "cleanup options",
          "timestamp": "2023-03-13T13:18:51+01:00",
          "tree_id": "d055a5e1f1fd5d9ea1c1b3adb13a97142ac50da6",
          "url": "https://github.com/aherlihy/carac/commit/1121ff6dc4d832661cb2749c561f5f004f2732e8"
        },
        "date": 1678712038569,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.2443826555853406,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.7387059268217904,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 3.964005967999541,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 17.69288999429072,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 1.6502873495536263,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.2548805658063142,
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
          "id": "7abde88654d0a1f973bdee1f05dbac7251f076df",
          "message": "change threshold",
          "timestamp": "2023-03-13T13:20:29+01:00",
          "tree_id": "324c3c8f61ffaf7d8e9c09fd0f163c75b73cf595",
          "url": "https://github.com/aherlihy/carac/commit/7abde88654d0a1f973bdee1f05dbac7251f076df"
        },
        "date": 1678712623814,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 4.992043620339537,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 5.361114687633799,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.062761100761068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 22.689014480500568,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 1.8620551083951409,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.5382422983197486,
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
          "id": "f51b6bb54bdf992f30bdda572c32d3c97e6f8058",
          "message": "Block for tests",
          "timestamp": "2023-03-13T15:03:51+01:00",
          "tree_id": "09e937d2063166fc0a5536b1ba4d5620e705262b",
          "url": "https://github.com/aherlihy/carac/commit/f51b6bb54bdf992f30bdda572c32d3c97e6f8058"
        },
        "date": 1678718790361,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.329711994580953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.896694127665421,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.269781607302584,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 18.709063248352027,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.186460620655956,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.5247814191839244,
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
          "id": "9f8744d8114fb40062bd0128741923d09e5385cc",
          "message": "re-add alias elimination",
          "timestamp": "2023-03-13T17:11:12+01:00",
          "tree_id": "78f8725cee3d6ae9579def9e0bf4b1ec65807594",
          "url": "https://github.com/aherlihy/carac/commit/9f8744d8114fb40062bd0128741923d09e5385cc"
        },
        "date": 1678725998930,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.636456859110132,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 3.6151657280116973,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 6.877953949807392,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 15.727394829873267,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.372461306397644,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.4461448830947659,
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
          "id": "b1371e659dcad8ed3aac5361db3d28e0dbc27e1d",
          "message": "Remove path-dependent types",
          "timestamp": "2023-03-16T12:01:34+01:00",
          "tree_id": "1713967bdd0cf253ed33fc61013115b865d1f773",
          "url": "https://github.com/aherlihy/carac/commit/b1371e659dcad8ed3aac5361db3d28e0dbc27e1d"
        },
        "date": 1678964966487,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.naive_collections",
            "value": 3.38829606987346,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_relational",
            "value": 4.397057771325988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_compiled",
            "value": 5.524455904411021,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_staged_interpreted",
            "value": 27.66385543646764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_collections",
            "value": 2.5882278660116507,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_relational",
            "value": 1.5008759967217764,
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
          "id": "d92606b58399b4ad42249574255eff4e5989b7c3",
          "message": "Clean up benchmarks a bit",
          "timestamp": "2023-03-27T20:25:46+02:00",
          "tree_id": "294f6d67760118e2596ca875c1722a2ae31a18b0",
          "url": "https://github.com/aherlihy/carac/commit/d92606b58399b4ad42249574255eff4e5989b7c3"
        },
        "date": 1679947610256,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_unordered__",
            "value": 1.3879047231813249,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_unordered__ci",
            "value": 0.25699560708453545,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_EVALRULEBODY_aot_async_unordered__",
            "value": 7.070228417703649,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_EVALRULEBODY_async_unordered__",
            "value": 7.092969067157182,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 0.5902148012257413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.42413051421101927,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.20316454415715382,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.15375396650636025,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.interpreted_unordered__ci",
            "value": 51.97074181199999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.seminaive_default__ci",
            "value": 35.55184086933333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann_benchmark.seminaive_volcano__ci",
            "value": 42.70485024,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.interpreted_unordered__ci",
            "value": 0.6614754179597748,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 3.4538172365629536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.seminaive_default__ci",
            "value": 0.6059597726329918,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen_benchmark.seminaive_volcano__ci",
            "value": 0.6391040508632648,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.interpreted_unordered__ci",
            "value": 581.2381994780001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.seminaive_default__ci",
            "value": 266.59762489599996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anon_var_benchmark.seminaive_volcano__ci",
            "value": 452.44009931599993,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.interpreted_unordered__ci",
            "value": 10.881551239417869,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 37.6770565696044,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.seminaive_default__ci",
            "value": 10.181289347990505,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cba_expr_value_benchmark.seminaive_volcano__ci",
            "value": 14.511503374876321,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.interpreted_unordered__ci",
            "value": 22.80449391669744,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 59.86608018784077,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.seminaive_default__ci",
            "value": 23.12572679401891,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique_benchmark.seminaive_volcano__ci",
            "value": 16.217473729822437,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.interpreted_unordered__ci",
            "value": 2.3569176949186295,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 9.278147974419074,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.seminaive_default__ci",
            "value": 2.2119061349957585,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer_benchmark.seminaive_volcano__ci",
            "value": 2.003426695681133,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.interpreted_unordered__ci",
            "value": 115.17713988738899,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 237.5207014013377,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.seminaive_default__ci",
            "value": 114.10934646350563,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal_benchmark.seminaive_volcano__ci",
            "value": 62.10556760371278,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib_benchmark.interpreted_unordered__ci",
            "value": 150.9267870300477,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 343.14896852487743,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib_benchmark.seminaive_default__ci",
            "value": 102.67095426297674,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib_benchmark.seminaive_volcano__ci",
            "value": 122.43755371545856,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.interpreted_unordered__ci",
            "value": 1.8017492004090099,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 6.341919197152896,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.seminaive_default__ci",
            "value": 2.0682618242211808,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func_benchmark.seminaive_volcano__ci",
            "value": 1.5395381943054054,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.interpreted_unordered__ci",
            "value": 0.25311373750027477,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 0.9113853026715877,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.seminaive_default__ci",
            "value": 0.26436635870738323,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad_benchmark.seminaive_volcano__ci",
            "value": 0.27564121713359785,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.interpreted_unordered__ci",
            "value": 5.926462987256741,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 30.437050495021413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.seminaive_default__ci",
            "value": 3.9709963685861283,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.input_output_benchmark.seminaive_volcano__ci",
            "value": 5.921877283103416,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.interpreted_unordered__ci",
            "value": 71.14070970269663,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 157.54342995714745,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.seminaive_default__ci",
            "value": 66.89166428070573,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_after_benchmark.seminaive_volcano__ci",
            "value": 46.80933727094726,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.interpreted_unordered__ci",
            "value": 10.832456076167839,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 29.851529007700822,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.seminaive_default__ci",
            "value": 10.887768960014935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.is_before_benchmark.seminaive_volcano__ci",
            "value": 7.4523719905798815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.interpreted_unordered__ci",
            "value": 0.4828315203175323,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 1.3508614440507956,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.seminaive_default__ci",
            "value": 0.4914202730450251,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.java_pointsto_benchmark.seminaive_volcano__ci",
            "value": 0.3490319827421985,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.interpreted_unordered__ci",
            "value": 1.1913865873028686,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 4.563456723539072,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.seminaive_default__ci",
            "value": 1.1094342130016202,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro_benchmark.seminaive_volcano__ci",
            "value": 1.2652875658192988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.interpreted_unordered__ci",
            "value": 84.35030440213237,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 182.17616432952275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.seminaive_default__ci",
            "value": 79.98378054801108,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome_benchmark.seminaive_volcano__ci",
            "value": 60.72476425314733,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.interpreted_unordered__ci",
            "value": 155.512425555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 172.18017300666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.seminaive_default__ci",
            "value": 150.31175518999999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto_benchmark.seminaive_volcano__ci",
            "value": 165.881802475,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.interpreted_unordered__ci",
            "value": 98.6465816208655,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 311.0946320587742,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.seminaive_default__ci",
            "value": 109.06080807939227,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime_benchmark.seminaive_volcano__ci",
            "value": 69.6082570657311,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.interpreted_unordered__ci",
            "value": 6.257947207870316,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 20.149136904874645,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.seminaive_default__ci",
            "value": 8.69535626690068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle_benchmark.seminaive_volcano__ci",
            "value": 4.789707786262196,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo_benchmark.interpreted_unordered__ci",
            "value": 216.20303206666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 218.7839742666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo_benchmark.seminaive_default__ci",
            "value": 90.89465677000001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo_benchmark.seminaive_volcano__ci",
            "value": 150.67137572,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.interpreted_unordered__ci",
            "value": 1.2061742706257514,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 4.078928087264721,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.seminaive_default__ci",
            "value": 1.1757169957386373,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg_benchmark.seminaive_volcano__ci",
            "value": 1.065209029445511,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.interpreted_unordered__ci",
            "value": 0.4698793944372189,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 1.6332845493086112,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.seminaive_default__ci",
            "value": 0.44740251696324523,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship_benchmark.seminaive_volcano__ci",
            "value": 0.4095242329329068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.interpreted_unordered__ci",
            "value": 1.151940375185152,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 4.843852914222821,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.seminaive_default__ci",
            "value": 1.032650017543673,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small_benchmark.seminaive_volcano__ci",
            "value": 1.0156853189622563,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.interpreted_unordered__ci",
            "value": 1.6873253589385129,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 5.699860434440312,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.seminaive_default__ci",
            "value": 1.6377219957563143,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc_benchmark.seminaive_volcano__ci",
            "value": 1.3864410466052823,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.interpreted_unordered__ci",
            "value": 0.9745895661537596,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 4.254802497292916,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.seminaive_default__ci",
            "value": 0.8753926828155947,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic_benchmark.seminaive_volcano__ci",
            "value": 0.7488619611427441,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.interpreted_unordered__ci",
            "value": 33.18217189033333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.seminaive_default__ci",
            "value": 27.817649106666664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains_benchmark.seminaive_volcano__ci",
            "value": 28.45477712666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.interpreted_unordered__ci",
            "value": 0.6858409889346528,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 2.3012049405955097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.seminaive_default__ci",
            "value": 0.6618008648815359,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans_benchmark.seminaive_volcano__ci",
            "value": 0.5594794996004395,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.interpreted_unordered__ci",
            "value": 1.4357881723487897,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.jit_EVALRULEBODY_blocking_unordered__ci",
            "value": 6.0294868218052216,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.seminaive_default__ci",
            "value": 1.325598292350874,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree_benchmark.seminaive_volcano__ci",
            "value": 1.1666943475026457,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}