package repositories

import scala.concurrent.Future

/**
 * Programming Language Of The Day repository
 */
trait Repo {
  def plotd: Future[String]
}
