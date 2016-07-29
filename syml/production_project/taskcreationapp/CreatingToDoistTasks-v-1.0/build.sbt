name := """ToDoistCreatingTasks"""


version := "1.0-SNAPSHOT"

checksums in update := Nil

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.couchbase.client" % "couchbase-client" % "1.4.11",
  "org.json" % "json" % "20140107",
  "com.debortoliwines.openerp" % "openerp-java-api" % "1.5.0",
  "javax.mail" % "mail" % "1.4",
    "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.freemarker" % "freemarker" % "2.3.20",
  "org.codehaus.jettison" % "jettison" % "1.1",
    "org.slf4j" % "slf4j-log4j12" % "1.7.12",
  "org.fluentd" % "fluent-logger" % "0.3.1",
  "ch.qos.logback" % "logback-classic" % "1.0.7",
  "com.sndyuk" % "logback-more-appenders" % "1.1.0",
  "net.iharder" % "base64" % "2.3.8",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "com.couchbase.client" % "java-client" % "2.1.6",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13"
  )
   

resolvers += "xsalefter maven" at "https://raw.githubusercontent.com/xsalefter/xsalefter-maven-repo/master/releases"

resolvers += "Logback more appenders" at "http://sndyuk.github.com/maven"


