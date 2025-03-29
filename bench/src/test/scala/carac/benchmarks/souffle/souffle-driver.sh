#!/usr/bin/env bash

if [[ $# -ne 3 ]]; then
  echo "USAGE: $0 <souffle> <benchmark> <mode>"
  exit 1
fi

SOUFFLE_BIN="$1"
BENCHMARK="$2"
MODE="$3"

FACTDIR="../src/test/scala/test/examples/$BENCHMARK/facts"
PROGRAM="src/test/scala/carac/benchmarks/souffle/$BENCHMARK.dl"

case "$MODE" in
  compile)
    OUTDIR="souffle-out/compile/$BENCHMARK"
    mkdir -p "$OUTDIR"
    ;;

  interp)
    OUTDIR="souffle-out/interp/$BENCHMARK"
    mkdir -p "$OUTDIR"
    ;;

  generate-preprofile)
    OUTDIR=""
    ;;

  preprofiled-compile)
    OUTDIR="souffle-out/preprofiled-compile/$BENCHMARK"
    mkdir -p "$OUTDIR"
    ;;

  preprofiled-interp)
    OUTDIR="souffle-out/preprofiled-interp/$BENCHMARK"
    mkdir -p "$OUTDIR"
    ;;

  profile-compile)
    OUTDIR="souffle-out/profile-compile/$BENCHMARK"
    mkdir -p "$OUTDIR"
    ;;

  profile-interp)
    OUTDIR="souffle-out/profile-interp/$BENCHMARK"
    mkdir -p "$OUTDIR"
    ;;

  *)
    echo "Invalid mode: $MODE"
    exit 1
    ;;
esac

# Check required files/dirs
if [[ ! -d "$FACTDIR" ]]; then
  echo "Fact directory $FACTDIR not found!"
  exit 1
fi
if [[ ! -f "$PROGRAM" ]]; then
  echo "Program $PROGRAM not found!"
  exit 1
fi

# Soufflé execution per mode
case "$MODE" in
  compile)
    $SOUFFLE_BIN -c -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" --jobs=1 \
      --wno=var-appears-once --wno=no-rules-nor-facts
    ;;

  interp)
    $SOUFFLE_BIN -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" --jobs=1 \
      --wno=var-appears-once --wno=no-rules-nor-facts
    ;;

  generate-preprofile)
    $SOUFFLE_BIN --profile="$BENCHMARK-preprofile" --emit-statistics \
      -F "$FACTDIR" "$PROGRAM" --jobs=1 \
      --wno=var-appears-once --wno=no-rules-nor-facts
    ;;

  preprofiled-compile)
    $SOUFFLE_BIN -c --a="$BENCHMARK-preprofile" -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" \
      --jobs=1 --wno=var-appears-once --wno=no-rules-nor-facts
    ;;

  preprofiled-interp)
    $SOUFFLE_BIN --a="$BENCHMARK-preprofile" -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" \
      --jobs=1 --wno=var-appears-once --wno=no-rules-nor-facts
    ;;

  profile-compile)
    $SOUFFLE_BIN -c --profile="$BENCHMARK-profile-compile" --emit-statistics \
      -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" --jobs=1 \
      --wno=var-appears-once --wno=no-rules-nor-facts

    $SOUFFLE_BIN -c --a="$BENCHMARK-profile-compile" \
      -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" --jobs=1 \
      --wno=var-appears-once --wno=no-rules-nor-facts
    ;;

  profile-interp)
    $SOUFFLE_BIN --profile="$BENCHMARK-profile-interp" --emit-statistics \
      -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" --jobs=1 \
      --wno=var-appears-once --wno=no-rules-nor-facts

    $SOUFFLE_BIN --a="$BENCHMARK-profile-interp" \
      -F "$FACTDIR" "$PROGRAM" -D "$OUTDIR" --jobs=1 \
      --wno=var-appears-once --wno=no-rules-nor-facts
    ;;
esac
