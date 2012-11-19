package controllers



import play.api._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits._
import libs.openid.{UserInfo, OpenID}
import play.api.mvc._
import play.api.mvc.Results._
import concurrent.Future
import scala.util.{Try,Success,Failure}

/**
 * Provide security features
 */
trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = {
    Logger.info("Unauthorized access to "+request.uri+" , redirecting to "+routes.Application.authenticate())

    Redirect(routes.Application.authenticate())
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
  def index = IsAuthenticated { userid => implicit request => controllers.Assets.at("/public","index.html")(request) }

  val REQUIRED_ATTRIBUTES=Seq(
    "email" -> "http://schema.openid.net/contact/email"
  )

  def authenticate = Action { implicit request =>
      val openIdCallbackUrl: String = routes.Application.openIDCallback().absoluteURL()
      def onRedirected(promise: NotWaiting[String]): Result = {
        promise match {
          case Redeemed(url) => Redirect(url)
          case Thrown(throwable) => Unauthorized("Unable to verify your openid provider.<br>"+throwable.getMessage)
        }
      }
      AsyncResult(
        OpenID.redirectURL("https://www.google.com/accounts/o8/id", openIdCallbackUrl, REQUIRED_ATTRIBUTES).extend1(onRedirected)
      )
  }
  def openIDCallback = Action { implicit request =>
    def onVerified(promise:NotWaiting[UserInfo]):Result ={
      promise match {
        case Redeemed(info) => Redirect(routes.Application.index()).withSession(("email",info.attributes.get("email").get))
        case Thrown(throwable) => Unauthorized("Authorization refused by your openid provider<br>"+throwable.getMessage)
      }
    }
    AsyncResult(
      OpenID.verifiedId.extend1(onVerified)
    )
  }
  def logout = Action { implicit request =>
    Redirect(routes.Application.index()).withNewSession
  }
}

object Application extends Controller with Secured with XStaffing



