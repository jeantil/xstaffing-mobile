import sbt._
import PlayProject._
import Keys._

object ApplicationBuild extends Build {

  val appName = "restapp"
  val appVersion = "1.0"

  val appDependencies = Seq()

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
  )

}
