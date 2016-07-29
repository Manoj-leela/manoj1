name := """automateCreditApp"""

version := "1.0-SNAPSHOT"

checksums in update := Nil

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
   "com.couchbase.client" % "couchbase-client" % "1.4.8",
     "com.debortoliwines.openerp" % "openerp-java-api" % "1.5.0",
      "org.json" % "json" % "20140107",
 		 "com.sendwithus" % "java-client" % "1.6.0",
 		 "com.google.code.gson" % "gson" % "2.5",
 		 "org.slf4j" % "slf4j-log4j12" % "1.7.12",
     "org.fluentd" % "fluent-logger" % "0.3.1",
 	 "ch.qos.logback" % "logback-classic" % "1.0.7",
     "com.sndyuk" % "logback-more-appenders" % "1.1.0"
)

resolvers += "repo" at "https://github.com/sendwithus/sendwithus-mvn-repo/raw/master/releases"

resolvers += "xsalefter maven" at "https://raw.githubusercontent.com/xsalefter/xsalefter-maven-repo/master/releases"

resolvers += "Logback more appenders" at "http://sndyuk.github.com/maven"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
//routesGenerator := InjectedRoutesGenerator
