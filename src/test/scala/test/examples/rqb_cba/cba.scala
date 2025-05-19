package test.examples.rqb_cba

import buildinfo.BuildInfo
import carac.dsl.{Constant, Program, Relation}
import test.{ExampleTestGenerator, RunTyQL, Tags, TyQLComparative, TyQLComparativeTest}
import carac.storage.{DatabaseType, DuckDBStorageManager}
import tyql.Expr.{IntLit, StringLit}
import tyql.{DatabaseAST, Ord, Query, Table}

import java.nio.file.Paths
import language.experimental.namedTuples

class TyQLCBA extends TyQLComparativeTest with rqb_cba

trait rqb_cba extends TyQLComparative {
  val directory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/rqb_cba"
  val toSolve = "data_term"

  override def loadData(program: Program): Unit =
    loadDataFromFile(program, directory)

  override val expectedFacts = loadExpectedFile(Paths.get(directory, "expected"))("data_term")

  override def generateCarac(program: Program) =
    val term = program.namedRelation[Constant]("term")
    val app = program.namedRelation[Constant]("app")
    val lits = program.namedRelation[Constant]("lits")
    val vars = program.namedRelation[Constant]("vars")
    val abs = program.namedRelation[Constant]("abs")
    val data_term = program.relation[Constant]("recursive1")//data_term")
    val data_var = program.relation[Constant]("recursive2")//data_var")
    val ctrl_term = program.relation[Constant]("recursive3")//ctrl_term")
    val ctrl_var = program.relation[Constant]("recursive4")//ctrl_var")

    val i, v, l, x, t1, f, b, a = program.variable()
    val any1, any2 = program.variable()

    data_term( i, v ) :- ( term(i, "Lit", l), lits(l, v) )

    data_term( i, v ) :- ( term(i, "Var", x), data_var(x, v) )

    /*x*/ data_term( i, v ) :- ( term(i, "App", x), data_term(b, v), ctrl_term(t1, f), abs(f, any2, b), app(x, t1, any1) )

    /*x*/ data_var( i, v ) :- ( ctrl_term(a, f), data_term(b, v), abs(f, i, any2), app(any1, a, b))

    ctrl_term( i, v ) :- ( term(i, "Var", x), ctrl_var(x, v) )

    /*x*/ctrl_term( i, v ) :- ( term(i, "App", x), ctrl_term(b, v), ctrl_term(t1, f), abs(f, any2, b), app(x, t1 , any1) )

    ctrl_term( i, v ) :- term(i, "Abs", v)

    /*x*/ ctrl_var( i, v ) :- ( ctrl_term(a, f), ctrl_term(b, v),  abs(f, i, any2), app(any1, a, b) )

    data_term

  override def generateTyQL(program: Program) =
    // TYQL data model
    type Term = (x: Int, y: String, z: Int)
    type Lits = (x: Int, y: String)
    type Vars = (x: Int, y: String)
    type Abs = (x: Int, y: Int, z: Int)
    type App = (x: Int, y: Int, z: Int)
    type BaseData = (x: Int, y: String)
    type BaseCtrl = (x: Int, y: Int)

    val tyqlDB = (
      term = Table[Term]("term"),
      lits = Table[Lits]("lits"),
      vars = Table[Vars]("vars"),
      abs = Table[Abs]("abs"),
      app = Table[App]("app"),
      baseData = Table[BaseData]("baseDataVar"),
      baseCtrl = Table[BaseCtrl]("baseCtrlVar")
    )
    val dataTermBase = tyqlDB.term.flatMap(t =>
      tyqlDB.lits
        .filter(l => l.x == t.z && t.y == StringLit("Lit"))
        .map(l => (x = t.x, y = l.y).toRow))

    val dataVarBase = tyqlDB.baseData

    val ctrlTermBase = tyqlDB.term.filter(t => t.y == StringLit("Abs")).map(t => (x = t.x, y = t.z).toRow)

    val ctrlVarBase = tyqlDB.baseCtrl

    val (dataTerm, dataVar, ctrlTerm, ctrlVar) = Query.unrestrictedFix(dataTermBase, dataVarBase, ctrlTermBase, ctrlVarBase)(
      (dataTerm, dataVar, ctrlTerm, ctrlVar) => {
        val dt1 =
          for
            t <- tyqlDB.term
            dv <- dataVar
            if t.y == "Var" && t.z == dv.x
          yield (x = t.x, y = dv.y).toRow

        val dt2 =
          for
            t <- tyqlDB.term
            dt <- dataTerm
            ct <- ctrlTerm
            abs <- tyqlDB.abs
            app <- tyqlDB.app
            if t.y == "App" && t.z == app.x && dt.x == abs.z && ct.x == app.y && ct.y == abs.x
          yield (x = t.x, y = dt.y).toRow

        val dv =
          for
            ct <- ctrlTerm
            dt <- dataTerm
            abs <- tyqlDB.abs
            app <- tyqlDB.app
            if ct.x == app.y && ct.y == abs.x && dt.x == app.z
          yield (x = abs.y, y = dt.y).toRow

        val ct1 =
          for
            t <- tyqlDB.term
            cv <- ctrlVar
            if t.y == "Var" && t.z == cv.x
          yield (x = t.x, y = cv.y).toRow
        val ct2 =
          for
            t <- tyqlDB.term
            ct1 <- ctrlTerm
            ct2 <- ctrlTerm
            abs <- tyqlDB.abs
            app <- tyqlDB.app
            if t.y == "App" && t.z == app.x && ct1.x == abs.z && ct2.x == app.y && ct2.y == abs.x
          yield (x = t.x, y = ct1.y).toRow

        val cv =
          for
            ct1 <- ctrlTerm
            ct2 <- ctrlTerm
            abs <- tyqlDB.abs
            app <- tyqlDB.app
            if ct1.x == app.y && ct1.y == abs.x && ct2.x == app.z
          yield (x = abs.y, y = ct2.y).toRow

        val dt = dt1.union(dt2)
        val ct = ct1.union(ct2)

        (dt, dv, ct, cv)
      })
      dataTerm
  
  override def loadSchema(program: Program, duckDBStorageManager: DuckDBStorageManager): Unit =
    val term = program.relation("term")
    val termS = Seq(("x", DatabaseType.INTEGER), ("y", DatabaseType.TEXT), ("z", DatabaseType.INTEGER))
    duckDBStorageManager.declareTable(term.id, termS)
    duckDBStorageManager.edbs.initializeTable(term.id, "term", termS)
    val vars = program.relation("vars")
    val varsS = Seq(("x", DatabaseType.INTEGER), ("y", DatabaseType.TEXT))
    duckDBStorageManager.declareTable(vars.id, varsS)
    duckDBStorageManager.edbs.initializeTable(vars.id, "vars", varsS)
    val app = program.relation("app")
    val appS = Seq(("x", DatabaseType.INTEGER), ("y", DatabaseType.INTEGER), ("z", DatabaseType.INTEGER))
    duckDBStorageManager.declareTable(app.id, appS)
    duckDBStorageManager.edbs.initializeTable(app.id, "app", appS)
    val lits = program.relation("lits")
    val litsS = Seq(("x", DatabaseType.INTEGER), ("y", DatabaseType.TEXT))
    duckDBStorageManager.declareTable(lits.id, litsS)
    duckDBStorageManager.edbs.initializeTable(lits.id, "lits", litsS)
    val abs = program.relation("abs")
    val absS = Seq(("x", DatabaseType.INTEGER), ("y", DatabaseType.INTEGER), ("z", DatabaseType.INTEGER))
    duckDBStorageManager.declareTable(abs.id, absS)
    duckDBStorageManager.edbs.initializeTable(abs.id, "abs", absS)

    val baseData = program.relation("baseDataVar")
    val baseDataS = Seq(("x", DatabaseType.INTEGER), ("y", DatabaseType.TEXT))
    duckDBStorageManager.declareTable(baseData.id, baseDataS)
    duckDBStorageManager.edbs.initializeTable(baseData.id, "baseDataVar", baseDataS)

    val baseCtrl = program.relation("baseCtrlVar")
    val baseCtrlS = Seq(("x", DatabaseType.INTEGER), ("y", DatabaseType.INTEGER))
    duckDBStorageManager.declareTable(baseCtrl.id, baseCtrlS)
    duckDBStorageManager.edbs.initializeTable(baseCtrl.id, "baseCtrlVar", baseCtrlS)
}
