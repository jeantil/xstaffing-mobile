package model


import play.api.libs.json.{JsSuccess, JsValue, Format}
import play.api.libs.json.Json
import play.Logger
import scala.StringContext
import org.h2.jdbc.JdbcSQLException
import slick.jdbc.GetResult

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

  def insert(location:Location, userId:String):Location=DAO.insert(location,userId)
  def findByUserId(userId:String):Seq[Location]=DAO.findByUserId(userId)

  object DAO {
    import play.api.Application
    import play.api.Play.current
    import play.api.db.DB
    import scala.slick.session.Database
    import Database.threadLocalSession
    import scala.slick.jdbc.{GetResult, StaticQuery => Q}
    import Q.interpolation

    def insert(location:Location, userId:String):Location={
      Database.forDataSource(DB.getDataSource()) withSession {
        try{
          val statement=sqlu"""
            insert into locations
            values ($userId,${location.clientName},${location.position.latitude},${location.position.longitude})
            """
            statement.execute()
        }catch{
          case e:JdbcSQLException=>Logger.info(s"Unable to persist location($userId,${location.clientName},${location.position.latitude},${location.position.longitude}), location already exists")
          case e=>Logger.warn(s"Unable to persist location($userId,${location.clientName},${location.position.latitude},${location.position.longitude})",e)
        }
      }
      location
    }
    def findByUserId(userId:String):Seq[Location]={
      implicit val getLocationResult = GetResult(r => Location( r.<<, Position(r.<<, r.<<)))
      Database.forDataSource(DB.getDataSource()) withSession {
        val statement=sql"select clientName, latitude, longitude from locations where userId=$userId".as[Location]
        statement.list()
      }
    }
  }
}

