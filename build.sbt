import java.util.regex.Pattern
import java.io.File.pathSeparator
import java.nio.file.{Files, Paths}

inThisBuild(Seq(
  organization := "ch.epfl.lamp",
  scalaVersion := "3.3.1-RC4",
//  scalaVersion := "3.3.1-RC1-bin-SNAPSHOT",
  version := "0.1",
))

scalacOptions ++= Seq("-deprecation", "-feature")

lazy val root = project.in(file("."))
  .enablePlugins(PackPlugin)
  .enablePlugins(BuildInfoPlugin)
  .enablePlugins(NativeImagePlugin)
  .settings(
    name := "datalog",

    // By default, the plugin will fetch graalvm from the Internet itself, but
    // it only supports the Community Edition. Here we customize it to look up
    // graalvm's `native-image` on the PATH which lets us setup whatever version
    // we want.
    nativeImageInstalled := true,
    nativeImageGraalHome := {
      val executable = "native-image"
      val paths = System.getenv("PATH").split(Pattern.quote(pathSeparator))
      paths.map(Paths.get(_)).find(p => Files.isExecutable(p.resolve(executable))) match {
        case Some(binDir) =>
          binDir.getParent
        case None =>
          throw new MessageOnlyException(s"Could not find binary `$executable` in PATH.")
      }
    },
    nativeImageCommand := nativeImageCommand.value ++ Seq(
      // Do not generate an image containing a JVM in case of issues with native generation
      "--no-fallback",
      // Some of our backends use runtime reflection and classloading, just
      // ignore them (and crash at runtime if we end up calling them).
      "--report-unsupported-elements-at-runtime"
    ),

    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
      "org.glavo" % "classfile" % "0.4.0", // Copy of jdk.internal.classfile, won't be necessary when https://openjdk.org/jeps/8280389 is done.
      "org.scalameta" %% "munit" % "0.7.29" % Test,
    ),

    buildInfoKeys := Seq[BuildInfoKey](baseDirectory),
    buildInfoPackage := "buildinfo",
  )

lazy val bench = project.in(file("bench"))
  .dependsOn(root)
  .dependsOn(root % "test->test")
  .enablePlugins(JmhPlugin)
  .settings(
    Jmh/sourceDirectory := (Test/sourceDirectory).value,
    Jmh/classDirectory := (Test/classDirectory).value,
    Jmh/dependencyClasspath := (Test/dependencyClasspath).value,
    // rewire tasks, so that 'jmh:run' automatically invokes 'jmh:compile' (otherwise a clean 'jmh:run' would fail)
    Jmh/compile := (Jmh/compile).dependsOn(Test/compile).value,
    Jmh/run := (Jmh/run).dependsOn(Jmh/compile).evaluated,

    // sbt-jmh generates a ton of Java files, but they're never referenced by Scala files.
    // By enforcing this using `compileOrder`, we avoid having to run these generated files
    // through the Scala typechecker which has a significant impact on compile-time.
    Jmh/compileOrder := CompileOrder.ScalaThenJava,
  )
