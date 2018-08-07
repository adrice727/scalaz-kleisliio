import Scalaz._

organization in ThisBuild := "org.scalaz"

version in ThisBuild := "0.1.0-SNAPSHOT"

publishTo in ThisBuild := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots".at(nexus + "content/repositories/snapshots"))
  else
    Some("releases".at(nexus + "service/local/staging/deploy/maven2"))
}

dynverSonatypeSnapshots in ThisBuild := true

lazy val sonataCredentials = for {
  username <- sys.env.get("SONATYPE_USERNAME")
  password <- sys.env.get("SONATYPE_PASSWORD")
} yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)

credentials in ThisBuild ++= sonataCredentials.toSeq

lazy val scalazDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.25",
  "org.scalaz" %% "scalaz-zio"  % "0.1-SNAPSHOT"
)

lazy val core = project
  .settings(
    // In the repl most warnings are useless or worse.
    // This is intentionally := as it's more direct to enumerate the few
    // options we do want than to try to subtract off the ones we don't.
    // One of -Ydelambdafy:inline or -Yrepl-class-based must be given to
    // avoid deadlocking on parallel operations, see
    //   https://issues.scala-lang.org/browse/SI-9076
    scalacOptions in Compile in console := Seq(
      "-Ypartial-unification",
      "-language:higherKinds",
      "-language:existentials",
      "-Yno-adapted-args",
      "-Xsource:2.13",
      "-Yrepl-class-based"
    ),
    resolvers += Resolver.sonatypeRepo("snapshots"),
    initialCommands in Compile in console := """
                                               |import scalaz._
                                               |import scalaz.zio._
                                               |object replRTS extends RTS {}
                                               |import replRTS._
                                             """.stripMargin,
    libraryDependencies ++= {
      val prod = Seq() ++ scalazDependencies

      val test = Seq(
        "org.specs2" %% "specs2-core"          % "4.3.2",
        "org.specs2" %% "specs2-scalacheck"    % "4.3.2",
        "org.specs2" %% "specs2-matcher-extra" % "4.3.2"
      ).map(_ % Test)

      prod ++ test
    }
  )

lazy val benchmarks = project
  .dependsOn(core)
  .enablePlugins(JmhPlugin)
  .settings(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    skip in publish := true,
    libraryDependencies ++=
      Seq(
        "org.scala-lang" % "scala-reflect"  % scalaVersion.value,
        "org.scala-lang" % "scala-compiler" % scalaVersion.value % Provided,
        "io.monix"       %% "monix"         % "3.0.0-RC1",
        "org.typelevel"  %% "cats-effect"   % "1.0.0-RC2"
      ) ++ scalazDependencies
  )

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val root =
  (project in file("."))
    .settings(stdSettings("kleisliio"))
    .aggregate(core, benchmarks)
