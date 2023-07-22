#!/usr/bin/env bash

if [[ $# -eq 0 ]] ; then
    echo 'ERROR: no arg passed'
    exit 0
fi

TESTNAME="$1"
TESTDIR=./src/test/scala/test/examples/$TESTNAME

IMPORTS="package test.examples.$TESTNAME\nimport datalog.dsl.{Constant, Program}\nimport test.ExampleTestGenerator\n"

TEMPLATE="class ${TESTNAME}_test extends ExampleTestGenerator(\"$TESTNAME\") with $TESTNAME\n"
TEMPLATE2="trait $TESTNAME {\n  val toSolve: String = \"$TESTNAME\"\n  def pretest(program: Program): Unit = {"

echo "mkdir $TESTDIR/expected"
mkdir "$TESTDIR"/expected

echo "mv $TESTDIR/*.csv $TESTDIR/expected"
mv "$TESTDIR/"*.csv $TESTDIR/expected

for i in "$TESTDIR"/*.{err,out}; do
  [ -f "$i" ] || break
  if [ -s $i ]; then
    echo "ERROR: $i not empty"
    exit
  else
    echo "rm -rf $i"
    rm -rf "$i"
  fi
done

for i in "$TESTDIR"/*.dl; do
  [ -f "$i" ] || break
  PROGRAM=${i%.dl}.scala
  touch $PROGRAM
  echo -e "$IMPORTS$TEMPLATE$TEMPLATE2" > $PROGRAM
  for e in $TESTDIR/facts/*.facts; do
    [ -f "$e" ] || break
    EDB=$(basename $e .facts)
    echo -e "    val $EDB = program.namedRelation[Constant](\"$EDB\")\n" >> $PROGRAM
  done
  echo -e "    val x, y, z = program.variable()" >> $PROGRAM
  for j in $TESTDIR/expected/*.csv; do
    [ -f "$j" ] || break
    IDB=$(basename $j .csv)
    echo -e "    val $IDB = program.relation[Constant](\"$IDB\")\n" >> $PROGRAM
  done

  cat $i | sed -e  '/^\/\//d' | sed -e '/^\./d' | sed -e '/^$/N;/^\n$/D' | sed -e 's/^/    /' | sed -e 's/\:-/\:- (/' | sed -e 's/\.$/\ )/' >> $PROGRAM

  echo -e "  }\n}" >> $PROGRAM
  git add $PROGRAM
  git reset $i
done
git add "$TESTDIR"/expected



