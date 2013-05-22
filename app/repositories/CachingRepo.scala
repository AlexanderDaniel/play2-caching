package repositories

import scala.concurrent.Future
import play.api.cache.Cache
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

class CachingRepo(repo: Repo) extends Repo {

  def plotd: Future[String] = Cache.getAs[Future[String]](CacheKey.plotd) match {
    case Some(value) => value
    case None => repo.plotd map { valueFromRepo =>
      Cache.set(CacheKey.plotd, Future(valueFromRepo))
      valueFromRepo
    }
  }

}

object CacheKey {
  val plotd = "plotd"
}