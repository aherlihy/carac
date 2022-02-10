inThisBuild(Seq(
  organization := "ch.epfl.lamp",
  scalaVersion := "3.1.1",
  version := "0.1",
))

lazy val root = project.in(file("."))
  .settings(
    name := "datalog",

    libraryDependencies ++= Seq(
      "org.scala-lang" %% "scala3-staging" % scalaVersion.value,
      "org.scalameta" %% "munit" % "0.7.29" % Test,
    ),
  )

lazy val bench = project.in(file("bench"))
  .dependsOn(root)
  .enablePlugins(JmhPlugin)
