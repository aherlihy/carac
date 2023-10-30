package test.examples.sequences

import buildinfo.BuildInfo
import datalog.dsl.{Constant, Program, __, *}
import test.{ExampleTestGenerator, Tags}
class sequences_test extends ExampleTestGenerator("sequences") with sequences
trait sequences {
  val factDirectory = s"${BuildInfo.baseDirectory}/src/test/scala/test/examples/sequences/facts"
  val toSolve: String = "_"
  def pretest(program: Program): Unit = {
    val num_letters = program.relation[Constant]("num_letters")

    val dom = program.relation[Constant]("dom")

    val a = program.relation[Constant]("a")
    val n = program.relation[Constant]("n")
    val s = program.relation[Constant]("s")

    val op_add = program.relation[Constant]("op_add")
    val op_mul = program.relation[Constant]("op_mul")
    val op_exp = program.relation[Constant]("op_exp")
    val op_log = program.relation[Constant]("op_log")
    val op_div = program.relation[Constant]("op_div")
    val op_mod = program.relation[Constant]("op_mod")

    val idx = program.relation[Constant]("idx")

    val x, y, r, b, y2, z, px, py, pr, sr = program.variable()


    num_letters(3) :- ()

    a("a") :- ()
    a("b") :- ()
    a("c") :- ()

    (0 to 120).foreach(o =>
      n(o) :- ()
    )

    (0 to 120).foreach(o =>
      s(o, o+1) :- ()
    )

    (0 to 121).foreach(o =>
      dom(o) :- ()
    )

    op_add(x, 0, x) :- n(x)
    op_add(x, y, r) :- (s(py, y), op_add(x, py, pr), s(pr, r))

    op_mul(x, 0, 0) :- n(x)
    op_mul(x, y, r) :- (s(py, y), op_mul(x, py, pr), op_add(pr, x, r))

    
    op_exp(x, 0, 1) :- n(x)
    op_exp(x, y, r) :- (s(py, y), op_exp(x, py, pr), op_mul(pr, x, r))
    
    op_log(x, b, r) :- (op_exp(b, r, y), s(r, sr), op_exp(b, sr, y2), n(x), y |<=| x, x |<| y2)
    
    op_div(x, y, r) :- (op_mul(r, y, sr), op_add(sr, z, x), 0 |<=| z, z |<| y)
    
    op_mod(x, y, r) :- (op_mul(y, __, z), op_add(z, r, x), 0 |<=| r, r |<| y)
    
    idx(0, "a") :- ()
    idx(1, "b") :- ()
    idx(2, "c") :- ()
    
    val trie_letter = program.relation[Constant]("trie_letter")
    val trie_level_end = program.relation[Constant]("trie_level_end")
    val trie_level_start = program.relation[Constant]("trie_level_start")
    val trie_level = program.relation[Constant]("trie_level")
    val trie_parent = program.relation[Constant]("trie_parent")
    val trie_root = program.relation[Constant]("trie_root")
    val trie = program.relation[Constant]("trie")
    
    val pl, p, i, l, low, high, c, o = program.variable()
    
    trie_letter(z,b) :- (s(x,z), num_letters(sr), op_mod(x,sr,r), idx(r,b))
    
    trie_level_end(0,0) :- ()
    trie_level_end(l,i) :- (num_letters(sr), s(pl,l), trie_level_end(pl,b), op_exp(sr,l,p), op_add(b,p,i))
    
    trie_level_start(0,0) :- ()
    trie_level_start(l,i) :- (s(pl,l), trie_level_end(pl,b), op_add(b,1,i))
    
    trie_level(0,0) :- ()
    trie_level(i,b) :- (n(i), s(z,b), trie_level_end(z,low), trie_level_end(b,high), low |<| i, i |<=| high)
    
    trie_parent(i,p) :- (num_letters(z), trie_level(i,l), s(pl,l), trie_level_start(l,b), op_add(b,x,i), op_div(x,z,o), trie_level_start(pl,c), op_add(c,o,p))
    
    trie_root(0) :- ()
    
    trie(x) :- trie_letter(x,__)
    
    val str = program.relation[Constant]("str")
    val str_len = program.relation[Constant]("str_len")
    val str_chain = program.relation[Constant]("str_chain")
    val str_letter_at = program.relation[Constant]("str_letter_at")

    val id, sx, sy = program.variable()

    str(x) :- trie(x)

    str_len(id,l) :- trie_level(id, l)

    str_chain(id,id) :- trie(id)
    str_chain(id,p) :- (str_chain(id,x), trie_parent(x,p))

    str_letter_at(id,z,l) :- (str_chain(id,p), trie_level(p,z), trie_letter(p,l))

    val palin_aux = program.relation[Constant]("palin_aux")
    val palindrome = program.relation[Constant]("palindrome")
    val debug_str = program.relation[Constant]("debug_str")
    val read = program.relation[Constant]("read")

    palin_aux(b,x,x) :- (str(b), n(x), str_len(b,l), x |<=| l)
    palin_aux(b,x,z) :- (str_letter_at(b, x, __), z |=| (x + 1), dom(z))
    palin_aux(b,x,sy) :- (str_letter_at(b,x,z), s(x,sx), palin_aux(b,sx,y), str_letter_at(b,y,z), s(y,sy))

    palindrome(z) :- str_len(z,0)
    palindrome(z) :- (palin_aux(z,1,sr), str_len(z,l), s(l,sr))

    debug_str(96) :- ()

    read(x,y) :- (debug_str(z), str_letter_at(z,x,y))

  }
}
