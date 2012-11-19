package controllers

import play.api.mvc.Controller
import play.api.data._
import format.Formatter
import play.api.data.Forms._
import play.api.data.format.Formats._
import validation.Constraint
import model.Location
import play.api.libs.json.Json.toJson
import play.libs.Akka


trait Locations {
  this: Controller with Secured =>

  implicit val bigDecimalFormat = new Formatter[BigDecimal] {
    override val format = Some("format.numeric", Nil)

    def bind(key: String, data: Map[String, String]) = {
      stringFormat.bind(key, data).right.flatMap {
        s =>
          scala.util.control.Exception.allCatch[BigDecimal]
            .either(BigDecimal.apply(s))
            .left.map(e => Seq(FormError(key, "error.number", Nil)))
      }
    }

    def unbind(key: String, value: BigDecimal) = Map(key -> value.toString)
  }

  case class ConstMapping[T](key: String, value: T, constraints: Seq[Constraint[T]] = Nil) extends Mapping[T] {
    def bind(data: Map[String, String]) = Right(value)

    val mappings: Seq[Mapping[_]] = Seq(this)

    def unbind(value: T) = null

    def verifying(addConstraints: Constraint[T]*): Mapping[T] = {
      this.copy(constraints = constraints ++ addConstraints.toSeq)
    }

    def withPrefix(prefix: String): Mapping[T] = {
      addPrefix(prefix).map(newKey => this.copy(key = newKey)).getOrElse(this)
    }
  }

  val locationForm = {
    userId: String => Form(
      mapping(
        "clientName" -> text,
        "latitude" -> of[BigDecimal],
        "longitude" -> of[BigDecimal]
      )(Location.apply)(Location.unapplyForm)
    )
  }

  val cache = scala.collection.mutable.Map[String, Map[String, Location]]()

  def index = IsAuthenticated {
    userId => implicit request => {
      def userLocationsFromDB(userId:String):Map[String,Location]={
        Location.findByUserId(userId).map {l:Location=>(l.clientName,l)}.toMap
      }
      val userLocations:Iterable[Location] = cache.get(userId) match {
        case Some(loaded) => loaded.values
        case None => {
          val loaded: Map[String, Location] = userLocationsFromDB(userId)
          cache.update(userId, loaded)
          loaded.values
        }
      }
      Ok(toJson(userLocations.toSeq))
    }
  }

  def create = IsAuthenticated {
    userId => implicit request =>
      locationForm(userId).bindFromRequest().fold(
        formWithErrors => BadRequest("missing parameter"),
        value => {
          val clientName = value.clientName
          val current: Map[String, Location] = cache.getOrElse(userId, Map())
          cache.update(userId, current.updated(clientName, value))
          Location.insert(value, userId)
          Redirect(routes.Application.index())
        }
      )
  }
}

object Locations extends Controller with Secured with Locations