lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      sparkVersion := "3.3.0",
      scalaVersion := "2.12.15",

      version      := "1.0-SNAPSHOT"
    )),

    sparkComponents ++= Seq("graphx", "sql", "mllib"),
    spDependencies += "graphframes/graphframes:0.8.2-spark3.2-s_2.12",

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.8" % "test",
      "org.scalafx" %% "scalafx" % "18.0.1-R28",
      "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.21",
      "info.bliki.wiki" % "bliki-core" % "3.1.0",
      "com.jfoenix" % "jfoenix" % "8.0.10",
      "com.lihaoyi" %% "upickle" % "0.9.5",
      "com.lihaoyi" %% "os-lib" % "0.9.0"

//      "org.apache.xmlgraphics" % "batik-codec" % "1.16"  // has to be imported manually as JAR
//      https://mvnrepository.com/artifact/org.apache.xmlgraphics/batik-codec/1.16
//      https://launchpad.net/gephi/+milestone/toolkit-0.8.7  // Gephi toolkit has to be imported as JAR as well
    ),

    libraryDependencies ~= {_.map(_.exclude("org.apache.logging.log4j",
      "log4j-slf4j-impl"))}
  )
