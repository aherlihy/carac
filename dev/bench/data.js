window.BENCHMARK_DATA = {
  "lastUpdate": 1681831260658,
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
      }
    ]
  }
}