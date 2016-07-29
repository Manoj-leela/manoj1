name := """visdomVideoViewer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

checksums := Seq("")

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
    "com.couchbase.client" % "couchbase-client" % "1.4.9",
      "org.codehaus.jettison" % "jettison" % "1.1",
     "com.debortoliwines.openerp" % "openerp-java-api" % "1.5.0",
       "org.slf4j" % "slf4j-log4j12" % "1.7.12",
    "org.fluentd" % "fluent-logger" % "0.3.1",
 	"ch.qos.logback" % "logback-classic" % "1.0.7",
   "com.sndyuk" % "logback-more-appenders" % "1.1.0"
     
)


resolvers += "xsalefter maven" at "https://raw.githubusercontent.com/xsalefter/xsalefter-maven-repo/master/releases"

resolvers += "Logback more appenders" at "http://sndyuk.github.com/maven"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.

