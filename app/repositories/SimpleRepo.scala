package repositories

import scala.concurrent.Future
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Logger

class SimpleRepo(url: String) extends Repo {

  def plotd: Future[String] =
    WS.url(url).get() map { response =>
      Logger.info(s"GET $url returned ${response.status}")
      response.status match {
        case 200 => response.json.toString()
        case _ => throw new RuntimeException("Problem with external service")
      }
    }

}
