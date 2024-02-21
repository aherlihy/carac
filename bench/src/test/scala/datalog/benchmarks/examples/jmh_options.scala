package datalog.benchmarks.examples

inline val examples_warmup_iterations = 3
inline val examples_iterations = 5
inline val examples_warmup_time = 10
inline val examples_time = 10
inline val examples_batchsize = 10000
inline val examples_xl_batchsize = 100
inline val examples_fork = 1
inline val examples_xl_time = 120
inline val examples_xxl_time = 360

/** The default GC (G1GC) is optimized for latency over throughput which only
 *  makes sense for interactive applications.
 */
inline val examples_gc = "-XX:+UseParallelGC"
