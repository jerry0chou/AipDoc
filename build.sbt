val ScalatraVersion = "2.6.5"

name := "ApiDoc"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % "3.2.3",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
    "org.xerial" % "sqlite-jdbc" % "3.25.2",
    "com.typesafe.slick" %% "slick-codegen" % "3.2.3",


    "org.scalatra" %% "scalatra" % ScalatraVersion,
    "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",

    "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
    "org.eclipse.jetty" % "jetty-webapp" % "9.4.19.v20190610" % "container;compile",
    "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",

    "com.mchange" % "c3p0" % "0.9.5.2",

    "org.scalatra" %% "scalatra-json" % ScalatraVersion,
    "org.json4s"   %% "json4s-jackson" % "3.5.3",
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)