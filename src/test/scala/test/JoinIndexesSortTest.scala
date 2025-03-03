package test

import datalog.dsl.{Atom, Constant, Program, __}
import datalog.execution.{ExecutionEngine, JoinIndexes, ShallowExecutionEngine}
import datalog.storage.{CollectionsStorageManager, NS}

import scala.collection.mutable

class JoinIndexesSortTest extends munit.FunSuite {
  test("JoinIdx sort simple") {
    val sm = new CollectionsStorageManager()
    val engine = new ShallowExecutionEngine(sm)
    val program = Program(engine)

    val one0 = program.relation[Constant]("one0")
    val one1 = program.relation[Constant]("one1")
    val two1 = program.relation[Constant]("two1")
    val two2 = program.relation[Constant]("two2")
    val two3 = program.relation[Constant]("two3")
    val three4 = program.relation[Constant]("three4")
    val three5 = program.relation[Constant]("three5")
    val three6 = program.relation[Constant]("three6")
    val three7 = program.relation[Constant]("three7")
    val threeAndOne7 = program.relation[Constant]("threeAndOne7")
    val extra9 = program.relation[Constant]("extra9")
    val extra10 = program.relation[Constant]("extra10")

    val rule = program.relation[Constant]("rule")

    val x, a, b, c, d, e, f, g, h, i = program.variable()

    one1(1) :- ()

    two1(1, 2) :- ()

    two2(0, 1) :- ()
    two2(1, 0) :- ()

    two3(1, 0) :- ()
    two3(0, 0) :- ()
    two3(1, 1) :- ()

    three4(1, 0, 1) :- ()
    three4(1, 1, 1) :- ()
    three4(1, 0, 0) :- ()
    three4(0, 0, 0) :- ()

    three5(1, 0, 0) :- ()
    three5(1, 1, 0) :- ()
    three5(1, 0, 1) :- ()
    three5(1, 1, 0) :- ()
    three5(1, 1, 1) :- ()

    three6(1, 0, 0) :- ()
    three6(1, 1, 0) :- ()
    three6(1, 0, 1) :- ()
    three6(1, 1, 0) :- ()
    three6(1, 1, 1) :- ()
    three6(0, 0, 0) :- ()

    three7(1, 0, 0) :- ()
    three7(1, 1, 0) :- ()
    three7(1, 0, 1) :- ()
    three7(1, 1, 0) :- ()
    three7(1, 1, 1) :- ()
    three7(0, 0, 0) :- ()
    three7(2, 0, 0) :- ()

    threeAndOne7(1) :- ()
    threeAndOne7(2) :- ()
    threeAndOne7(3) :- ()
    threeAndOne7(4) :- ()
    threeAndOne7(5) :- ()
    threeAndOne7(6) :- ()
    threeAndOne7(7) :- ()
    threeAndOne7(8) :- ()

    extra9(1) :- ()
    extra9(2) :- ()
    extra9(3) :- ()
    extra9(4) :- ()
    extra9(5) :- ()
    extra9(6) :- ()
    extra9(7) :- ()
    extra9(8) :- ()
    extra9(9) :- ()

    extra10(1) :- ()
    extra10(10) :- ()
    extra10(2) :- ()
    extra10(3) :- ()
    extra10(4) :- ()
    extra10(5) :- ()
    extra10(6) :- ()
    extra10(7) :- ()
    extra10(8) :- ()
    extra10(9) :- ()

    val atom1 = one0(x)
    val atom2 = one1(x)
    val atom3 = two2(a, b)
    val atom4 = two3(a, b)
//    val atom5 = extra9(a, b)
    val atom6 = three4(c, d, e)
    val atom7 = three5(c, d, e)
    val atom8 = three6(c, d, e)
    val atom9 = three7(c, d, e)
    val atom10 = threeAndOne7(f, g, h, i)
    val atom11 = extra10(f, g, h)
    val atom12 = extra9(i, f, g, h, x)

    rule(x) :- (
      atom6, atom9, atom8, atom7,
      atom1, atom4,
      atom3, atom2,
//      atom10, atom11, atom12
    )

    val jIdx = sm.allRulesAllIndexes(rule.id)(engine.keyHashs(rule.id).head)

    assertEquals(
      jIdx.cxns,
      mutable.Map(
        atom1.hash -> mutable.Map(
          1-> Seq(atom2.hash)),
        atom2.hash -> mutable.Map(
          1 -> Seq(atom1.hash)),
        atom3.hash -> mutable.Map(
          2 -> Seq(atom4.hash)),
//        atom4.hash -> mutable.Map(
//          2 -> Seq(atom3.hash, atom5.hash)),
        atom4.hash -> mutable.Map(
          2 -> Seq(atom3.hash)),
        atom6.hash -> mutable.Map(
          3 -> Seq(atom9.hash, atom8.hash, atom7.hash)),
        atom7.hash -> mutable.Map(
          3 -> Seq(atom6.hash, atom9.hash, atom8.hash)),
        atom8.hash -> mutable.Map(
          3 -> Seq(atom6.hash, atom9.hash, atom7.hash)),
        atom9.hash -> mutable.Map(
          3 -> Seq(atom6.hash, atom8.hash, atom7.hash)),
      )
    )
    val (newBody, hash) = JoinIndexes.presortSelect((a, d) => (true, sm.getEDBResult(a.rId).size), jIdx, sm, -1)
    val expected = Array(atom1.hash, atom2.hash, atom3.hash, atom4.hash, atom6.hash, atom7.hash, atom8.hash, atom9.hash)

    assertEquals(
      newBody.map(n => sm.ns.hashToAtom(n._1.hash)).toSeq,
      expected.map(e => sm.ns.hashToAtom(e)).toSeq
    )
  }
  test("sort with cycle") {
    val sm = new CollectionsStorageManager()

    val engine = new ShallowExecutionEngine(sm)

    val program = Program(engine)

    val one0 = program.relation[Constant]("one0")
    val one1 = program.relation[Constant]("one1")
    val two1 = program.relation[Constant]("two1")
    val two2 = program.relation[Constant]("two2")
    val two3 = program.relation[Constant]("two3")
    val three4 = program.relation[Constant]("three4")
    val three5 = program.relation[Constant]("three5")
    val three6 = program.relation[Constant]("three6")
    val three7 = program.relation[Constant]("three7")
    val threeAndOne7 = program.relation[Constant]("threeAndOne7")
    val extra9 = program.relation[Constant]("extra9")
    val extra10 = program.relation[Constant]("extra10")

    val rule = program.relation[Constant]("rule")

    val x, a, b, c, d, e, f, g, h, i = program.variable()

    one1(1) :- ()

    two1(1, 2) :- ()

    two2(0, 1) :- ()
    two2(1, 0) :- ()

    two3(1, 0) :- ()
    two3(0, 0) :- ()
    two3(1, 1) :- ()

    three4(1, 0, 1) :- ()
    three4(1, 1, 1) :- ()
    three4(1, 0, 0) :- ()
    three4(0, 0, 0) :- ()

    three5(1, 0, 0) :- ()
    three5(1, 1, 0) :- ()
    three5(1, 0, 1) :- ()
    three5(1, 1, 0) :- ()
    three5(1, 1, 1) :- ()

    three6(1, 0, 0) :- ()
    three6(1, 1, 0) :- ()
    three6(1, 0, 1) :- ()
    three6(1, 1, 0) :- ()
    three6(1, 1, 1) :- ()
    three6(0, 0, 0) :- ()

    three7(1, 0, 0) :- ()
    three7(1, 1, 0) :- ()
    three7(1, 0, 1) :- ()
    three7(1, 1, 0) :- ()
    three7(1, 1, 1) :- ()
    three7(0, 0, 0) :- ()
    three7(2, 0, 0) :- ()

    threeAndOne7(1, 1, 1, 1) :- ()
    threeAndOne7(2, 2, 2, 2) :- ()
    threeAndOne7(3, 3, 3, 3) :- ()
    threeAndOne7(4, 4, 4, 4) :- ()
    threeAndOne7(5, 5, 5, 5) :- ()
    threeAndOne7(6, 6, 6, 6) :- ()
    threeAndOne7(7, 7, 7, 7) :- ()
    threeAndOne7(8, 8, 8, 8) :- ()

    extra9(1, 1, 1, 1, 1) :- ()
    extra9(2, 2, 2, 2, 2) :- ()
    extra9(3, 3, 3, 3, 3) :- ()
    extra9(4, 4, 4, 4, 4) :- ()
    extra9(5, 5, 5, 5, 5) :- ()
    extra9(6, 6, 6, 6, 6) :- ()
    extra9(7, 7, 7, 7, 7) :- ()
    extra9(8, 8, 8, 8, 8) :- ()
    extra9(9, 9, 9, 9, 9) :- ()

    extra10(1, 1, 1) :- ()
    extra10(10, 10, 10) :- ()
    extra10(2, 2, 2) :- ()
    extra10(3, 3, 3) :- ()
    extra10(4, 4, 4) :- ()
    extra10(5, 5, 5) :- ()
    extra10(6, 6, 6) :- ()
    extra10(7, 7, 7) :- ()
    extra10(8, 8, 8) :- ()
    extra10(9, 9, 9) :- ()

    val atom1 = one0(x)
    val atom2 = one1(x)
    val atom3 = two2(a, b)
    val atom4 = two3(a, b)
    val atom5 = extra9(a, b, a, b, a)
    val atom6 = three4(c, d, e)
    val atom7 = three5(c, d, e)
    val atom8 = three6(c, d, e)
    val atom9 = three7(c, d, e)
    val atom10 = threeAndOne7(f, g, h, i) // 3: 11, 4: 12
    val atom11 = extra10(f, g, h) // 3: 10, 12
    val atom12 = extra9(i, f, g, h, x) // 4: 10, 3: 11, 1: 1,2

    rule(x) :- (
      atom10, atom11, atom12, atom5,
      atom6, atom9, atom8, atom7,
      atom1, atom4,
      atom3, atom2,
    )

    val jIdx = sm.allRulesAllIndexes(rule.id)(engine.keyHashs(rule.id).head)

    assertEquals(
      jIdx.cxns.map((k, v) => (sm.ns.hashToAtom(k), v.map((c, hs) => (c, hs.map(h => sm.ns.hashToAtom(h)).toList.sorted)))),
      mutable.Map(
        atom1.hash -> mutable.Map(
          1 -> Seq(atom2.hash, atom12.hash)),
        atom2.hash -> mutable.Map(
          1 -> Seq(atom1.hash, atom12.hash)),
        atom3.hash -> mutable.Map(
          2 -> Seq(atom4.hash, atom5.hash)),
        atom4.hash -> mutable.Map(
          2 -> Seq(atom5.hash, atom3.hash)),
        atom5.hash -> mutable.Map(
          2 -> Seq(atom3.hash, atom4.hash)),
        atom6.hash -> mutable.Map(
          3 -> Seq(atom9.hash, atom8.hash, atom7.hash)),
        atom7.hash -> mutable.Map(
          3 -> Seq(atom6.hash, atom9.hash, atom8.hash)),
        atom8.hash -> mutable.Map(
          3 -> Seq(atom6.hash, atom9.hash, atom7.hash)),
        atom9.hash -> mutable.Map(
          3 -> Seq(atom6.hash, atom8.hash, atom7.hash)),
        atom10.hash -> mutable.Map(
          3 -> Seq(atom11.hash), 4 -> Seq(atom12.hash)),
        atom11.hash -> mutable.Map(
          3 -> Seq(atom10.hash, atom12.hash)),
        atom12.hash -> mutable.Map(
          1 -> Seq(atom1.hash, atom2.hash), 3 -> Seq(atom11.hash), 4 -> Seq(atom10.hash))
      ).map((k, v) => (sm.ns.hashToAtom(k), v.map((c, hs) => (c, hs.map(h => sm.ns.hashToAtom(h)).toList.sorted))))
    )
    val (newBody, hash) = JoinIndexes.presortSelect((a, _) => (true, sm.getEDBResult(a.rId).size), jIdx, sm, -1)
    val expected = Array(atom1.hash, atom2.hash, atom12.hash, atom10.hash, atom11.hash, atom3.hash, atom4.hash, atom5.hash, atom6.hash, atom7.hash, atom8.hash, atom9.hash)

    assertEquals(
      newBody.map(n => sm.ns.hashToAtom(n._1.hash)).toSeq,
      expected.map(e => sm.ns.hashToAtom(e)).toSeq
    )
  }
  test("sort with repeated vars") {
    val sm = new CollectionsStorageManager()

    val engine = new ShallowExecutionEngine(sm)

    val program = Program(engine)

    val one0 = program.relation[Constant]("one0")
    val one1 = program.relation[Constant]("one1")
    val one2 = program.relation[Constant]("one2")
    one1(1, 2, -5, -6) :- ()
    one2(1, 2, -1, -2) :- ()
    one2(0, 3, -3, -4) :- ()
    val x, y, z, a, b, c, d, e, f, g = program.variable()
    val q1, q2, q3, q4, q5, q6, q7, q8 = program.variable()

    val rule = program.relation[Constant]("rule")

    val a0 = one0(x) // card = 0 so start
    val a1 = one1(y, z, q5, q6)
    val a2 = one1(x, a, q7, q8) // -> x, a -> a3
    val a3 = one2(a, b, q1, q2) // -> a, b -> (a4/a6) but a4 > a6 so -> a6
    val a4 = one2(b, g, q3 ,q4)
    val a5 = one2(c, d, e, f) // -> d/e/f, go to stack. a1 < a4 so -> a1
    val a6 = one1(b, d, e, f) // -> b, d/e/f -> a5

//    rule(x) :- (a5, a2, a4, a6, a0, a3, a1)
    rule(x) :- (a3, a1, a6, a4, a0, a5, a2)

    val jIdx = sm.allRulesAllIndexes(rule.id)(engine.keyHashs(rule.id).head)

    val (newBody, hash) = JoinIndexes.presortSelect((a, d) => (true, sm.getEDBResult(a.rId).size), jIdx, sm, -1)
    val expected = Array(a0.hash, a2.hash, a3.hash, a6.hash, a5.hash, a1.hash, a4.hash)

    assertEquals(
      newBody.map(n => sm.ns.hashToAtom(n._1.hash)).toSeq,
      expected.map(e => sm.ns.hashToAtom(e)).toSeq
    )
  }
  test("worst sort with repeated vars") {
    val sm = new CollectionsStorageManager()

    val engine = new ShallowExecutionEngine(sm)

    val program = Program(engine)

    val one3 = program.relation[Constant]("one3")
    val one1 = program.relation[Constant]("one1")
    val one2 = program.relation[Constant]("one2")
    val one0 = program.relation[Constant]("one0")
    one1(1, 1) :- ()
    one2(1, 1, -3, -4) :- ()
    one2(0, 0, -5, -6) :- ()
    one3(0) :- ()
    one3(2) :- ()
    one3(1) :- ()
    val x, y, z, a, b, c, d, e, f, g = program.variable()
    val q1, q2, q3, q4 = program.variable()

    val rule = program.relation[Constant]("rule")

    val a0 = one3(x) // card = 3 so start. -> x so not a2, next biggest card -> one2, leaving -> a4
    val a1 = one0(a, z, a, z) // a5 bc last
    val a2 = one2(x, a, q1, q2) // not a2 again, leaving a1/a3/a5. all have 'a' so take next biggest -> a3
    val a3 = one1(a, b) // -> a1,a5,a2 left, have 2 in common with a5 so -> a1 or a2. a2 > a1 so a2
    val a4 = one2(b, d, e, f) // not a3, leaving a2/a3/a5. next biggest card = one2 -> a2
    val a5 = one0(a, b, q3, q4)
    // a2 = one2(x, a) // a1 and a5 left, both have one in common so either

    //    rule(x) :- (a5, a2, a4, a6, a0, a3, a1)
    rule(x) :- (a3, a1, a4, a0, a2, a5, a2)

    val jIdx = sm.allRulesAllIndexes(rule.id)(engine.keyHashs(rule.id).head)

    val (newBody, hash) = JoinIndexes.presortSelectWorst((a, d) => (true, sm.getEDBResult(a.rId).size), jIdx, sm, -1)
    val expected = Array(a0.hash, a4.hash, a2.hash, a3.hash, a2.hash, a5.hash, a1.hash)

    assertEquals(
      newBody.map(n => sm.ns.hashToAtom(n._1.hash)).toSeq,
      expected.map(e => sm.ns.hashToAtom(e)).toSeq
    )
  }
}
