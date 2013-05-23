package external

import play.api.mvc._
import play.api.libs.json.JsString
import java.util.concurrent.atomic.{AtomicInteger, AtomicReference}

object ExternalRestService extends Controller {

  private val numberOfRequest = new AtomicInteger(0)

  /** Programming Language Of The Day */
  def plotd = Action {
    if (numberOfRequest.incrementAndGet()>=2)
      Ok(JsString("Scala"))
    else
      ServiceUnavailable
  }

}
