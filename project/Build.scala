import sbt._
import play.Project._
import Keys._

object ApplicationBuild extends Build {

  val appName = "xstaffing"
  val appVersion = "1.0"

  val appDependencies: Seq[ModuleID]=Seq()

  val main = play.Project(appName, appVersion, appDependencies).settings(
    net.virtualvoid.sbt.graph.Plugin.graphSettings: _*
  )
}
