package model

import play.api.libs.json.{JsSuccess, JsValue, Format}
import play.api.libs.json.Json

case class Position(latitude:BigDecimal, longitude:BigDecimal)
case class Location(clientName:String,position:Position)

object Position {
  implicit object PositionFormat extends Format[Position]{
    def reads(json: JsValue) = {
      JsSuccess(Position(
        BigDecimal((json\"lat").as[Double]),
        BigDecimal((json\"lng").as[Double])
      ))
    }

    def writes(pos: Position) = {
      Json.obj(
        "lat"->pos.latitude.toDouble,
        "lng"->pos.longitude.toDouble
      )
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
      JsSuccess(Location(
        (json \ "name").as[String],
        (json \ "position").as[Position]
      ))
    }

    def writes(l: Location) = {
      Json.obj(
        "name"->l.clientName,
        "position"->l.position
      )
    }
  }
}

