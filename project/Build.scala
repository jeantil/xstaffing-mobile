import sbt._
import PlayProject._
import Keys._

object ApplicationBuild extends Build {

  val appName = "xstaffing"
  val appVersion = "1.0"

  val appDependencies = Seq("nl.rhinofly" %% "api-ses" % "1.0")
  val exclusions = Seq(
    ExclusionRule("org.hibernate.javax.persistence","hibernate-jpa-2.0-api"),
    ExclusionRule(name="anorm_2.9.1"),
    ExclusionRule(name="ebean"),
    ExclusionRule(name="ehcache-core"),
    ExclusionRule(name="h2"),
    ExclusionRule(name="bonecp"),
    ExclusionRule(name="guava")
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
     libraryDependencies ~=  { _.map {
	  case dep if dep.name=="play"  => exclusions.foldLeft(dep){ (depWithExclusions, ex) => depWithExclusions.excludeAll(ex)}
	  case dep => dep
	}}
  ) 
 

}
