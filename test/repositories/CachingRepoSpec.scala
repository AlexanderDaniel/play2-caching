package repositories

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import scala.concurrent.Future
import play.api.test.Helpers._
import play.api.test.WithApplication
import play.api.cache.Cache
import scala.concurrent.ExecutionContext.Implicits.global

class CachingRepoSpec extends Specification with Mockito {

  "plotd with empty cache" should {
    "query the repo, add the result in the cache and return the cached value" in new WithApplication {
      // given
      val repo = mock[Repo]
      repo.plotd returns Future("Scala")
      val cachingRepo = new CachingRepo(repo)

      // when
      await(cachingRepo.plotd) === "Scala"

      // then
      await(Cache.getAs[Future[String]](CacheKey.plotd).get) === "Scala"
      await(cachingRepo.plotd) === "Scala" // second call
      there was one(repo).plotd
      noMoreCallsTo(repo)
    }
    "just query the repo if an exception is thrown" in new WithApplication {
      // given
      val repo = mock[Repo]
      repo.plotd returns Future(throw new RuntimeException)
      val cachingRepo = new CachingRepo(repo)

      // when
      await(cachingRepo.plotd) must throwA[RuntimeException]

      // then
      Cache.get(CacheKey.plotd) === None
      there was one(repo).plotd
      noMoreCallsTo(repo)
    }
  }
  "plotd with filled cache" should {
    "return the cached value" in new WithApplication {
      // given
      val repo = mock[Repo]
      val cachingRepo = new CachingRepo(repo)
      Cache.set(CacheKey.plotd, Future("CoffeeScript"))

      // when
      await(cachingRepo.plotd) === "CoffeeScript"

      // then
      noMoreCallsTo(repo)
    }
  }

}
