window.BENCHMARK_DATA = {
  "lastUpdate": 1691181339385,
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
          "id": "aecebc24aac6da9664678ca4728b42d8afe9edd3",
          "message": "tasty test with negationg",
          "timestamp": "2023-07-22T17:40:26+02:00",
          "tree_id": "f9bd5af22bc41ce052529591acbd844d68465f90",
          "url": "https://github.com/aherlihy/carac/commit/aecebc24aac6da9664678ca4728b42d8afe9edd3"
        },
        "date": 1690046084409,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.6988899655576688,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.5566985614491762,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 4.339202802709898,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 4.636871754673884,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.9374392913929878,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.23472537624047568,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.24417418726989376,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.1986420812614809,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.209155513748959,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 51.713280409999996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 29.71879125764706,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 39.3384182,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.8317125309843867,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.6300858456043983,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.6925892456171014,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.7518232165756764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 195.593141472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 200.152715514,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 90.228591106,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 144.867634856,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 7.153707962071783,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 24.454520352782033,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 6.074898674135012,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 9.11216426750285,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 45.40065491563092,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 105.97543627327293,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 52.956326754041264,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 34.164232001605015,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.610626360423966,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.672616083941978,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.17416201848276,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.229516198044613,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 112.39479706733846,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 251.5101083472176,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 113.1838325652601,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 62.771984446101854,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 151.67181793044855,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 330.263202605472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 126.76958847110454,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 122.14984033807418,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.5397638641509492,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.599653765165813,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.164384807972721,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.2208584397646605,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.29353001509580795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1685227509382003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.29685446043812747,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.3326519307791328,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.428320742202088,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 19.546167461163996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 4.5139258804947175,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.803647954415096,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 70.68255032998673,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 157.6058012714046,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 70.4201521272053,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 53.25041402864375,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 11.106858294249491,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 28.681557107451464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 11.074726505377475,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.42301702690036,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.3961614242215108,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.0838030164828887,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.28358386744135655,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.30676133467257793,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.4545300164419914,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.19082546042528,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.435531237938384,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.5057535074305155,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 43.09428183903078,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 99.64207567644685,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 47.22582893563422,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 38.54719237165601,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 227.28605360666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 235.85643814666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 232.77481040666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 163.17313167000003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 60.941303026575056,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 161.7097411358646,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 66.39600413411351,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 78.95072897294862,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 11.83314304263773,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 32.9752360857575,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 15.588859832188643,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 8.410173544678603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 146.752223765,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 224.36690920666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 86.90156910333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 152.06528616,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.2691147023832179,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.367787740987346,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.1859074177479378,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.0481143254114145,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.5347368929300291,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.8621794133538774,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.46910387042413354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.4537550962772401,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 1.994819051118276,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.731023694125895,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.7418543110117621,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.5900338319150036,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.01738364848541371,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.05997477279552964,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.02756716819052707,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.013923802747308162,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.8582663057214455,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.474028525375502,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.80379292035051,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.3799451866427188,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.1109491487875047,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.877675554860987,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 0.9244797903467511,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.8923750848739903,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 55.1850815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 58.77955621555556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 42.37552849,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 31.365687868382356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.7289761918358755,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.4194043685536086,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.6784125604698893,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.5864045699712155,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.4387151926685786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.250321229431259,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.149514742453908,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.0420994927075404,
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
          "id": "3a5c4726730be370aacabc336b96c158f54c483a",
          "message": "rename listlib -> tastylistlib",
          "timestamp": "2023-07-22T17:43:10+02:00",
          "tree_id": "b08858ab6dd622a26fbad23886b6fcd920bf3647",
          "url": "https://github.com/aherlihy/carac/commit/3a5c4726730be370aacabc336b96c158f54c483a"
        },
        "date": 1690046478295,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 2.924394974229716,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.6676095829272907,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 6.907563644409341,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 7.852224688597661,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2639765110720815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.3531910855664469,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.3586015133486998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.3185600305764765,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.20274108491621376,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 38.84195109967033,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 45.64073554742424,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 48.93270603454546,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.1886021227986163,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.962004034762779,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 1.0389358043729413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 1.091960883762099,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 249.60078814,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 248.13139999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 123.312305268,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 181.703091828,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 10.327319296671053,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 30.670263531299277,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 7.961543278298608,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 11.82962079071441,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 58.967501541278885,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 121.64279907000316,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 73.32731407539958,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 43.31658106217353,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.804365268868348,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 13.235842562430097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 3.286272292395277,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 3.1907165540431874,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 148.7779954304073,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 260.23836830333397,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 154.55537500750773,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 97.85341286980564,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 183.10643866151284,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 263.0591909751759,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 137.19409769327416,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 154.20381010198656,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.725914310235407,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.143612002790073,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.7099069230025943,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.7125962283335305,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.5229575645704412,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.9236351961598381,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.5159896221922584,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.5712037979434542,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 7.634158565465998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 26.33288888884254,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 7.008760631134786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 8.215013512764923,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 97.49471077884814,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 193.54520873239025,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 92.04665170028764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 80.6792331801242,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 15.136258736048754,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 36.09605384925616,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 15.00081231139328,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 11.545731346100089,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.5677065138985182,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2699642247012528,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.3944573763445308,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.38503095814462446,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 2.1154921404291622,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.979274198401659,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 2.078390317531948,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 2.2643420152812004,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 56.543305283049975,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 111.15711515155718,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 67.3462844344389,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 43.86703309016879,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 230.37846608,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 242.55680354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 279.64388873999997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 209.17375266666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 80.43746975123483,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 171.38466316652858,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 85.15951635279248,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 61.57444570976727,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 15.910424239590904,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 39.31966446930515,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 20.845649338640676,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 11.726815206632272,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 195.73390009333335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 199.51219791333332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 130.91202184,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 118.01012444799998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.8086890277187646,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.795028316926517,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.6920442186680351,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.5844585055180482,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.819402442035791,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.703930610728997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.7111559715108934,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.7865501349755979,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.836833250623287,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.51094325536506,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 2.4701957731925046,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 2.473171335874044,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.027426397389775935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.08657947592519824,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.035224505464259336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.02075305429605993,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.5964404308218594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.554308085098664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.4051840415168164,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 2.0988112233947853,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.654386430998081,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.349416283655472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.2988703095024363,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.4088584345933057,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 81.98269546285714,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 74.83071592571427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 56.09192630222223,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 42.615636904999995,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 1.1107281603719164,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.424189045005685,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.9873572817804945,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.9192068432305623,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 2.070649794933697,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.12389413355154,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.6010308472810237,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.623386616449866,
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
          "id": "81d0473f36953c9650bb4f812879060efa0c17c1",
          "message": "add bench",
          "timestamp": "2023-07-22T17:54:09+02:00",
          "tree_id": "66763a30a97e6176a32b021eb4d0d60346233c5c",
          "url": "https://github.com/aherlihy/carac/commit/81d0473f36953c9650bb4f812879060efa0c17c1"
        },
        "date": 1690047430861,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.8346116097235154,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.7366111343279825,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 4.659158809149883,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 5.028469507965096,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.891983315961068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.25788423484881184,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.35129069254265655,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.2564602901575408,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.1758073462526227,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 52.903681526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 39.219662018461534,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 40.443857649230765,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.8522650788620083,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.602018071558316,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.7506940142744198,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.8026512942006374,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 212.31659658999996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 205.498816498,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 99.13430825399999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 154.55399139000002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 8.309622198046213,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 21.551556617752972,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 7.077826600408899,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 9.441661817425906,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 48.01344427009191,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 104.00424069371313,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 59.4032662119432,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 34.958954302287545,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.7116339943278245,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.55210066469515,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.1090071925258718,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.209953325476223,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 117.34571502417866,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 241.4788267780062,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 130.4079621765593,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 74.68736496438396,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 161.48924448798908,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 335.770858706357,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 109.10763043936075,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 123.53888880697987,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.4997407742929298,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.106240317455822,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.2643402741626577,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.5707813173968308,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.3004528858589795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1113224276073521,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.30744095123307325,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.3392399569789657,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.635997564383551,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 18.915380980741105,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 4.8499809200332304,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.658108663337988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 77.10987747055962,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 167.7130185596509,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 76.21693475733208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 62.93905869200664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 12.418779385320036,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 29.665791693586783,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 11.862932404131524,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.541249958066794,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.41403227256090763,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1184310175137706,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.303151819781647,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.3221752939689046,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.5041876529707023,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.053512370493831,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.5302329422731138,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.6105599075054542,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 46.70682580656057,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 101.05970492295714,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 51.4555550083396,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 35.68435797726263,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 184.83661156666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 245.90410079333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 153.28418685,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 180.02410662666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 62.64317338974795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 235.4566179482183,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 74.61984717660808,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 47.09126384450729,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 12.67834279589346,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 33.93573771911988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 16.56700386360489,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 9.004470645975466,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 224.64123712666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 172.9232057,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 107.115708924,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 97.00198032333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.2637594521655309,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.487230184171293,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.1795156301961311,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.0907527803859793,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.543412274489427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.7552145447555774,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.49403600102268835,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.4678057031728143,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.0543641235299672,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.90939904512742,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.769741182169995,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6725911131464268,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.018276984749001305,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.0637099919427231,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.02898931929478369,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.01495366257147413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.interpreted_default_unordered__ci",
            "value": 78.02925784857142,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 483.86130268999995,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_default__ci",
            "value": 93.09355065333332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_volcano__ci",
            "value": 73.2363116742857,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.interpreted_default_unordered__ci",
            "value": 38.33182642186813,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 132.3999724026667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_default__ci",
            "value": 21.716624792608695,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_volcano__ci",
            "value": 17.36871136618391,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.9513824941595843,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.935694574082645,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.821488192468168,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.574378444253568,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.0839828280356354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.331072183223131,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 0.9735408384147466,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.9290130133434925,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 68.455683445,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 77.10211732285714,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 45.213563625,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 34.81933328133333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.7477613965743821,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.3986930636687473,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.6937287935810088,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.621693724238789,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.4223560490467233,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.063407557735458,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.0719928837757113,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.0761225708115216,
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
          "id": "ddca94ab03f1a48aca9e0a07392a13bcb06072f1",
          "message": "use enum not string for predicate types",
          "timestamp": "2023-07-23T15:10:22+02:00",
          "tree_id": "7f19351c7aa622d97785c6773a88061dc86be4de",
          "url": "https://github.com/aherlihy/carac/commit/ddca94ab03f1a48aca9e0a07392a13bcb06072f1"
        },
        "date": 1690123978532,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.7062987016363838,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.5377831370416702,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 4.562242180505542,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 4.357338082376827,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.0918909755617596,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.2942368889415349,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.3180420448336797,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.19808797729263491,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.13123594783580458,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 51.870836180000005,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 37.89642331857143,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 40.96088043230769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.8673529223862311,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.726666952401679,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.7069637034563399,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.7673965658379649,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 207.930354712,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 205.84819627400003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 98.41120915,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 144.818858522,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 7.78881472227342,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 20.394389405009214,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 5.461934654306635,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 9.05659223307958,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 47.059282475019934,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 102.22115624823226,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 58.28789210326581,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 33.823068991515626,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.6666354697922676,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.462709794679458,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.2533889839855137,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.1094892995763157,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 109.8446227691808,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 245.86059082725873,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 127.7534732445326,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 70.99099555617583,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 152.5083099702299,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 329.7660835334516,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 108.17809467404689,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 124.16229132506203,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.5677879462247626,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.092752602962061,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.271717910018041,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.237590419203623,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.29949093376610536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.0668792077012879,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.30559404444611815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.34619782593736426,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.596459377684273,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 20.345998199776865,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 4.984566788337571,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 6.142430841365772,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 74.56022335196963,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 157.73408905439365,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 76.78546042927135,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 53.39947319344979,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 12.204987266549647,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 28.54088269340827,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 12.017894805012329,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.521578878354925,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.4163209020487361,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1309347818555922,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.3067999382975554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.3238048921653439,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.495367940508551,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.106260782919544,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.5281816576281821,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.5736720989144735,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 45.90125295103399,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 104.64552881996693,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 53.100202170955356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 35.07332443362732,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 229.62007920666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 253.44838476666663,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 237.75052892,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 126.25158745000002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 62.605821823794905,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 140.83775637591197,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 76.17894146993784,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 44.757971156921116,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 12.299134709797249,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 37.05422939285283,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 15.590697053769924,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 8.976729350797363,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 221.67322533333336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 166.06846401,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 95.12513319,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 97.31727139666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.2827263879735036,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.555518790316944,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.187556973749857,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.067594480642431,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.5188790590044918,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.7959262552645696,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.46821792637666215,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.46651640335194855,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.002134143131783,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.482379852704854,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.807370124943936,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6061145173204738,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.021010856087600206,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.06717805608582877,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.03046358778628801,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.014416886746259416,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.interpreted_default_unordered__ci",
            "value": 76.48236451428572,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 596.6524986500001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_default__ci",
            "value": 91.56793729666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_volcano__ci",
            "value": 57.21642981777777,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.interpreted_default_unordered__ci",
            "value": 22.043539719130433,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 125.55594001766667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_default__ci",
            "value": 21.0059568714,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_volcano__ci",
            "value": 17.985013639285715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.909192622847554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.977786521724097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.8208294102995297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.4385456169615736,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.080242255937326,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.467333474985083,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 0.9089611591893603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.9120631829217345,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 57.70341098444444,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 64.227458065,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 46.0865977090909,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 35.45871995733334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.7333541123116634,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.4782268790893993,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.6642289889462603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.5889263877687869,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.4499085514563057,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.258773632211068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.174341158642415,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.0924399790102641,
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
          "id": "e5ed79341787ca9d2564813516f5852580f687d1",
          "message": "Add more benchmarks for listlib",
          "timestamp": "2023-07-24T22:14:54+02:00",
          "tree_id": "a8705da624b8f37e176387106f45f9eaa7055244",
          "url": "https://github.com/aherlihy/carac/commit/e5ed79341787ca9d2564813516f5852580f687d1"
        },
        "date": 1690235972012,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 2.564834847939991,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.7569467407154713,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 6.406063157967166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 7.3006762906077896,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.037928650562686,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.4308688594925062,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.4598377484871863,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.28729547870221805,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.23087745268257018,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 58.58919135555554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 82.368983,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 43.21530389666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 53.57459420400001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.1640604394796543,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.217338267905289,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.8712778568147812,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.9877237017978825,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 233.25087789999998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 220.83395946000002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 115.85432631200001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 95.291254558,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 9.509689037628764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 27.098244473334198,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 7.8644294133221475,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 11.380015595649464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 51.96597004344311,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 111.36695301209434,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 67.6727036625793,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 40.19789453056764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.462773023443444,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 12.653506138847904,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 3.0430729649877484,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.9165350814485205,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 128.51791927162725,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 269.53534445950515,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 140.8525738766968,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 83.34179242410569,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 168.28258701263186,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 319.01298427792557,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 135.38224540819803,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 139.93902951725383,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.696134295385351,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.589566803600117,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.5849678631492892,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.6147565662750314,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.4767256732451764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.0026870844275626,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.4577302944156231,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.5274239300602211,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 6.395348782891723,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 25.212516466666877,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 6.161084389841427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 7.64096950900975,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 76.32217192657163,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 178.8524650641032,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 82.4102462389989,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 63.66492517990882,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 13.733773957042137,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 32.15827111822586,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 13.860645019745897,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 9.91894264144069,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.487744344825823,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1523021385875007,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.33352013978411726,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.37651463998978996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 2.208183880331611,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.776829795805594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.9824303923452788,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 2.1443566802632312,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 53.998497654284336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 106.18203592459955,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 58.30156129256234,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 42.042307173766844,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 256.71458636,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 185.3358530066667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 175.35436209999997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 176.99813358666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 75.1093227148859,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 180.89494585200467,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 89.03726271496244,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 55.20222176987069,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 15.532576900435808,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 39.9584422344292,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 19.66661662973622,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 11.023035221076626,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 158.889843795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 208.30916508,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 113.249878,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 105.709949852,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.6966099875065623,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.001793738798817,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.499287625792584,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.4595773927775775,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.7186068966477729,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.6072582988002067,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.6167540413781359,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.6538513428977668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.6098305372634614,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 10.149426179561447,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 2.3572171799599855,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 2.179028098710525,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.024476480462989402,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.07522374058452541,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.03776142407095058,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.02286963463884665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.interpreted_default_unordered__ci",
            "value": 94.25824111,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 437.01629883999993,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_default__ci",
            "value": 106.86701464400001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_volcano__ci",
            "value": 80.72555588285715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.interpreted_default_unordered__ci",
            "value": 26.378524599526315,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 171.874938155,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_default__ci",
            "value": 26.663335419526312,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_volcano__ci",
            "value": 30.290218562426467,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.3826639692855385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.6317519780862595,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.3048857341280984,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.8861319755600596,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.4970281546910436,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.9843079511336965,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.3057131053888749,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.2997722622924166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 75.73923770857144,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 71.32057424892857,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 52.544230758000005,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 54.15769186800001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.9685539595864325,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.7493512520037426,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.9040811838927297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.8576268558973356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.9282842277464802,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.106096379019297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.6022007275779917,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.4798912405584423,
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
          "id": "6651a23b2a8839b60da3828099eca16e014185cd",
          "message": "Add slistlib",
          "timestamp": "2023-07-24T23:23:38+02:00",
          "tree_id": "1d188e2dd1efe6b174e1b36347c3bdbee435c17f",
          "url": "https://github.com/aherlihy/carac/commit/6651a23b2a8839b60da3828099eca16e014185cd"
        },
        "date": 1690240227187,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.796278757722576,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.6067641029316141,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 3.700032674489845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 4.304600968303255,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.9886082732849427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.20081811404686048,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.23206640754530414,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.18904863749220566,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.2836021883009022,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 51.577578508,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 36.83766888,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 40.0600844876923,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.8254743721543771,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.7407379081232457,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.7100227559259544,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.8006448581284147,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 203.13277216800003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 202.533660464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 93.618925344,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 150.43788778799998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 7.7368442255676415,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 24.740804175337388,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 6.783104710390316,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 8.814174109531509,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 47.54993634680058,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 100.5114693765821,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 58.166120658002384,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 32.78365839602104,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.6556744778694563,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.82536505051539,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.2775615049087223,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.2588392850357324,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 110.84153309269762,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 258.24362027939526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 141.27759671009443,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 68.26947737914517,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 158.85701393702664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 307.51651914441794,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 107.24439057550674,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 123.25316247328347,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.53745839775282,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.977926550753262,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.258854360995645,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.569002081792946,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.30223071483810837,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.110476742000413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.3254011323303082,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.3402954721373537,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 6.036019822721562,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 19.437222673036732,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 4.662859813466204,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 6.159408781723447,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 74.87375001550491,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 169.89845172575048,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 75.65402052657755,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 54.75605385164099,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 12.143345469206412,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 28.63603543316291,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 11.739907827281666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.859639006935002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.4105432138768766,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1881572458602698,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.2887155595443014,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.32440893770204915,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.5034628051429544,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.088710750613542,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.5686170449244896,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.6460467225762287,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 46.28931108934027,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 105.2938286284206,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 49.35403064229041,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 35.4216583576695,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 176.52576791333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 247.25363263999998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 237.52883812,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 123.287725924,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 120.99470158926985,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 187.32612458071594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 73.9344680130566,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 55.75640056081129,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 12.128004519767423,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 36.56989621941999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 16.369631578357904,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 8.533461945057429,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 158.434543575,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 227.99859708666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 95.98540105666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 151.4560684,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.3104532044283062,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.38349286213715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.1824428936981097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.0563912302627965,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.5449247193322845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.7690049774583905,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.49131944239749786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.47136150315966285,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.0072129462877095,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.756457473175082,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.7756747761667668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6353085288249,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.01730317353294156,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.062280633824002264,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.028989886516439994,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.01532081330013942,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.interpreted_default_unordered__ci",
            "value": 78.28131946285714,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 526.0860483099999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_default__ci",
            "value": 91.52071183333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_volcano__ci",
            "value": 111.32921070000002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.interpreted_default_unordered__ci",
            "value": 36.07018705495237,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 109.95855012066666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_default__ci",
            "value": 21.020936196666668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_volcano__ci",
            "value": 29.115128667908493,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.interpreted_default_unordered__ci",
            "value": 84.99802101333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 484.15750536250005,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_default__ci",
            "value": 100.9347872,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_volcano__ci",
            "value": 59.26329262222222,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.9076286289318927,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.201774203473047,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.7997171383984594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.4707837096144758,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.1157877726462062,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.295905393046404,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 0.9911342240676475,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.8833613926740662,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 60.64279883555556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 64.524930775,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 58.761167571111116,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 51.637258808,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.7413351878909443,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.4918590868240766,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.6952918729805945,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.6037854485790162,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.4233104382521875,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.186164031075672,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.1802928144056104,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.0717558916464325,
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
          "id": "baad42dc9e1127b12ba2f358bfbdb57acd67051b",
          "message": "favor cxn for presort",
          "timestamp": "2023-07-25T17:42:30+02:00",
          "tree_id": "05682aa68a0c93d9f4578d275320fa691a7678f4",
          "url": "https://github.com/aherlihy/carac/commit/baad42dc9e1127b12ba2f358bfbdb57acd67051b"
        },
        "date": 1690312580906,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.6700718276766697,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.6052948170208208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 4.373623107823059,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 4.903482573045453,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1535970442441117,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.38798321307539096,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.2887535685576137,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.15122496596811769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.2617231810661325,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 54.63408038,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 37.90986545,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 25.750273872999998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.0132739961688784,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.806091573361742,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.7227595562458167,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.7426103540454487,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 207.70847634800003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 212.02006594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 102.253231448,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 160.49494002600002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 9.627657585091715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 26.680194711856178,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 6.889581730292273,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 9.09304349586728,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 48.31769112281435,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 107.01376319872425,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 59.81732880532867,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 34.19926207381586,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.71691431680189,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 10.0984627043212,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.290447580971306,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.1774658936668945,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 124.67751313372227,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 426.1992918862438,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 143.0914959314908,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 73.54452442920788,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 164.19077707354205,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 364.8442911678837,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 115.01129660330898,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 125.88184625607286,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.8632732416386812,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.353650552839456,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.47993976024248,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.7641748073264694,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.3233801697647452,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1628418245377403,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.3203995696571666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.33909460282816717,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 6.411633312341931,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 20.05899594469731,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 5.467075363059886,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.959565260535959,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 73.88017199340128,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 168.4670842069419,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 72.78251748367383,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 60.01101222482896,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 11.616485912374415,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 32.286292593159246,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 12.54725780841302,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.465283337160887,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.42776388547522715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2607461155116408,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.29509646066855827,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.3220670736834711,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.493604366143053,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.344177724714786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.5169136423235852,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.6134966935960566,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 86.2207812820255,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 113.9360880932464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 51.961632776363935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 41.53634450849322,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 248.09824942,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 243.16158590666663,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 172.99544496666664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 111.74806834996032,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 255.795032473406,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 78.63753750090015,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 83.72445125857935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 14.197416247351835,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 40.734628146189905,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 17.12767372657508,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 12.888282550570423,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 166.73036030500003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 167.577402835,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 100.722693948,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 148.869710855,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.433806565974367,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.495490317908952,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.2854125813963289,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.0969776191454141,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.6013360430157478,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.9665899143184198,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.5409416975770802,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.47201385488158615,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.091247061538658,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.18533510716397,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.7339372480372124,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6394641370225145,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.023516154732949657,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.07843790419485477,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.03722591305588953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.018337201434426263,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.9228837236092915,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.435197341774601,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.8141380198654449,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.5647244943951368,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.6526592595715066,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.152909461875366,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.090284231505808,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.993049350808282,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 62.40033316583333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 70.45321000464286,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 48.09290641636364,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 52.748437235999994,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.8581982929010195,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.757191409455687,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.7448241075908129,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.6171680674350554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.733497473012045,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.454551664822304,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.2139889965570514,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.0250931765980165,
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
          "id": "c8bf30a8d721d07911490c6bd5c0eca783651c0a",
          "message": "use only select for sort",
          "timestamp": "2023-07-25T18:58:53+02:00",
          "tree_id": "adde4fc9b450936400e001bf1949e031902a582f",
          "url": "https://github.com/aherlihy/carac/commit/c8bf30a8d721d07911490c6bd5c0eca783651c0a"
        },
        "date": 1690317775675,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 2.1389967827269176,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.6087755090198554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 5.451832149781552,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 5.215954409363311,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.034666674935618,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.3166149113068408,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.2229374751642232,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.18830315138433082,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.25612423453766875,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 34.504094945619045,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 40.755023754487176,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 24.886589264809523,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.4956549915422537,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.790051432625859,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 1.0018215876360717,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.9800164819138526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 205.522626322,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 200.539158906,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 113.14647015999999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 153.410120164,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 10.609980265743394,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 30.464354606433375,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 8.089675463809698,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 8.226477009056747,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 47.752185963922074,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 109.6538011097983,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 62.24671229221292,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 36.65822443927569,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.3304383874200703,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 11.527827142529308,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.680551963143285,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.6829507464659303,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 145.7622086879133,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 322.9919360406993,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 136.77980361844868,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 80.18726625949535,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 157.86862346728105,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 324.763313039621,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 112.72061842634227,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 126.40930073287939,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 2.5453676534985767,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.761278487018396,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.6569312991963354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.9906504416560555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.5412630526158738,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.7091213589356744,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.5045433973324003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.5592909306681003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 7.6672432711555825,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 22.56606472843047,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 5.356196829013096,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 7.330851735196343,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 71.97090744405209,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 159.1888828438048,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 73.4554464811288,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 58.69361118839023,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 12.461921520369616,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 31.206548493949988,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 12.306128273888314,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 9.230668940578425,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.43601408612370196,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.9438734933044189,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.34253107823116186,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.3342929206273599,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.7466999640537477,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.456249378547792,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.7578669617636435,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 2.014703656691168,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 81.80890363728982,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 96.17186102201234,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 49.40922430215238,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 37.0847266966461,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 187.07455253333336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 193.95028206666663,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 234.82549325999997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 139.059192445,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 121.07769812035924,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 255.06181699498657,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 81.21583528388682,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 82.66384403813481,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 16.87829973014942,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 47.7187275542546,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 19.18801664035645,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 13.255697103507424,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 223.95521846000003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 145.216109045,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 103.051326428,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 102.124342132,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.8818527289538358,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.398079699572558,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.6035498488544355,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.477115589108581,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.8538427840529346,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.4080298248111105,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.7027969502788075,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.6986528761382214,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.664492057648077,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.70760247814639,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 2.088452031612889,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 2.096042092268259,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.027837152776798636,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.07859307956752244,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.036655888865150074,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.022619002351705175,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.206539973558576,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.933367088193724,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.033909690780747,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.9054218825269253,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 2.1339179035787668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.6526341164962774,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.367023907256047,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.201376254875084,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 65.108112275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 65.04463285,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 49.429320030909096,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 48.45162271454545,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 1.0299024664149463,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.1810105733134577,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.8904499833518502,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.8086555142273972,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.9246238972583793,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.075988574523171,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.4728800898898111,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.3702388170066095,
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
          "id": "f8ac08cc4e5b560fe211f9a0c6e21205871cb519",
          "message": "Update Serializable ListLib test",
          "timestamp": "2023-07-25T20:18:19+02:00",
          "tree_id": "4231ca9cae84b11f37308320736112621f1e30d9",
          "url": "https://github.com/aherlihy/carac/commit/f8ac08cc4e5b560fe211f9a0c6e21205871cb519"
        },
        "date": 1690322502605,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 2.672017980997586,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.6910205181284443,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 6.115199885872093,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 6.353988038931952,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.294558478656936,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.302129064140234,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.28869024536981663,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.23467579437740732,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.19969352095688886,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 40.80502259384615,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 43.70070681166667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 42.13733728474359,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.3911910279464128,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.325948711320153,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.9264849796254608,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 1.0525788631932222,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 221.653514834,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 222.815033386,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 115.21210122400001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 156.98791136,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 10.965307431299735,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 30.685678591653776,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 8.517341110389404,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 11.185000722354483,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 52.96853738835931,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 112.65922634134532,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 79.8557920375923,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 39.71373971279344,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.5722112249788376,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 12.2340360423811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.811918366274823,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 3.015080704064385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 175.75003734836613,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 292.52519052140076,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 158.93231595509536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 88.48412169496538,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 178.72697564631594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 380.7962422479071,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 134.1595882789015,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 141.89740565184857,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 2.280221938195839,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.8511802995863915,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.777969512977902,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.6779880358989332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.5982316828002803,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.9503891149399462,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.484900424503533,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.5417719570510878,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 7.637710184557416,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 23.88018203942321,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 6.919541150151806,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 7.048617190128243,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 80.96228023241514,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 183.79875025374625,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 77.33924068849893,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 63.54308604450297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 12.869889573986958,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 32.364234577181264,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 13.26377813598152,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 10.430393559478526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.4977425118252058,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2748011197496385,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.3149116368826574,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.36363777414181614,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.864959175269155,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.724381685879304,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.8471970509417985,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 2.0433761648325186,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 83.24821973472145,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 113.0159059946789,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 55.23814301888284,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 41.48021268088259,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 246.4381677433333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 253.03994795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 192.79210275333332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 132.13553559500002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 84.93246833056148,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 248.2133665674704,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 89.21987111481748,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 70.21401810546497,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 18.17921409852764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 43.09951787239302,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 19.953058091102793,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 14.8739943965949,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 244.0358392733333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 253.09152866333335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 108.35180327999998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 160.75182867,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.9084325836271567,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.116286644310245,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.5929043794315851,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.3882512950303854,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.753336136004606,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.900533715621766,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.6377532529081572,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.6815013735657389,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.6459104069124644,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.6773494044774,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 2.063214460181069,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 2.138084145550402,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.027524932222478038,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.09896678305931685,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.036977943387868084,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.02095468868966712,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.1435223687519427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.950294358018148,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.004555496877603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.7746420505090206,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.5062647012147774,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.0120763024264035,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.3480276031618592,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.21740007733066,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 58.287396173333335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 98.04850649266666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 46.41006302,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 36.19689676428571,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 1.0593351937256297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.7701062452090355,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.8340177296810646,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.7433327611551797,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.8473691066891234,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.057421177794916,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.4215292770951387,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.3117610667173412,
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
          "id": "ab511888fa3ef917510927b79301bca51397ab1c",
          "message": "negated test and more benchmarks",
          "timestamp": "2023-07-26T00:55:47+02:00",
          "tree_id": "cd38192a122ade55ed443b4cf787da4d3e376f2d",
          "url": "https://github.com/aherlihy/carac/commit/ab511888fa3ef917510927b79301bca51397ab1c"
        },
        "date": 1690332897039,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.840657363325393,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.6168814463447103,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 4.833430538448647,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 5.06120809480724,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.062419784330241,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.29400137020030664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.24440146171641777,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.23592721406379574,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.3217176657403166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 55.657966097111114,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 38.48333306450549,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 40.24567626307692,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.0604485891513917,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.466516607403676,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.8517111933862713,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.7552340836683966,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 203.711353668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 201.18383574,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 98.674833318,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 145.952690958,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 9.117877086113001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 27.391081769416406,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 6.3250524034454845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 9.177622911835337,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 47.64321096656637,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 104.23872933485472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 58.819005212050726,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 33.46892872894538,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.7724201264159682,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.198749764655965,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.2127414637025455,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.1804297122649534,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 129.83635239969306,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 278.0311821239795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 138.69241793938133,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 103.28616933519068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 178.25690822326814,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 331.7874579172008,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 115.57118099195961,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 123.60377649877766,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.6438007880565746,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.617901114026844,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.2414574918031982,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.5986103819295399,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.32437462139543083,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.0953020832257168,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.32383934577687684,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.3436054112452426,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.656974294325792,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 20.359647762399213,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 4.950923835596124,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.749713150315314,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 74.97470517811615,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 163.67016182694607,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 72.63374599863,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 53.25386919555253,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 11.806614780759727,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 29.42183751116523,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 11.593224778582776,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.453614882016485,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.4588462745600088,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.1682515583583384,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.29425731544702505,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.31349838469726354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.4866144654083935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.767472352833229,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.483420748686229,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.593307848517531,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 45.805415445055154,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 110.6993457843145,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 50.07428981697878,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 65.96035184801582,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 242.61201174666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 203.0706049,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 231.73865626000003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 180.85486619333332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 114.0315198570484,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 210.48969013806078,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 81.3743317276998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 78.54188285022042,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 16.393937226170205,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 45.454324443009156,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 18.145494938099024,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 12.416020385413624,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 235.00871648666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 229.39772279333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 95.45124784333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 159.7573751,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.4154541653305739,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.5612107143878635,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.3114079336639253,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.1041692025437695,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.5751282511950605,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.8922527983966908,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.5239919595446172,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.47802214285391953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.0166996446812275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.436868893895396,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.7337517375978535,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6715764428014928,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.02373137264910407,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.0691610373903244,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.03214993859835946,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.017680037875987178,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.interpreted_default_unordered__ci",
            "value": 115.98438119599999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1268.2621312400001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_default__ci",
            "value": 96.61336730333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_volcano__ci",
            "value": 49.05733316727273,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.interpreted_default_unordered__ci",
            "value": 35.84728308190476,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 558.79456637,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_default__ci",
            "value": 24.379411193333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_volcano__ci",
            "value": 30.815189016838236,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.interpreted_default_unordered__ci",
            "value": 123.88066494000002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_default__ci",
            "value": 151.040063885,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_volcano__ci",
            "value": 81.14953464952382,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlibnegated.interpreted_default_unordered__ci",
            "value": 2253.52760336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.9301459006648205,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.001123981515947,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.2208006779752054,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.4395444404556894,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.3897059538206098,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.068142802140839,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.1995799223021528,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.9970013439152657,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 58.61884095555556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 64.56177195500001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 46.88316019636363,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 31.809818333750002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.8521743816942398,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.528012209166012,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.7414833185178165,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.6046343454986469,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.5501329423153325,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.411603397822786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.114947799121262,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.1935233699975618,
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
          "id": "27903820fe427bab09c0f2ce80c938aa25a45e7e",
          "message": "Add tests",
          "timestamp": "2023-07-26T01:10:15+02:00",
          "tree_id": "9b77db941e84f1f9bc1aba6532dbf93f039c9678",
          "url": "https://github.com/aherlihy/carac/commit/27903820fe427bab09c0f2ce80c938aa25a45e7e"
        },
        "date": 1690334018119,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.785502455892842,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.5395952504155853,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 4.235961094592463,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 4.7920147111400615,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.538111774674166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.24931066259755016,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.3116440449303341,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.22190839979301585,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.2149398369436811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 33.719822537333336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 35.641946479333335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 40.11202939076924,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.1438694163906635,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.497057576932247,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.8475933960701688,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.7623445934627716,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 194.70008877200002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 201.65956361399998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 94.917617046,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 140.46472156000002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 9.07097408820335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 25.934058928388527,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 7.369538009382812,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 9.142580107205246,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 44.61385107555692,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 108.00091132501711,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 54.1653316586503,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 30.448599351853225,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.7200328801949527,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 8.942314995904411,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.164187416412393,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.1540841460557374,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 156.95150201541117,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 271.7267102426751,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 133.48243036615045,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 64.3511857619893,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 163.07193968067526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 352.617347802357,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 107.30944115885913,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 120.45252774516685,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.824036444289872,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.074022470425878,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.1955003562077953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.6184184361975902,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.33171991778380466,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.0093210782607138,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.32640617632779334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.3317029577666427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.690258750795224,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 19.328866569240727,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 4.459177183140541,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.263883224701585,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 68.78794529976874,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 153.92698886615898,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 68.00093011038612,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 49.76043516082897,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 10.993378432306375,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 28.140596018625992,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 10.908751943776844,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 7.44481232568468,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.4658572558648449,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.3219478312738107,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.2799266988022411,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.3077815600896498,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.4718409141035882,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.857748053228946,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.406140286407048,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.4348174227370083,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 74.21004694219259,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 194.04236066650998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 43.98954720453886,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 63.51150380454294,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 233.21306531333335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 242.5737665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 174.61286073333332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 96.89540377,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 70.12726397963847,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 270.2489610058054,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 78.18841960464223,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 46.90474711998932,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 16.414093132458387,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 47.01934619687728,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 18.061507060567948,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 12.399423124785123,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 221.69809248666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 171.26247226,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 84.19893134333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 85.70104395333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.412984407914833,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.504303378774029,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.3002671601266438,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.0216779945156487,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.5822325083441223,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.938048602666603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.5116423260881436,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.582133388714611,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.0862383021272946,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.854515419050658,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.709282569416575,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6587144958371554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.02421567694911979,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.0827324194547113,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.03265505379843351,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.017730246400526103,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.interpreted_default_unordered__ci",
            "value": 75.59373628857142,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1504.0354508199998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_default__ci",
            "value": 88.16188287,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_volcano__ci",
            "value": 64.46516385750002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.interpreted_default_unordered__ci",
            "value": 41.47384185794872,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 525.77470645,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_default__ci",
            "value": 21.91131185384058,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_volcano__ci",
            "value": 34.38226958399999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.interpreted_default_unordered__ci",
            "value": 125.638401985,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2097.005915333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_default__ci",
            "value": 138.071071925,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_volcano__ci",
            "value": 81.23873212857143,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlibnegated.interpreted_default_unordered__ci",
            "value": 5167.6228636,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.9185030658005364,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.7555758151313565,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.7638081449425873,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.3904337493916077,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.554626267516959,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.446201167257698,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 0.9449428880261255,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 0.9352388197581984,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 51.191928516000004,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 59.048067655555556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 45.46187098469697,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 30.175121508235293,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.8447761330753935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.444022296073919,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.7150015876167748,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.6041598561453025,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.477189192456837,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.583929205825168,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 0.9888698794353289,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.0581937092216354,
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
          "id": "4fd5bdb5ca970b2d2255e5f21bb70a30a9abc80e",
          "message": "Add worsts",
          "timestamp": "2023-07-26T15:33:38+02:00",
          "tree_id": "469a3241623c24087b9b916f15a0bfb10b4474d5",
          "url": "https://github.com/aherlihy/carac/commit/4fd5bdb5ca970b2d2255e5f21bb70a30a9abc80e"
        },
        "date": 1690385726657,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 1.7944068951413559,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.5918667108381164,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY__",
            "value": 4.108790573949914,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULEBODY_aot__",
            "value": 4.438541793466171,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2486924436651097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.29110307183405937,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.25408490111429194,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.28190222399597886,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.17029375120544715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 52.802628303999995,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 37.21766153142857,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 20.887441928333338,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.798092351272809,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 3.588854607626698,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.753805521549465,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.7579943472698047,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 206.09695421,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 206.30371100000002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 101.624479664,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 146.42035031,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 8.2049654441519,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 26.94106260861461,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 6.108060640854655,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 9.04325146786776,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 48.67175454024418,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 113.95631653878736,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 58.96160297626324,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 34.03396538419535,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.566489999924795,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 9.757433847371718,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 2.250482958084425,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.199523650761055,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 128.42141134159846,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 269.2626066354627,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 138.20438637588603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 103.40033882759053,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 162.39414964435537,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 354.5217115589127,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 110.21929850359341,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 121.21590618031121,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.3882267257509522,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.049864380371275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.2538917170191033,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.3211752368744347,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.307602681475526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.0705849814271609,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.3403472784519253,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.33856933371895603,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.996446956614871,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 19.411361256324604,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 5.024917775644526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 5.423002447322787,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 73.05302008552094,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 159.5350342103102,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 73.09197035052195,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 54.28463120915618,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 12.116496867748907,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 32.12427180255317,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 11.724166999090366,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 8.636982244424418,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.42201376090357556,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.2150374994086168,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.31516512460551377,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.3170815456935585,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.4769905028662573,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 5.2821769253649675,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.5317715028258376,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 1.553226714019051,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 58.169816304531935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 101.1491197484681,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 55.404244653028535,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 69.70391649462181,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 234.9122986866667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 250.76148401,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 245.9438859866667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 112.1170971,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 112.29952391903937,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 271.35904654389907,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 80.12778225308327,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 79.92645317298965,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 16.17875109258275,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 47.8364246032356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 17.31873799334118,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 13.219078315399125,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 227.39029952,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 243.01719073333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 96.42338767666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 159.23485694999997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.3947888818535097,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.7165117512222166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.338288598812691,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.1599278816162308,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.5589556582143926,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1.9214548777137206,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.5209025986076272,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.47103801639642706,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 1.9741976717357943,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 7.445433831132485,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 1.7444797295237198,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 1.6130076209546655,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.01989838715620925,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 0.06303077472492756,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.03140717589945968,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.019047169884644593,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.interpreted_default_unordered__ci",
            "value": 150.25747297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 1185.4331784400001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_default__ci",
            "value": 93.26235286666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastylistlib.seminaive_volcano__ci",
            "value": 55.217166410000004,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.interpreted_default_unordered__ci",
            "value": 36.28869600647619,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 469.45948868999994,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_default__ci",
            "value": 22.592863228221344,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastypointstofun.seminaive_volcano__ci",
            "value": 31.322177301176474,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.interpreted_default_unordered__ci",
            "value": 118.85950124800002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2253.1287712000003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_default__ci",
            "value": 149.311583585,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlib.seminaive_volcano__ci",
            "value": 84.37286655999999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tastyslistlibnegated.interpreted_default_unordered__ci",
            "value": 2825.1289307599995,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.8538183875790062,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 6.385409387618845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 1.826497409778261,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.4997297342140339,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.2317010826929171,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.945948944415701,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.0407739851740447,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.0813016899321088,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 68.22779456250001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 67.3132950225,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 46.28809570181818,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 51.405954074,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.7933948374776442,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 2.5342310323524573,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.7287038175601831,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.6296599991007522,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.5398077731779432,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_unordered_blocking_EVALRULEBODY__ci",
            "value": 4.710795355737076,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.1025869080611927,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.04120854746975,
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
          "id": "b5c5e36169d4b95184a0b93020a50685be881e12",
          "message": "Clean up old benchmarks",
          "timestamp": "2023-08-01T18:06:56+02:00",
          "tree_id": "af747c874460bc66821101a2ce8537a6e767e60b",
          "url": "https://github.com/aherlihy/carac/commit/b5c5e36169d4b95184a0b93020a50685be881e12"
        },
        "date": 1690911786154,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 3.048928057936584,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.7896865880048203,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULESN__",
            "value": 1.4923255830734812,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULESN__ci",
            "value": 6.592414214269065,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.3945799587986124,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.41640424826077405,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.27379972261708735,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.25107811192433466,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 63.23205231555555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 42.52371644186813,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 41.67424478205128,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 50.24252385436363,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 1.1726955848735194,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 454.0491224182526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.9785416472155953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 1.131636950409408,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 177.170845036,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 182.092302938,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 132.80569454800002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 187.460394554,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 10.951954474102392,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 2208.6695541338668,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 9.093885018432072,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 12.202974100783667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 59.884933681651816,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 583.1989514286505,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 75.43329394172197,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 45.33960424827178,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.4816654010541526,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1232.5997829983448,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 3.2201201870904983,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 3.5031718912452297,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 144.05340335175532,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 5099.401070533334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 151.4782819265783,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 99.8991582009644,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 195.03598423881104,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 2785.780141753446,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 140.38523679237318,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 155.55023244500777,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.754868455228819,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 416.6953511963799,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.772208034409579,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.7000374490455763,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.5413996543552129,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 120.5407062847614,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.5305158151460649,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.568363153456856,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 7.425779351151464,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 2666.6036543813675,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 6.701946945525281,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 7.918353943947127,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 92.19504607372042,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 834.1605205438118,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 86.71959261791963,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 69.03371464510772,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 14.585542264244655,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 538.5946069923691,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 15.121393196494362,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 11.490911352821916,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.6094652715318893,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 54.958109488151514,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.36663605361386553,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.4098145895016483,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 2.0522658847324164,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 372.6406576790713,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 2.177187090254619,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 2.4436110459181295,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 57.72276393883128,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 729.1995136561272,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 63.39335181872218,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 47.7905363662098,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 223.88811841333336,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 220.01180559333335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 203.31932338666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 137.87247945500002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 76.79170019214764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 4229.829524044455,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 92.48326266477355,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 59.59498766433344,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 14.407562639723418,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1667.7933185332695,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 19.29733970236642,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 15.987221559530365,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 200.05133414666665,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 193.59945751333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 124.365401335,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 198.08497786000004,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.9048172375732868,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 393.17666318113055,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.778974067472261,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.6483368622988195,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.849025972297308,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 139.59417655887935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.7402571847017961,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.7618084714795371,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.7359697514483208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 758.7819891858715,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 2.34910692328759,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 2.4934510943151333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.029616967412506544,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 6.474399936020497,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.038198284056373766,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.021648882692727085,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.573078248206577,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 531.4159083691708,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.498521288540811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 2.2071453584469496,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.73556638917113,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 544.1998416506731,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.454368878352742,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.4584514466592764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 73.30638800857142,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 107.196776132,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 56.334723437555546,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 41.33283908948718,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 1.1436965391407312,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 249.90633281844617,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 1.024192291290588,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.9505069830969886,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.934280489855477,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 537.9306093537222,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.7370909575585192,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.6486872163734385,
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
          "id": "0800c33b3313ec83aab86415a234a68f4ba9d15a",
          "message": "Cache joinindexes for rules < 8",
          "timestamp": "2023-08-02T17:37:45+02:00",
          "tree_id": "98fd2463697e0d27ac0038cfd615b6ccccbc869e",
          "url": "https://github.com/aherlihy/carac/commit/0800c33b3313ec83aab86415a234a68f4ba9d15a"
        },
        "date": 1690996939338,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 6.282021817288222,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.8094327122799706,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_async_EVALRULESN__",
            "value": 1.0832723079499467,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.jit_default_unordered_blocking_EVALRULESN__ci",
            "value": 12.02129197612918,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_default__",
            "value": 0.4385221924161873,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.naive_volcano__",
            "value": 0.25644037251057866,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_default__ci",
            "value": 0.2752681023654686,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.seminaive_volcano__ci",
            "value": 0.22777798306906996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 49.62003200145455,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 161.00834987333332,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_default__ci",
            "value": 50.632659782000005,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.seminaive_volcano__ci",
            "value": 33.113680982,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.976518199826157,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1589.5100289958536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_default__ci",
            "value": 0.8458439920364891,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.seminaive_volcano__ci",
            "value": 0.9615631265936541,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 289.23529110199996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 294.65849602800006,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_default__ci",
            "value": 118.66298795200001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.seminaive_volcano__ci",
            "value": 196.761545406,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 6.907452409792265,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 6630.902945876985,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_default__ci",
            "value": 7.4516066792472895,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.seminaive_volcano__ci",
            "value": 13.001721143446423,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 69.22717831243996,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1111.6563835436373,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_default__ci",
            "value": 82.6382054060231,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.seminaive_volcano__ci",
            "value": 50.440894691428284,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 3.343425784908388,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 2387.573037037879,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_default__ci",
            "value": 3.041231657126341,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.seminaive_volcano__ci",
            "value": 2.865458829266259,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 156.38529636569132,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 11749.5205013,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_default__ci",
            "value": 162.41963915413356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.seminaive_volcano__ci",
            "value": 96.91640829826218,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 175.50306223665797,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 8789.419877600001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_default__ci",
            "value": 145.61476984967467,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.seminaive_volcano__ci",
            "value": 85.4113906557827,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.6320975802129307,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1144.485486336131,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_default__ci",
            "value": 1.538817192405412,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.seminaive_volcano__ci",
            "value": 1.5955451418037234,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.379069668116606,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 239.09103899771904,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_default__ci",
            "value": 0.42325877092285225,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.seminaive_volcano__ci",
            "value": 0.4744299066086648,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 6.517379460106736,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 4418.5966775501165,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_default__ci",
            "value": 6.620929599659776,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.seminaive_volcano__ci",
            "value": 7.7984069890868195,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 103.77644431853233,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1466.4786230194445,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_default__ci",
            "value": 101.38338631785305,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.seminaive_volcano__ci",
            "value": 81.99123901326223,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 16.875484953921536,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1089.3368038792619,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_default__ci",
            "value": 16.868049012891085,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.seminaive_volcano__ci",
            "value": 10.8407453788062,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 0.422100578397418,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 133.55674738300002,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_default__ci",
            "value": 0.4211822813454068,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.seminaive_volcano__ci",
            "value": 0.4365947227853887,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.980176591647648,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 768.7127240011557,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_default__ci",
            "value": 1.9305990030489366,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.seminaive_volcano__ci",
            "value": 2.1138056358373842,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.old_listlib.interpreted_default_unordered__ci",
            "value": 98.35759268000001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.old_listlib.seminaive_default__ci",
            "value": 126.58639633499999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.old_listlib.seminaive_volcano__ci",
            "value": 84.52494773000001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 65.6714170852895,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1651.0949335059354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_default__ci",
            "value": 67.65436078740086,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.seminaive_volcano__ci",
            "value": 46.98978745937118,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 324.78926138,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 393.86252356,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_default__ci",
            "value": 224.12068904666666,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.seminaive_volcano__ci",
            "value": 129.914253175,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 88.19684198683528,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 10076.1036174,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_default__ci",
            "value": 100.48791366909788,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.seminaive_volcano__ci",
            "value": 58.43339828622542,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 14.624011575120415,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 6473.315978130953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_default__ci",
            "value": 19.179033825266746,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.seminaive_volcano__ci",
            "value": 11.16417069717823,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 223.25845980666662,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 215.1905133333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_default__ci",
            "value": 137.81521421999997,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.seminaive_volcano__ci",
            "value": 212.19573287333333,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.767414004292529,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 968.7646600268254,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_default__ci",
            "value": 1.6505318676968623,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.seminaive_volcano__ci",
            "value": 1.4232553118892195,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.7041685560208515,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 299.43683696902247,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_default__ci",
            "value": 0.6426745862278838,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.seminaive_volcano__ci",
            "value": 0.6589149076408243,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 2.5236158848094132,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1466.3073531089908,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_default__ci",
            "value": 2.3629870445204326,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.seminaive_volcano__ci",
            "value": 2.2690512572262067,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 0.024409531731232552,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 12.843558177503917,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_default__ci",
            "value": 0.0392372942556806,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.seminaive_volcano__ci",
            "value": 0.018749695655971517,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 2.5711774610848646,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1080.4184699254072,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_default__ci",
            "value": 2.4461826983533324,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.seminaive_volcano__ci",
            "value": 1.9218189341581227,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 1.446362531382701,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1433.2320220835832,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_default__ci",
            "value": 1.2102099951880192,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.seminaive_volcano__ci",
            "value": 1.1721202250530756,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 77.6445123,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 121.52121105199998,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_default__ci",
            "value": 59.60401357111111,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.seminaive_volcano__ci",
            "value": 41.6003780548718,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.9938784617358823,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 492.5526514712786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_default__ci",
            "value": 0.9257676448982346,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.seminaive_volcano__ci",
            "value": 0.7870887579584541,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.8254892086895815,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_sel_blocking_EVALRULESN__ci",
            "value": 1213.3620860277438,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_default__ci",
            "value": 1.4244177226188488,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.seminaive_volcano__ci",
            "value": 1.3857959842417635,
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
            "name": "anna herlihy",
            "username": "aherlihy"
          },
          "distinct": true,
          "id": "cf009823f6c038d75bf2ca6e84c6036859293c9e",
          "message": "Add new bytecode emitters for JoinIndexes",
          "timestamp": "2023-08-04T20:09:16+02:00",
          "tree_id": "d19de5ad107b19c372667e9486e405321bd18917",
          "url": "https://github.com/aherlihy/carac/commit/cf009823f6c038d75bf2ca6e84c6036859293c9e"
        },
        "date": 1691181338740,
        "tool": "jmh",
        "benches": [
          {
            "name": "datalog.benchmarks.Bench_ci.compiled_default_unordered__",
            "value": 4.933913121574205,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.Bench_ci.interpreted_default_unordered__ci",
            "value": 0.5356642616262413,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.interpreted_default_unordered__ci",
            "value": 33.05516882375,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 6.132338590798699,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 4.998525386694842,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 5.855808276501208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 5.800554719413724,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.shallowSeminaive_default__ci",
            "value": 32.22304574125,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ackermann.shallowSeminaive_volcano__ci",
            "value": 25.494930898,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.interpreted_default_unordered__ci",
            "value": 0.6537326752331947,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 2.430466198933,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 1.9244005334844565,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 2.6443537555890737,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 2.0554382508973625,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.shallowSeminaive_default__ci",
            "value": 0.5974197914401898,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.andersen.shallowSeminaive_volcano__ci",
            "value": 0.7013150982248766,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.interpreted_default_unordered__ci",
            "value": 91462.799892,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 95589.73935,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 97263.94247800001,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 92432.432158,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 89730.997978,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.shallowSeminaive_default__ci",
            "value": 86229.431984,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.anonvar.shallowSeminaive_volcano__ci",
            "value": 82355.579444,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.interpreted_default_unordered__ci",
            "value": 6.394713623876408,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 20.204009842915344,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 19.595341081785644,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 19.10008033463991,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 19.043455197015426,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.shallowSeminaive_default__ci",
            "value": 6.256923624838717,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cbaexprvalue.shallowSeminaive_volcano__ci",
            "value": 6.872306051874816,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.interpreted_default_unordered__ci",
            "value": 35.28171998205299,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 69.95405786776166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 65.84873010889913,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 74.77541601202446,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 71.75652728954012,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.shallowSeminaive_default__ci",
            "value": 43.76359828753065,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.clique.shallowSeminaive_volcano__ci",
            "value": 32.49565748153007,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.interpreted_default_unordered__ci",
            "value": 2.1481051408821212,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 6.0406112506452745,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 6.091308559109435,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 6.733914623567354,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 5.8297648454452995,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.shallowSeminaive_default__ci",
            "value": 1.824058714826446,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.cliquer.shallowSeminaive_volcano__ci",
            "value": 2.069154819527835,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.interpreted_default_unordered__ci",
            "value": 92.37933529002217,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 40.8308251483528,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 39.89198808309303,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 46.68273537569646,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 42.478013485822046,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.shallowSeminaive_default__ci",
            "value": 93.98143705589555,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.equal.shallowSeminaive_volcano__ci",
            "value": 73.11186921174445,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.interpreted_default_unordered__ci",
            "value": 60.8569546298202,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 349.3260194345209,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 388.2985339338753,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 369.8572423163134,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 369.5221521412101,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.shallowSeminaive_default__ci",
            "value": 70.59618335922337,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.fib.shallowSeminaive_volcano__ci",
            "value": 57.04420914355442,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.interpreted_default_unordered__ci",
            "value": 1.3606120257416436,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 3.1814856833932836,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 3.5824689424682523,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 3.793440635737114,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 3.3560122027166828,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.shallowSeminaive_default__ci",
            "value": 1.1058658727602109,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.func.shallowSeminaive_volcano__ci",
            "value": 1.1537708369830342,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.interpreted_default_unordered__ci",
            "value": 0.3861436361160363,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 0.9185054563882206,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 0.8682686015550585,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 0.8572895567406796,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 0.8135658810787947,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.shallowSeminaive_default__ci",
            "value": 0.33252299952655595,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.grad.shallowSeminaive_volcano__ci",
            "value": 0.34814675472229345,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.interpreted_default_unordered__ci",
            "value": 5.201833841831366,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 15.382975724340957,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 15.112913233208383,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 15.147420932002095,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 15.458837204185764,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.shallowSeminaive_default__ci",
            "value": 5.189084498796055,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.inputoutput.shallowSeminaive_volcano__ci",
            "value": 5.8708600486223785,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.interpreted_default_unordered__ci",
            "value": 55.17163676355208,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 116.13964923509911,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 101.75881736724224,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 109.66250120707613,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 101.2423694345157,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.shallowSeminaive_default__ci",
            "value": 54.29022083176462,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isafter.shallowSeminaive_volcano__ci",
            "value": 63.282717139896455,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.interpreted_default_unordered__ci",
            "value": 8.668106526401,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 19.058348490925255,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 19.207176772425402,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 18.86047628720629,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 17.92214920982522,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.shallowSeminaive_default__ci",
            "value": 8.627618065911374,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.isbefore.shallowSeminaive_volcano__ci",
            "value": 8.039002162616162,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.interpreted_default_unordered__ci",
            "value": 20.886981895636517,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 142.90034886558058,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 90.53367490837864,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 91.39484684863992,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 95.7581884128149,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.shallowSeminaive_default__ci",
            "value": 20.092781244120754,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.javapointsto.shallowSeminaive_volcano__ci",
            "value": 20.03333084856642,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.interpreted_default_unordered__ci",
            "value": 1.4974655593641466,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 3.536162597859655,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 3.4594863951600168,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 3.4012418793472863,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 3.5428151717262737,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.shallowSeminaive_default__ci",
            "value": 1.5159283224380953,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.metro.shallowSeminaive_volcano__ci",
            "value": 1.639134179697182,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.interpreted_default_unordered__ci",
            "value": 43.24274028393212,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 91.49532838014238,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 83.40215832121066,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 74.37044856903644,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 69.93500722130128,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.shallowSeminaive_default__ci",
            "value": 40.90632255786367,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.palindrome.shallowSeminaive_volcano__ci",
            "value": 34.767886596699576,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.interpreted_default_unordered__ci",
            "value": 17296.139472,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.shallowSeminaive_default__ci",
            "value": 10653.5371268,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.pointsto.shallowSeminaive_volcano__ci",
            "value": 10241.358488,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.interpreted_default_unordered__ci",
            "value": 45.31602305137911,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 29.529637615782804,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 30.507482311464003,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 33.41719639796845,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 31.55857243057245,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.shallowSeminaive_default__ci",
            "value": 48.04052536971633,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.prime.shallowSeminaive_volcano__ci",
            "value": 41.78952541804743,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.interpreted_default_unordered__ci",
            "value": 8.582655459318286,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 25.678292540623193,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 25.717816309426496,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 24.88729073113594,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 24.350194824513544,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.shallowSeminaive_default__ci",
            "value": 13.057131216406919,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.puzzle.shallowSeminaive_volcano__ci",
            "value": 8.27671517074493,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.interpreted_default_unordered__ci",
            "value": 9458.254864333334,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 10392.143078,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 9897.5685242,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 10451.993011999999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 10017.607201199999,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.shallowSeminaive_default__ci",
            "value": 8580.789928,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ranpo.shallowSeminaive_volcano__ci",
            "value": 8300.959079714285,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.interpreted_default_unordered__ci",
            "value": 1.0728731830044422,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 2.8738443934040747,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 3.3306163112345786,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 3.1071829327199714,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 2.846185191650118,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.shallowSeminaive_default__ci",
            "value": 1.0170192577099235,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.rsg.shallowSeminaive_volcano__ci",
            "value": 1.0336318512281184,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.interpreted_default_unordered__ci",
            "value": 0.6057031352687254,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 1.6217413751641918,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 1.4860170537559392,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 1.3383794860929323,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 1.2346923828857548,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.shallowSeminaive_default__ci",
            "value": 0.48727696783086294,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.ship.shallowSeminaive_volcano__ci",
            "value": 0.4919229500406329,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.interpreted_default_unordered__ci",
            "value": 1.647418572497811,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 4.4547042402007495,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 4.316043804301185,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 5.321041012127255,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 5.002039961884977,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.shallowSeminaive_default__ci",
            "value": 1.4778946050283541,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.small.shallowSeminaive_volcano__ci",
            "value": 1.605052471808237,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.interpreted_default_unordered__ci",
            "value": 1.448920680005407,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 4.423949451087276,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 4.587516275273623,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 4.365652610942989,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 3.93795476003595,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.shallowSeminaive_default__ci",
            "value": 2.472608900595944,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.strata.shallowSeminaive_volcano__ci",
            "value": 1.371359325040896,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.interpreted_default_unordered__ci",
            "value": 1.478374029530166,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 3.403010972938101,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 3.769921793771689,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 3.6263604154582922,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 4.18469170687427,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.shallowSeminaive_default__ci",
            "value": 1.4478586397310955,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tc.shallowSeminaive_volcano__ci",
            "value": 1.4196295448881453,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.interpreted_default_unordered__ci",
            "value": 0.9558501127367822,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 2.871989723426927,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 2.7555029952321304,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 2.884138859540273,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 2.9091950289437496,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.shallowSeminaive_default__ci",
            "value": 0.7950732380000723,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.traffic.shallowSeminaive_volcano__ci",
            "value": 0.8819290485157911,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.interpreted_default_unordered__ci",
            "value": 3084.8399802352938,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 4185.447305666667,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 4089.994136230769,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 3934.865811142857,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 4826.296000836363,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.shallowSeminaive_default__ci",
            "value": 3947.3180827692304,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trains.shallowSeminaive_volcano__ci",
            "value": 4076.7816661538463,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.interpreted_default_unordered__ci",
            "value": 0.6907334019033196,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 2.11052667042276,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 2.112889501605209,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 2.1026195118230735,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 2.0466512621706703,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.shallowSeminaive_default__ci",
            "value": 0.7654377998478459,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.trans.shallowSeminaive_volcano__ci",
            "value": 0.5699926350033746,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.interpreted_default_unordered__ci",
            "value": 1.0945773286254983,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_sel_async_EVALRULEBODY_quotes__ci",
            "value": 3.3314025073969744,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_sel_async_EVALRULESN_quotes__ci",
            "value": 3.154528705222525,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_sel_blocking_EVALRULEBODY_quotes__ci",
            "value": 3.403275835268775,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.jit_default_sel_blocking_EVALRULESN_quotes__ci",
            "value": 3.0484142068953926,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.shallowSeminaive_default__ci",
            "value": 0.8921371981361539,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          },
          {
            "name": "datalog.benchmarks.examples.tree.shallowSeminaive_volcano__ci",
            "value": 1.0462712691559788,
            "unit": "s/op",
            "extra": "iterations: 5\nforks: 1\nthreads: 1"
          }
        ]
      }
    ]
  }
}