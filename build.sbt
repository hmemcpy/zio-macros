import sbtcrossproject.CrossPlugin.autoImport.crossProject
import BuildHelper._
import xerial.sbt.Sonatype._

inThisBuild(
  List(
    name := "zio-macros",
    organization := "dev.zio",
    homepage := Some(url("https://github.com/zio/zio-macros")),
    developers := List(
      Developer(
        "mschuwalow",
        "Maxim Schuwalow",
        "maxim.schuwalow@gmail.com",
        url("https://github.com/mschuwalow")
      ),
      Developer(
        "ioleo",
        "Piotr Gołębiewski",
        "ioleo+zio@protonmail.com",
        url("https://github.com/ioleo")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        homepage.value.get,
        "scm:git:git@github.com:zio/zio-macros.git"
      )
    ),
    licenses := Seq("Apache-2.0" -> url(s"${scmInfo.value.map(_.browseUrl).get}/blob/v${version.value}/LICENSE")),
    pgpPublicRing := file("/tmp/public.asc"),
    pgpSecretRing := file("/tmp/secret.asc"),
    releaseEarlyWith := SonatypePublisher
  )
)

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val root = project
  .in(file("."))
  .settings(
    skip in publish := true
  )
  .aggregate(
    mock.jvm,
    mock.js,
    mockExamples.jvm,
    mockExamples.js
  )
  .enablePlugins(ScalaJSPlugin)

lazy val mock = crossProject(JSPlatform, JVMPlatform)
  .in(file("mock"))
  .settings(stdSettings("zio-macros-mock"))
  .settings(
    scalacOptions --= Seq("-deprecation", "-Xfatal-warnings")
  )
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio"        %% "zio-test"       % "1.0.0-RC12-1+121-67be0f99+20190923-1143",
      "org.scala-lang" %  "scala-reflect"  % scalaVersion.value % "provided",
      "org.scala-lang" %  "scala-compiler" % scalaVersion.value % "provided"
    )
  )

lazy val mockExamples = crossProject(JSPlatform, JVMPlatform)
  .in(file("mock-examples"))
  .dependsOn(mock)
  .settings(stdSettings("zio-macros-mock-examples"))
  .settings(
    skip in publish := true,
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.0-RC12-1+121-67be0f99+20190922-1210"
    )
  )
