name := "spray-json"

version := "1.2.7-NAP"

organization := "com.netaporter"

organizationHomepage := Some(new URL("http://spray.io"))

description := "A Scala library for easy and idiomatic JSON (de)serialization"

homepage := Some(new URL("https://github.com/spray/spray-json"))

startYear := Some(2011)

licenses := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))

scalaVersion := "2.11.0"

scalacOptions <<= scalaVersion map {
  case "2.9.3"  => Seq("-unchecked", "-deprecation", "-encoding", "utf8")
  case _ => Seq("-feature", "-language:implicitConversions", "-unchecked", "-deprecation", "-encoding", "utf8")
}

resolvers += Opts.resolver.sonatypeReleases

libraryDependencies ++= {
  Seq("org.parboiled" %% "parboiled-scala" % "1.1.6" % "compile") ++
  (scalaVersion.value match {
    case "2.9.3"  =>
      Seq(
        "org.specs2" %% "specs2" % "1.12.4.1" % "test",
        "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
      )
    // Scala 2.10 and Scala 2.11
    case _ =>
      Seq(
        "org.specs2" %% "specs2" % "2.3.11" % "test",
        "org.scalacheck" %% "scalacheck" % "1.11.3" % "test"
      )
  })
}

(scalacOptions in doc) <<= (name, version).map { (n, v) => Seq("-doc-title", n + " " + v) }

// generate boilerplate
Boilerplate.settings

// OSGi settings
osgiSettings

OsgiKeys.exportPackage := Seq("""spray.json.*;version="${Bundle-Version}"""")

OsgiKeys.importPackage <<= scalaVersion { sv => Seq("""scala.*;version="$<range;[==,=+);%s>"""".format(sv)) }

OsgiKeys.importPackage ++= Seq("""spray.json;version="${Bundle-Version}"""", "*")

OsgiKeys.additionalHeaders := Map("-removeheaders" -> "Include-Resource,Private-Package")

///////////////
// publishing
///////////////

crossScalaVersions := Seq("2.9.3", "2.10.4", "2.11.0")

scalaBinaryVersion <<= scalaVersion(sV => if (CrossVersion.isStable(sV)) CrossVersion.binaryScalaVersion(sV) else sV)

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <scm>
    <url>git@github.com:net-a-porter/scala-uri.git</url>
    <connection>scm:git@github.com:net-a-porter/scala-uri.git</connection>
  </scm>
  <developers>
    <developer>
      <id>Mathias</id>
      <name>Mathias Doenitz</name>
      <url>https://github.com/sirthias</url>
    </developer>
    <developer>
      <id>Johannes</id>
      <name>Johannes Rudolph</name>
      <url>https://github.com/jrudolph</url>
    </developer>
  </developers>)


///////////////
// ls-sbt
///////////////

seq(lsSettings:_*)

(LsKeys.tags in LsKeys.lsync) := Seq("json")

(LsKeys.docsUrl in LsKeys.lsync) := Some(new URL("http://spray.github.com/spray/api/spray-json/"))

(externalResolvers in LsKeys.lsync) := Seq("spray repo" at "http://repo.spray.io")
