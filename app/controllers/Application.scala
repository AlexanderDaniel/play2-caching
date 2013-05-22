package controllers

import play.api.mvc._
import repositories.{CachingRepo, SimpleRepo}
import play.api.libs.concurrent.Execution.Implicits._

object Application extends Controller {

  val repo = new CachingRepo(new SimpleRepo("http://localhost:9000/external/plotd"))

  def index = Action {
    Ok(views.html.index("plotd • programming language of the day"))
  }

  def plotd = Action {
    Async {
      repo.plotd.map {
        Ok(_)
      }.recover {
        case _ => InternalServerError("Sorry. There was a problem. Please try again!")
      }
    }
  }

}