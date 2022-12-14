package graphs.fromFile.incomplete.bigrams

import datalog.dsl.{Constant, Program}
import graphs.TestIDB
class bigrams extends TestIDB {

  def run(program: Program): Unit = {
    val words = program.relation[Constant]("words")

    val bigram = program.namedRelation[Constant]("bigram")

    val w, w1, i = program.variable()
    
    bigram(w, w1) :- ( words(i, w), words(i/*TODO: agg +1*/, w1) )
  }
}
