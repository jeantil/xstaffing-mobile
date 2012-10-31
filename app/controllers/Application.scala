package controllers

import play.api._
import play.api.Play.current
import libs.concurrent.{Thrown, Redeemed}
import libs.openid.OpenID
import play.api.mvc._
import play.mvc.Http

/**
 * Provide security features
 */
trait Secured {
  val REQUIRED_ATTRIBUTES=Seq(
    "email" -> "http://schema.openid.net/contact/email"
  )
  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = {
    Logger.info("Unauthorized access to "+request.uri+" , redirecting to "+routes.Application.authenticate())
    Results.Redirect(routes.Application.authenticate())
  }

  // ---

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result): Action[(Action[AnyContent], AnyContent)] = Security.Authenticated(username, onUnauthorized) {
    userId =>
      Action({
        request =>
          Logger.info("Authorized access to " + request.uri + " , for user " + userId)
          f(userId)(request)
      })
  }

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated[A](parser: BodyParser[A])(f: => String => Request[A] => Result) = Security.Authenticated(username, onUnauthorized) {
    user =>
      Action(parser)(request => f(user)(request))
  }
}

trait XStaffing {
  this: Controller with Secured=>
  def index = IsAuthenticated { userid => controllers.Assets.at("/public","index.html") }

  def authenticate = Action { implicit request =>
    Logger.debug("in authenticate")
    AsyncResult(
      OpenID.redirectURL(
        "https://www.google.com/accounts/o8/id",
        routes.Application.openIDCallback().absoluteURL(),
        REQUIRED_ATTRIBUTES
      ).extend(_.value match {
        case Redeemed(url) =>
          Logger.debug("authenticate redirecting to "+url)
          Redirect(url)

        case Thrown(throwable) =>
          Logger.error("authenticate impossible d'authentifier avec openid",throwable)
          Unauthorized("")
      }
      )
    )
  }
  def openIDCallback = Action { implicit request =>
    AsyncResult(
      OpenID.verifiedId.extend( _.value match {
        case Redeemed(info) => Redirect(routes.Application.index()).withSession(("email",info.attributes.get("email").get))
        case Thrown(throwable) => Unauthorized("")
      })
    )
  }
}

object Application extends Controller with Secured with XStaffing



