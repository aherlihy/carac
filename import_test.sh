#!/usr/bin/env bash

if [[ $# -eq 0 ]] ; then
    echo 'ERROR: no arg passed'
    exit 0
fi

TESTNAME=$1
TESTDIR=./src/test/scala/graphs/fromFile/complete/$TESTNAME

TEMPLATE="package graphs\n\nimport datalog.dsl.{Program, Constant}\nclass $1 extends TestIDB {\n"
TEMPLATE2="  def run(program: Program): Unit = {"

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
  echo -e "$TEMPLATE" > $PROGRAM
  echo -e "$TEMPLATE2" >> $PROGRAM
  for e in $TESTDIR/facts/*.facts; do
    EDB=$(basename $e .facts)
    echo -e "    val $EDB = program.relation[Constant](\"$EDB\")\n" >> $PROGRAM
  done
  for j in $TESTDIR/expected/*.csv; do
     IDB=$(basename $j .csv)
     echo -e "    val $IDB = program.namedRelation[Constant](\"$IDB\")\n" >> $PROGRAM
  done

  cat $i | sed -e  '/^\/\//d' | sed -e '/^\./d' | sed -e '/^$/N;/^\n$/D' | sed -e 's/^/    /' | sed -e 's/\:-/\:- (/' | sed -e 's/\.$/\ )/' >> $PROGRAM

  echo -e "  }\n}" >> $PROGRAM
done



