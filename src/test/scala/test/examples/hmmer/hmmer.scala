//package test.examples.hmmer
//import datalog.dsl.{Constant, Program, __}
//import test.ExampleTestGenerator
//class hmmer_test extends ExampleTestGenerator("hmmer") with hmmer
//trait hmmer {
//  val toSolve: String = "CPtrLoad"
//  def pretest(program: Program): Unit = {
//    val DirectFlow = program.namedRelation[Constant]("DirectFlow")
//
//    val EscapePtr = program.namedRelation[Constant]("EscapePtr")
//
//    val ExtReturn = program.namedRelation[Constant]("ExtReturn")
//
//    val Function = program.namedRelation[Constant]("Function")
//
//    val Global = program.namedRelation[Constant]("Global")
//
//    val HeapAlloc = program.namedRelation[Constant]("HeapAlloc")
//
//    val Load = program.namedRelation[Constant]("Load")
//
//    val StackAlloc = program.namedRelation[Constant]("StackAlloc")
//
//    val Store = program.namedRelation[Constant]("Store")
//
//    val v, w, o, src, dest, o1, o2, addr, arg = program.variable()
//    val CPtrLoad = program.relation[Constant]("CPtrLoad")
//
//    val CPtrStore = program.relation[Constant]("CPtrStore")
//    val LptrVar = program.relation[Constant]("LptrVar")
//    val IsPtr = program.relation[Constant]("IsPtr")
//    val Memory = program.relation[Constant]("Memory")
//    val IsReachable = program.relation[Constant]("IsReachable")
//    val CFormat = program.relation[Constant]("CFormat")
//
//    LptrVar(v) :- ( StackAlloc(v,__) )
//    LptrVar(v) :- ( HeapAlloc(v,__) )
//    LptrVar(v) :- ( Load(__,v) )
//    LptrVar(v) :- ( Store(__,v) )
//    LptrVar(v) :- ( DirectFlow(v,w), LptrVar(w) )
//    LptrVar(v) :- ( DirectFlow(w,v), LptrVar(w) )
//
//    IsPtr(v,o) :- ( HeapAlloc(v,o) )
//    IsPtr(v,o) :- ( StackAlloc(v,o) )
//    IsPtr(dest,o) :- ( DirectFlow(src,dest), IsPtr(src,o) )
//    IsPtr(dest,o) :- ( Load(dest, addr), IsPtr(addr, o1), Memory(o1,o) )
//    IsPtr(v,o) :- ( ExtReturn(arg,v), IsReachable(arg,o) )
//
//    IsPtr(v,o) :- ( Global(v, o) )
//
//    Memory(o1,o2) :- ( Store(src,addr), IsPtr(src,o2), IsPtr(addr, o1) )
//
//    IsReachable(v,o) :- ( IsPtr(v,o) )
//    IsReachable(v,o) :- ( Memory(o1,o), IsReachable(v,o1) )
//
//    CFormat(o) :- ( EscapePtr(v), IsReachable(v,o) )
//
//    IsPtr(v,"ttt_obj") :- ( ExtReturn(__,v) )
//
//    CFormat("ttt_obj") :- ( ExtReturn(__,__) )
//    Memory("ttt_obj", "ttt_obj") :- ( ExtReturn(__,__) )
//
//    CPtrLoad(dest, addr) :- ( Load(dest, addr), IsPtr(addr, o), CFormat(o), LptrVar(dest) )
//    CPtrStore(src, addr) :- ( Store(src, addr), IsPtr(addr, o), CFormat(o), LptrVar(src) )
//
//    CFormat(o) :- ( CPtrLoad(__,v), IsPtr(v,o) )
//    CFormat(o) :- ( CPtrStore(__,v), IsPtr(v,o) )
//
//    CFormat(o) :- ( Load(v,addr), IsPtr(addr,o), !LptrVar(v) )
//    CFormat(o) :- ( Store(v,addr), IsPtr(addr,o), !LptrVar(v) )
//  }
//}
