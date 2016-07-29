name := """pdfviewer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  "net.iharder" % "base64" % "2.3.8",
   
  
  javaWs,
  "net.logstash.logback" % "logstash-logback-encoder" % "3.4",
  "net.iharder" % "base64" % "2.3.8",

"com.couchbase.client" % "java-client" % "2.2.5"
)
	
// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
