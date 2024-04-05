val scala3Version = "3.4.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "GameProject",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" ,
      "org.scalafx" %% "scalafx" % "16.0.0-R24"
      // Add other dependencies as needed
    )
    
  )
