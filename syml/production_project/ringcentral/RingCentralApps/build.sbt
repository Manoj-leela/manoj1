name := """RingCentralApps"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)
scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  "com.google.code.gson" % "gson" % "2.2.4",
  "com.couchbase.client" % "couchbase-client" % "1.4.8",
  "org.json" % "json" % "20140107",
  "com.jayway.jsonpath" % "json-path" % "2.0.0",
  javaWs,
  "net.logstash.logback" % "logstash-logback-encoder" % "3.4",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "junit" % "junit" % "4.11",
  "org.apache.httpcomponents" % "httpclient" % "4.5",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.4.3",
  "com.squareup.okhttp" % "okhttp" % "2.5.0"
  )
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
      //resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
