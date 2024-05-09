val scala3Version = "3.4.1"

lazy val root = (project in file("."))
  .settings(
    name := "BlackJack",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29",
      "org.scalactic" %% "scalactic" % "3.2.14",
      "org.scalatest" %% "scalatest" % "3.2.14" % Test
    )
  )

// Add scoverage plugin settings
coverageEnabled := true
coverageFailOnMinimum := true
coverageHighlighting := true

coverageMinimumStmtTotal := 90
coverageMinimumBranchTotal := 90
coverageMinimumStmtPerPackage := 90
coverageMinimumBranchPerPackage := 85
coverageMinimumStmtPerFile := 85
coverageMinimumBranchPerFile := 80