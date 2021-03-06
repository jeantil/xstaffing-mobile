import sbt._
import play.Project._
import Keys._

object ApplicationBuild extends Build {

  val appName = "xstaffing"
  val appVersion = "1.0"

  val appDependencies: Seq[ModuleID]=Seq(
    "com.typesafe" % "slick_2.10.0-RC1" % "0.11.2",
    "play" %% "play-jdbc" % "2.1-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    net.virtualvoid.sbt.graph.Plugin.graphSettings: _*
  )
}
