package model

import play.api.libs.json.{JsValue, Format}
import play.api.libs.json.Json.{toJson,fromJson}

case class Position(latitude:BigDecimal, longitude:BigDecimal)
case class Location(clientName:String,position:Position)

object Position {
  implicit object PositionFormat extends Format[Position]{
    def reads(json: JsValue) = {
      Position(
        BigDecimal((json\"lat").as[Double]),
        BigDecimal((json\"lng").as[Double])
      )
    }

    def writes(pos: Position) = {
      toJson(Map(
        "lat"->pos.latitude.toDouble,
        "lng"->pos.longitude.toDouble
      ))
    }
  }
}
object Location {
  import Position._
  def apply(name:String,latitude:BigDecimal, longitude:BigDecimal):Location={
    Location(name,Position(latitude,longitude))
  }
  def unapplyForm(l:Location) = {
    Some((l.clientName, l.position.latitude, l.position.longitude))
  }
  implicit object LocationFormat extends Format[Location]{
    def reads(json: JsValue) = {
      Location(
        (json \ "name").as[String],
        (json \ "position").as[Position]
      )
    }

    def writes(l: Location) = {
      toJson(
        Map(
        "name"->toJson(l.clientName),
        "position"->toJson(l.position)
        )
      )
    }
  }
}

