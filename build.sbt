// See README.md for license details.

//ThisBuild / scalaVersion     := "2.13.10"
//ThisBuild / version          := "0.1.0"
//ThisBuild / organization     := "UofT"
//
//val chiselVersion = "3.6.0"
//val chiselTestVersion = "0.6.0"
//
//lazy val root = (project in file("."))
//  .settings(
//    name := "SystolicArray",
//    libraryDependencies ++= Seq(
//      "org.chipsalliance" %% "chisel3" % "3.6.0",
//      "org.chipsalliance" %% "chiseltest" % "0.6.0" % Test
//      // "org.chipsalliance" %% "chisel3" % chiselVersion,
//      // "org.chipsalliance" %% "chiseltest" % chiselTestVersion % Test,
//      // "org.scalatest" %% "scalatest" % "3.2.16" % Test // For ScalaTest integration
//    ),
//    scalacOptions ++= Seq(
//      "-language:reflectiveCalls",
//      "-deprecation",
//      "-feature",
//      "-Xcheckinit",
//      "-Ymacro-annotations"
//    ),
//    addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full),
//    Compile / run / javaOptions += "-Dgenerated.files.dir=generated"
//  )

// This is based on build.sbt in the chisel-template reposiory

//ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / scalaVersion     := "2.12.17"
ThisBuild / version          := "0.0.3"

//val chiselVersion = "3.5.4"
//val chiseltestVersion = "0.5.4"
val chiselVersion = "3.6.0"
val chiseltestVersion = "0.6.0"


lazy val root = (project in file("."))
  .settings(
    name := "simple-systolic-matmul",
    libraryDependencies ++= Seq(
      "edu.berkeley.cs" %% "chisel3" % chiselVersion,
      "edu.berkeley.cs" %% "chiseltest" % chiseltestVersion % "test"
    ),
    scalacOptions ++= Seq(
      "-language:reflectiveCalls",
      "-deprecation",
      "-feature",
      "-Xcheckinit",
      "-P:chiselplugin:genBundleElements",
    ),
    addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full),
  )