# CARAC: Adative Metaprogramming in Scala Reproducibility

# Experimental Setup Section 9.2 (Online Optimization Speedup)
Experiments are run on Intel(R) Xeon(R) Gold 5118 CPU @ 2.30GHz (2 x 12-core) with 395GB RAM, 
on
    Ubuntu 22.04 LTS with Linux kernel 5.15.0-27-generic (46)
    Ubuntu 18.04.5 LTS with Linux kernel 4.15.0-151-generic (44)

and Scala 3.3.1-RC4. The JVM used is OpenJDK 64-Bit Server VM (build 17.0.2+8-86, mixed mode, sharing)
and OpenJDK Runtime Environment (build 17.0.2+8-86) and SBT 1.8.2. 

Each experiment is run with the Java Benchmarking Harness 
(JMH) version 1.32 with 10 iterations, 10 warm-up iterations, 10 calls per iteration ("batch size"), and a minimum 
execution time of 10 seconds per benchmark.

# Build Carac
```shell
# Always clean + recompile both source and benchmarking code between runs, otherwise JMH can cause SBT to exit if something is out of sync 
$ sbt -java-home <path to jdk-17.0.2> "clean;compile;bench/Jmh/clean;bench/Jmh/compile"
```
# Run Benchmarks
```shell
# Run benchmarks, on a dedicated server without other processes running at the same time:
$ sbt -java-home <path to jdk-17.0.2> "bench/Jmh/run  XX.*(interpreted.*(unordered|sel)|jit.*sel) -wbs 10 -bs 10 -r 10 -w 10 -i 10 -wi 10"
# As the entire benchmarking run can take more than 24 hours, I recommend using nohup and writing the results to a file, as well as any output of Jmh
$ nohup sbt -java-home <path to jdk-17.0.2 "bench/Jmh/run  XX.*(interpreted.*(unordered|sel)|jit.*sel) -wbs 10 -bs 10 -r 10 -w 10 -i 10 -wi 10 -rff benchmark_out.csv" &> bench.out &
```
I do not specifically set JVM options, but you can verify that they should align with these by running `ps aux | grep sbt` and comparing with:
```shell
java -Dfile.encoding=UTF-8 -Xms1024m -Xmx1024m -Xss4M -XX:ReservedCodeCacheSize=128m -Dsbt.script=<sbt> -Dscala.ext.dirs=<java9-rt-ext-oracle_corporation_17_0_2> -jar <sbt-launch-1.9.0.jar> bench/Jmh/run  XX.*(interpreted.*(unordered|sel)|jit.*sel) -wbs 10 -bs 10 -r 10 -w 10 -i 10 -wi 10 -rff benchmark_out.csv
```

# Post-processing benchmark result data
This section describes how to generate the charts used in the paper.
The results should be written to a file in `bench/benchmark_out.csv` and the output of the benchmarking run to `bench.out`. 
Search `bench.out` for `"Exception"`, there should not be any, but if there were problems running the benchmarks then 
it will show up there.

## Cleaning the benchmark file
```shell
cat <benchmark_out>.csv | sed -e 's/"datalog\.benchmarks\.examples\.\([^\.]*\)\./\1_/' | sed -e 's/_EOL\([^"]*\)"*/,/' | sed -e "1s/Benchmark/Benchmark_inputprogram_mode_storage_sort_onlineSort_fuzzy_compileSync_granularity_backend,/" | sed -e "1s/\"//g" > <benchmark_out>_clean.csv
```

## Importing into excel
The excel file used to generate results is "Carac-Experiments.xlsx", import the data and the charts should auto-generate.
Don't be worried if it freezes when you paste the data in, at least on my machine it can take a few minutes for the charts to populate.
Steps:
1) Open Carac-Experiments.xlsx with Excel Version 16.76 (23081101)
2) Add an empty sheet to import the data into (right-click the tab at the bottom that says "aug-13-all-fpj FIGURES" and click "insert sheet").
3) In that new sheet, nagivate to "Data > Get Data (Power Query) > From Text (Legacy) > select the <benchmark_out>_clean.csv 
file produced by the section above > "Get Data"
4) In the text import wizard click "Delimited" > next > select "Comma" *and* Other and put an underscore "_" in the box. 
5) Do not treat consecutive delimiters as one. > Finish and put the data in the existing sheet.
6) Select and copy the data, it should have columns A-Q and 1-256.
7) Navigate to the sheet "aug-13-all-fpj FIGURES", select box A2 and paste. The charts should auto-populate with the new 
data, if not you may need to right click "refresh". 

Due to the JIT there is some variability in the calculated speedups 
but the numbers should be within the same order of magnitude and consistent, relative to each other, with the ones in the paper.
If any cells are highlighted in red or says "INVALID", it indicates that the difference between the two execution times 
being compared were smaller than the error margins for that run, so they can be treated as essentially having the same runtime 
if the ratio is close to 1. However, if the machine was running on battery power or there were other processes running at the 
same time then there could be larger error scores that interfere with the measurements. There shouldn't be more than a few 
red boxes.

# Experimental Setup Section 9.3 (Comparison with State-of-the-Art)
Experiments are run on Intel(R) Xeon(R) Gold 5118 CPU @ 2.30GHz (2 x 12-core) with 395GB RAM,
on Ubuntu 22.04 LTS with Linux kernel 5.15.0-27-generic (46) and Scala 3.3.1-RC4. The JVM used is Java HotSpot(TM) 
64-Bit Server VM Oracle GraalVM 17.0.8+9.1 (build 17.0.8+9-LTS-jvmci-23.0-b14, mixed mode, sharing) and SBT 1.8.2. 

**Note that this is a different JVM, and different operating system than the last section, due to needing to build a 
Graal Native Image and Souffle's requirement of at least C++17 and CMake 3.15**.

Each experiment is run with the Java Benchmarking Harness
(JMH) version 1.32 with 10 iterations, 10 warm-up iterations, 10 calls per iteration ("batch size"), and a minimum
execution time of 10 seconds per benchmark.

# Build Carac
```shell
# Always clean + recompile both source and benchmarking code between runs, otherwise JMH can cause SBT to exit if something is out of sync 
$ sbt -java-home <path to graalvm-jdk-17.0.8+9.1> "clean;compile;bench/Jmh/clean;bench/Jmh/compile"
```
You will need to build both JAR and Graal Native Image. Install Graal + Native Image: https://www.graalvm.org/22.0/reference-manual/native-image/#install-native-image
```shell
$ export PATH=$PATH:<path to graalvm-jdk-17.0.8+9.1/bin/> && sbt -java-home <path to graalvm-jdk-17.0.8+9.1>
sbt shell:
>> bench/Jmh/clean;bench/Jmh/compile # compile
>> show root/pack # build jAR
>> nativeImage # build native image
```

# Build Souffle
Use version 2.4 retrieved from GitHub https://github.com/souffle-lang/souffle/releases/tag/2.4.
Instructions for building are here: https://souffle-lang.github.io/build.
You will need to put the path to where the souffle executable is in the bash scripts located in 
`bench/src/test/scala/datalog/benchmarks/run-cli/*.sh`
You will also need to create the output directories for souffle and carac if they do not already exist in 
`bench/carac-out|carac-scala-out|souffle-out` and within each of those directories, a directory with the name of the 
experiments being run.

# Run Benchmarks
```shell
# Run benchmarks, on a dedicated server without other processes running at the same time:
$ bench/Jmh/run BenchCLI.* -i 10 -wi 10 -r 10 -w 10 -rff benchmark_out.csv
```

# Post-processing benchmark result data
## Cleaning the benchmark file
Clean + download the results file the same way as the previous section, making sure there are no errors in the bench.out file.

## Importing into excel
Steps:
1) Open Carac-Experiments.xlsx with Excel Version 16.76 (23081101)
2) Add an empty sheet to import the data into (right-click the tab at the bottom that says "sota-compare" and click "insert sheet").
3) In that new sheet, import the data in the same way as the previous section. 
4) Select and copy the data
5) Navigate to the sheet "sota-compare", select box A2 and paste. The charts should auto-populate with the new
   data, if not you may need to right click "refresh".

If there are any questions or problems running the benchmark you can always contact me at herlihyap at gmail and I will do my best to clarify.