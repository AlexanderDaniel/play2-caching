package futures

import org.specs2.mutable.Specification
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Code demonstrating that it is perfectly valid to throw exceptions in futures because
 * they are caught by the framework and converted to a [[scala.util.Failure]].
 *
 * I wrote this piece of code when writing the response to the
 * [[http://www.dzone.com/links/caching_of_futures_in_play_21_and_scala.html#289487 comment]]
 * of [[http://www.dzone.com/links/users/profile/266330.html Slim Ouertani]] at
 * [[http://www.dzone.com/links/caching_of_futures_in_play_21_and_scala.html DZone]].
 */
class FuturePlaygroundSpec extends Specification {

  "value of future" should {
    "capture success" in {
      val future = Future("tst")
      valueOfReadyFuture(future) === Success("tst")
    }
    "capture failure when an exception is thrown" in {
      val exception = new RuntimeException("exception thrown inside future")
      val future = Future(throw exception)
      valueOfReadyFuture(future) === Failure(exception)
    }
  }

  "returning a Try[T] inside of the future" should {
    "yield a Try[Try[T]]" in {
      pending
    }
  }

  private def valueOfReadyFuture[T](future: Future[T]): Try[T] = {
    Await.ready(future, Duration.Inf)
    future.value.get
  }
}
