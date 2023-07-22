window.BENCHMARK_DATA = {
  "lastUpdate": 1690043719788,
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
          "id": "a1a33554ff3ce6674ff7254b506574f981a44fe7",
          "message": "Add plugin for jar",
          "timestamp": "2023-04-18T15:35:33+02:00",
          "tree_id": "4a7bf5bc85ab622308a49a5c244f2f25d4b8558d",
          "url": "https://github.com/aherlihy/carac/commit/a1a33554ff3ce6674ff7254b506574f981a44fe7"
        },
        "date": 1681831259886,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.6013899052379643,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.24672568157154134,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 7.916010396406657,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 8.279324672047935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.5740640637617759,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.4207666271360975,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.3419552863293731,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.27501674172046703,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.2637250549591975,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 50.59193170799999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 35.66826215285714,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 40.58065716307693,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.6425055390290636,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.9118665500781202,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.5909278945206786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.6517553843205905,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 591.203041326,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 578.489299976,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 292.77152514200003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 266.041768192,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 11.428113682648211,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 33.103866216818616,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 9.374628358283099,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 14.251221566981616,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 23.242616507794796,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 59.82135911140631,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 22.96608912110749,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 18.344735182131576,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.297867981001815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.640575434529648,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.2397716390076488,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.01975808736354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 110.53880179998207,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 258.98107221747796,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 117.57279190168353,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 80.80579631565267,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 156.69707630653392,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 313.94145819812553,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 131.1447213976172,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 121.19875250448999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 2.2939025848383388,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.435004581901684,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.8271894914441387,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.7115046417086472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.250013537024443,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.9137486283769893,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.27710549195552464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.2834295460055544,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.938598470909523,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 27.26340611626884,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 3.908837208789417,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.7706149331381615,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 72.84568129481984,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 149.74988276860046,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 69.5476242292095,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 52.35376454340096,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 11.588743734484327,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 26.144855994688402,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 11.154385720652765,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 7.870011546157883,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.4662860455040597,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.3645975590929413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.4614099872576448,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.3518569389231242,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.1549615539009825,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.6110722607350185,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.0913948057230416,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.2039475638748618,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 85.11563473828953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 184.5520530554665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 83.83694235443545,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 65.05080151670037,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 160.88676820999999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 227.67502144166667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 151.82419462,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 103.711524496,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 97.59603507210531,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 227.41503441789092,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 116.40387872237586,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 70.22303947767051,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 6.781129553981744,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 20.404887612997562,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 8.363352623590938,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 5.3587046551074105,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 135.738525295,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 214.83870199333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 93.27350646666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 87.83590007999999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.169269037182269,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.157420942810689,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.1564499581925893,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 0.9598471386620993,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.47355319219284164,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.5479760687332456,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.4451482566998089,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.4034590678996806,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 1.1237201569034885,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.60774766553291,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.0134311148259503,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.0001744598444517,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.65259150997325,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.4429451977048755,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.6302789888834976,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.3994068169387028,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 0.926294787083164,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.798931908821408,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 0.8840751875735045,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.7356564886032378,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 31.627886516249998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 29.269537253333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 28.004244446666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.6886645824949831,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.260364711701666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.6721767864428357,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.5447058614998015,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.3816078308508,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.743874098936493,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.3844689772261345,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.086924806615946,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "alexandre@piveteau.email",
            "name": "Alexandre Piveteau",
            "username": "alexandrepiveteau"
          },
          "committer": {
            "email": "noreply@github.com",
            "name": "GitHub",
            "username": "web-flow"
          },
          "distinct": true,
          "id": "4fc7124d74e93e6c81fe64746ac06151db891635",
          "message": "Expose strongly connected components on `PrecedenceGraph` (#24)\n\n* Expose strongly connected components on `PrecedenceGraph`\r\n\r\n* Handle `alias` updates in `PrecedenceGraph`\r\n\r\n* Apply code review suggestions for tests\r\n\r\nCo-authored-by: anna herlihy <herlihyap@gmail.com>\r\n\r\n* Reorder imports\r\n\r\n* Apply suggestions from code review\r\n\r\nCo-authored-by: anna herlihy <herlihyap@gmail.com>\r\n\r\n* Remove an unnecessary comment\r\n\r\n* Rename `nodes` into `buildGraph`\r\n\r\n* Test alias removal\r\n\r\n* Fix handling of multiple alias updates\r\n\r\n---------\r\n\r\nCo-authored-by: anna herlihy <herlihyap@gmail.com>",
          "timestamp": "2023-05-29T17:00:52+02:00",
          "tree_id": "626a9f737aa37791154e147630b3a1598f41f600",
          "url": "https://github.com/aherlihy/carac/commit/4fc7124d74e93e6c81fe64746ac06151db891635"
        },
        "date": 1685378081463,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 2.483640433597419,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.2680128776356542,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 11.457766671226896,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 13.270174976352205,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.6407636560532396,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.554472636135231,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.6532116861754427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.3527143547711188,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.28708435076803845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 63.28621851472222,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 46.631469120909095,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 50.93912093054546,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.092638195070988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.050569923578429,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.984222233675361,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 1.0234700940307782,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 217.795948076,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 230.233345968,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 129.082770456,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 176.364536536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 14.199320776065258,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 38.963630373875134,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 10.837369716174885,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 17.282053367372242,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 27.51202515432425,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 71.02675010718409,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 28.717207614123605,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 22.784751212109384,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.1791900146866974,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 12.477509470152203,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 3.3017834935886925,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 3.2259710173634004,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 147.96047995250444,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 277.7045279364371,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 143.21310147614827,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 108.91088934737634,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 180.72577399681717,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 349.13613577458244,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 143.37953765587977,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 161.11608851434795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 2.5600230926703373,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.816911358694838,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 2.717678076373907,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 2.979813244798417,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.47716341854127464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.9237706374369286,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.5066410393757484,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.5148405763005043,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 6.2815092140600335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 21.81080352680883,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 5.596824394464659,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 6.02689389363677,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 88.10114084275617,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 183.10256192853316,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 87.9691261345412,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 71.17624963111874,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 14.062600510545348,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 32.10750066412473,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 14.718393799267293,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 11.283475152105359,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.5733278851117097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.459728950308631,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.5593047878421495,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.4534622080322651,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.693313203417206,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.6574697090845065,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.5360368578674077,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.9427366572209066,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 106.67200929485867,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 202.36526977898885,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 100.37951880696022,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 88.60333744151349,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 196.76006121333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 197.22054136666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 209.5516578066667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 139.25260472999997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 88.4531114242299,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 206.92376312632751,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 104.78819266017373,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 68.66290915588553,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 8.301074160607804,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 23.698652649596905,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 11.138281024213422,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 7.668790517076905,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 178.02019116666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 182.13967518666664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 114.95606054800001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 178.27567485333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.774326039568809,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.670416637716977,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.6845588645230272,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.626066339123507,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.7755729492130413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.8152990877140764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.7251976213297789,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.6814028225564883,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 1.714242291225921,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.888553919103144,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.5885278673307865,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6956280313018284,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.4222871826316554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.18659101963117,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.374643275666753,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 2.0669642795887224,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.5885420343139234,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.038555636988539,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.4461488520059373,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.3668971947936819,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 44.73056743166667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 90.31842426666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 35.69159622638095,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 24.790250179047618,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 1.0740426840502895,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.4757570932217177,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 1.0133118749243768,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.893259871438014,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 2.2071193693961257,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.508478917001721,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.9410953198271224,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.789820174774799,
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
          "id": "c4bb3741af3c530e25a0604d9f6dbe96d6c3e802",
          "message": "update scala",
          "timestamp": "2023-07-20T17:41:56+02:00",
          "tree_id": "a7f6a830a6aee0d9bb7d4cc87ce65998bdbf8337",
          "url": "https://github.com/aherlihy/carac/commit/c4bb3741af3c530e25a0604d9f6dbe96d6c3e802"
        },
        "date": 1689873384730,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.860715421376069,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.4274436135837102,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 8.717320510008056,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 9.696744367577644,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.8073945472102567,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.5791386806329946,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.6451141914698137,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.3990253147105521,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.16942284777916133,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 71.44643141321428,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 46.61795783454546,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 55.34699821955555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.9128031248340225,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.219780576232887,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.82998440317569,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.9176557874331476,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 256.27112802199997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 260.94332703599997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 121.72166025000001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 186.729145914,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 13.679515954184472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 46.24105849395529,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 11.295018430009481,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 17.845411973017967,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 30.684479081357484,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 76.70759541307032,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 30.316739541035496,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 21.855890651880166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.0476132889337357,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 11.799712721194862,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.9732510358534716,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.8087192431709385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 145.80375026894723,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 301.0727483846717,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 154.7734616629893,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 87.62478707190571,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 209.20476200297102,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 426.9022230612377,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 143.66970753178938,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 164.7469672130734,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 2.455766118742094,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.481556036245166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 2.443004611928393,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 2.773137484332836,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.34618828287269776,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2944811336756756,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.3819979783306512,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.3952654104541587,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.4533971471389915,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 19.060745501393317,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 4.713691851743769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.469099294361021,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 101.12096843350312,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 207.13414407061973,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 94.95577505440504,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 65.61746141261764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 15.24157678851241,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 33.49874057951356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 15.193812373670834,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 10.294757941526557,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.6159298689628361,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.6306321165759385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.6153929323053623,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.4928368503296693,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.5028452130431948,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.663627335871169,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.47259601215135,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.5959603357676522,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 110.2278552619586,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 228.57163607109345,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 110.45830904852258,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 84.50515281404483,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 304.09940974,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 227.39962135333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 204.52733083333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 218.27736211333337,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 98.20773218467124,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 208.0587338174343,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 110.27206261076643,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 66.99143219374922,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 8.413016039326642,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 24.459860690534047,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 9.8694176776539,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 6.394403988928415,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 280.26710393,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 313.98001839,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 113.539671752,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 205.04992280666664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.624804940303563,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.002707744123207,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.595215781692072,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.3146377816827897,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.6539212100417038,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.2011356649274076,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.5735673356036075,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.5785193675352599,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 1.515548316073139,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.851396064430378,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.397515045173718,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.3715280704787594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.176473420120458,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.141061220826524,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.194741031951047,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.749302240363556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.331484733056475,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.011113132449553,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.2590478364555462,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.0542107247357042,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 64.84468423249999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 39.89298388461538,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 36.786110380000004,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.9378587168408359,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.7301611091056603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.8721220895381949,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.7106579836748221,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.9362900230561606,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.879948339261114,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.8115196960868822,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.5358515765794662,
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
          "distinct": false,
          "id": "cdd152d50b9550f13b0f870ac5b4c3f57a824759",
          "message": "Add PT tests from tasty-carac",
          "timestamp": "2023-07-22T16:28:45+02:00",
          "tree_id": "c38472dd526b3a70f0ab5861c27ea4e4536f5553",
          "url": "https://github.com/aherlihy/carac/commit/cdd152d50b9550f13b0f870ac5b4c3f57a824759"
        },
        "date": 1690043014788,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.5576170888073733,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.2502423514778124,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 7.225278998483892,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 7.784627995248141,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.5093161747374403,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.3729041162416994,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.5539871416044724,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.32517975320498244,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.1666869950411651,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 52.264518214000006,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 35.88554777,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 44.72503260666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.6989249047236129,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.3207560664746496,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.6636691974742228,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.7075264215915409,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 188.52762428,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 193.77972455,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 101.36528066000001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 150.17134059400001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 9.286867327349778,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 34.688664049732566,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 9.950196083011004,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 14.29330634679915,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 24.12417946163463,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 62.59643961232115,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 23.848827667970657,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 17.801668577561898,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.4566444140242654,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.426507889046302,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.314681863365356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.2218499293970786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 117.55697143246434,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 244.98165081244733,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 122.48421510077075,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 70.61154705441797,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 153.4569800146259,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 179.88842888244068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 110.36908199057987,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 124.26401967485754,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.904609366985828,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.514051962190708,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.9386552212632566,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 2.246656835970506,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.27950754459999677,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.054296519087341,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.3095252203503881,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.3198983388361637,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 4.215809847889934,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 15.643519598402815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 3.901056157056805,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 4.280971708012636,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 73.34493967300587,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 154.78717700576811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 72.96591792015315,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 59.85450509433268,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 11.893954960259322,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 29.210701864947094,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 11.870599235472287,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.51841254879447,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.48312836073242654,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.3037441172180233,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.4782490304305652,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.37609991749942717,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.2356024723821633,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.768492996594889,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.1577933184009974,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.3793797495838598,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 85.79780199912439,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 195.3071234056961,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 87.85014405286054,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 83.76266648339778,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 171.35041636666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 180.26244511333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 167.231865475,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 108.179500628,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 73.32902093529609,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 166.21060387184866,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 86.32301215947976,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 53.02693234579171,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 6.573237659092866,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 21.747484739173107,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 9.053802767846562,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 5.647961530693516,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 147.81103825999998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 148.29567793500001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 95.87610407666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 152.36547752,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.2702267866986527,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.2558122132183165,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.2086329619399485,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.0628084008415868,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.5184595324212367,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.710075158830842,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.4962721172513721,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.434030352142611,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 1.179355533590969,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.054853044993885,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.1055639752128343,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.122830778372487,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.7539947273772991,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.698100960082361,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.7283892521553401,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.4626499384575637,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.0565151665691228,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.078317725739987,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.0218741407404635,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.9154796187840603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 35.99477450142858,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 40.24427562615385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 29.277450571503273,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.7139828669404327,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.2416272177212138,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.7033141639957797,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.5815353536140746,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.5455440408476946,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.751618511059213,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.3426914935438297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.248805001714318,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      },
      {
        "commit": {
          "author": {
            "email": "herlihyap@gmail.com",
            "name": "anna herlihy",
            "username": "aherlihy"
          },
          "committer": {
            "email": "noreply@github.com",
            "name": "GitHub",
            "username": "web-flow"
          },
          "distinct": true,
          "id": "0d433b4acfa589fe2c551a2633865075efb92f3e",
          "message": "Stratified Datalog (#27)",
          "timestamp": "2023-07-22T16:56:46+02:00",
          "tree_id": "99c4787b52977fd32ac77d43f957cf126d11cda9",
          "url": "https://github.com/aherlihy/carac/commit/0d433b4acfa589fe2c551a2633865075efb92f3e"
        },
        "date": 1690043718686,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 2.124890355438886,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.6347203379055386,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 5.228315920152037,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 5.5257918503202035,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.3958333352752845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.3441441692736986,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.549676628836173,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.2549749808989649,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.1874905408797194,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 69.569694845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 107.98320636,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 45.91483227651515,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 53.137493633999995,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.0227652080293974,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.262103166999968,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.8807588962502519,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.9330665485352698,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 271.553598676,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 268.636635262,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 126.47822882999999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 193.58133182199998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 9.613917360461713,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 29.194910748076445,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 8.05640049008469,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 11.857703566964178,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 62.505583598335534,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 134.93229468803654,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 74.52838842622997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 40.32472614743651,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.3278579472657626,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 11.683622531035764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.74824428388339,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.687291852501115,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 152.35716245457976,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 328.96628509591676,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 160.63416869382792,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 85.91989563792653,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 207.89395263356988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 445.2555510239566,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 176.91445804295472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 157.8172306236184,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.9452226359278022,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.702835787431818,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.6329022526836579,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.9813480916935684,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.3728739749984884,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2842966918549954,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.3822799034209382,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.41846544570112953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 7.286783535017536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 26.771872091147294,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 6.06376369454663,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 7.4691753309590725,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 99.77891231817492,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 213.41484361549618,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 98.14884909321201,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 67.65856424838793,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 15.971945082941266,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 37.966026636440446,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 15.37131394846666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 9.966024143721095,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.5576339847904092,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.468371779399322,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.3931100158379089,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.41644170342070536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.890504459593135,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.6451408822145694,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.8786602917000657,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.9612246663202781,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 58.349951027836745,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 137.23792348173757,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 62.34936070764238,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 84.83074136426214,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 324.20134850000005,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 337.54194035,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 233.04180954666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 228.0557547266667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 79.35571285345937,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 175.57529855484322,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 84.11616032169769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 54.158518883344264,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 18.937049397228627,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 42.17014642183738,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 20.747481143505492,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 15.656369100734906,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 211.61099088666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 214.03324442,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 116.76786603600002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 110.39019476400001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.5737298339851127,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.157364603222208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.5295725546287346,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.3216970073353855,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.6713182717118583,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.2148306510949607,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.6090721166472493,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.5738181234122526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.5595499521731186,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.654675193415917,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 2.205152457466097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.97020553455923,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.02261665825488935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.09038659780714799,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.03497553258914198,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.02212471794015921,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.395831961924614,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.638990716342031,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.284393347025502,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.789220541043342,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.3199442371912322,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.466889412600528,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.1344137098195306,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.2019229045189141,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 74.04302755714286,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 79.21917568,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 57.158361275555556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 39.83791011230769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.9436093620993228,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.930985952477774,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.8777536589484877,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.7281100013386719,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.8199272286643122,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.236558133623019,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.4423578946908773,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.3151581950231381,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}