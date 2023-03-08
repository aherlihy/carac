# CARAC: Datalog in Scala

Carac is split into 3 pluggable layers. Users instantiate programs by composing a Program, an ExecutionEngine, and a StorageManager.
For example:
```scala
val p = new Program(
  new StagedExecutionEngine(
    new CollectionsStorageManager(...)))
```

## DSL
This is the user-facing DSL package. Datalog program environments are constructed with the `Program` class.
Relations can be constructed with `program.relation[type]` which constructs a new relation, and `program.namedRelation` which accesses previously defined relations.

## Execution Engine
This is the layer that determines the algorithmic execution for Datalog. The `SemiNaiveExecutionEngine` and `NaiveExecutionEngine` are shallow embeddings (no AST) and are there mostly as a proof of concept or comparison, the actual development is in `StagedExecutionEngine`, which is a deep embedding. 
The `StagedExecutionEngine` will pass the Datalog program through a series of representations, first in the `ast` package. This is a typical Datalog AST where static optimization or rewrite passes can be applied via the classes in `execution/ast/transform`.

After the AST is constructed, the `ir` package will construct an imperative representation of the Datalog program. `ir/IRTreeGenerator` visits the nodes of the AST and does a partial evaluation using the Semi-Naive or Naive evaluation algorithm, producing a `ir/IROp` tree (also known as a Futamura projection). 

Once the `IRTree` is generated, we can then either interpret it via the `run` methods inside each `IROp` node. Alternatively, we can compile it by applying the `StagedCompiler.getCompiled` on the root of the node, which will generate a single method for the entire Datalog program (unless the program is huge, in which case lambdas will be generated) to evade the JVM method size limit.
The third option is to JIT, which is using a combination of `StagedCompiler.getCompiledX` and the `run_continuation` methods of the `IROp` nodes. The`SnippetExecutionEngine` is the same as the `StagedExecutionEngine` except it uses a `SnippetCompiler` to compile only the bodies of the operators, then calls back into the interpreter. This is so the volume of the code getting generated is minimized, but has overheads of jumping around between compiled and interpreted code.

All of the engines use `JoinIndexes` to track locations of constants and variables in the Datalog rules, and a `PrecedenceGraph` which stores the dependencies of each rule.

## Storage Manager
This layer determines how the data is stored and some basic relational operators.
`CollectionsStorageEngine` uses Scala collections, which is a pull-based engine, and `RelationalStorageManager` which is a push-based engine. The type aliases, which are very likely to change, are stored in `StorageManager` and will likely require wrapping in order to operate over anything other than in-memory collections. `SimpleStorageManager` has shared functionality between the push and pull based engine.

## Tools
At the moment this package just contains the `Debug` statement, but is where any other future tooling can go. Debug will print to the console when the env var `CARAC_DEBUG` is defined. The `Printer` is where I put pretty printing statements for the entire architecture, its just located in the `storage` layer for convenience.

## Tests
There are manual tests located in `tests/*Test.scala` which are defined by hand, and then there are generated tests based on the programs defined in `tests/examples` using the `ExampleTestGenerator`. Each test may have an `facts` directory which defines facts to be pre-loaded into the engine, and an `expected` directory where the expected output for each program is stored. The body of the program itself is in the .scala file within each example subdirectory. IDBs are defined in `pretest` (or, for the cases where there is no `facts` directory, EDBs can be defined there too) and the relation for which we want to solve is indciated by `toSolve`.
Tags indicating metadata about the test, for example if it's really slow, are passed to the test superclass `ExampleTestGenerator`. Types of tests to be skipped can be indicated by setting the corresponding env var to be defined (this is to get around some missing functionality in MUnit).

## Bench
Benchmarks are stored here and are generated similary to tests.