val scala3Version = "3.4.1"

lazy val root = (project in file("."))
  .settings(
    name := "BlackJack",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29",
      "org.scalactic" %% "scalactic" % "3.2.14",
      "org.scalatest" %% "scalatest" % "3.2.14" % Test,
      "org.scalafx" %% "scalafx" % "16.0.0-R25",
      "net.codingwell" %% "scala-guice" % "7.0.0",
      "org.scala-lang.modules" %% "scala-xml" % "2.3.0",
      "com.typesafe.play" %% "play-json" % "2.10.5"
    ),
    libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
    lazy val osName = System.getProperty("os.name") match {
      case n if n.startsWith("Linux") => "linux"
      case n if n.startsWith("Mac") => "mac"
      case n if n.startsWith("Windows") => "win"
      case _ => throw new Exception("Unknown platform!")
      }
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
    }
  )

// Add scoverage plugin settings
coverageEnabled := true
coverageFailOnMinimum := true
coverageHighlighting := true

//coverageMinimumStmtTotal := 90
//coverageMinimumBranchTotal := 90
//coverageMinimumStmtPerPackage := 90
//coverageMinimumBranchPerPackage := 85
//coverageMinimumStmtPerFile := 85
//coverageMinimumBranchPerFile := 80
