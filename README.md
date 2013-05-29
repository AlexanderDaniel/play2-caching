# Caching of Futures in Play 2.1
The [Play WS API](http://www.playframework.com/documentation/2.1.1/ScalaWS) returns [futures](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future). We are mapping theses futures in our [repositories](https://en.wikipedia.org/wiki/Domain-driven_design) and return a future with the mapped value.

How to introduce a caching proxy with the [Play caching API](http://www.playframework.com/documentation/2.1.1/ScalaCache)? To keep everything non-blocking it has to return a future as well. But can we cache the future? Well, YES! The future is just an object which is not bound to any thread.

The straight-forward implementation of a caching proxy would look like this:

    class NaiveCachingRepo(repo: Repo) extends Repo {
      def plotd: Future[String] =
        Cache.getOrElse(CacheKey.plotd) {
          repo.plotd
        }
    }

But it would also cache a error response of the external service, e.g. `503/ServiceUnavailable`. We do not want to cache that because the external service might be back to normal very soon. I.e. we only want to cache successful responses of the external service:

    class CachingRepo(repo: Repo) extends Repo {
      def plotd: Future[String] = Cache.getAs[Future[String]](CacheKey.plotd) match {
        case Some(value) => value
        case None => repo.plotd map { valueFromRepo =>
          Cache.set(CacheKey.plotd, Future(valueFromRepo))
          valueFromRepo
        }
      }
    }

Here we check whether there is already an value with the cache key available in the cache. If not we query the repo and map the result to fill the cache in the successful case.

A working sample is available at [GitHub](https://github.com/AlexanderDaniel/play2-caching/tree/blogPost1):

* The [simulation of the external service](https://github.com/AlexanderDaniel/play2-caching/blob/blogPost1/app/external/ExternalRestService.scala) with it's [unit tests](https://github.com/AlexanderDaniel/play2-caching/blob/blogPost1/test/external/ExternalRestServiceSpec.scala)
* The [repo trait](https://github.com/AlexanderDaniel/play2-caching/blob/blogPost1/app/repositories/Repo.scala), the [implementation](https://github.com/AlexanderDaniel/play2-caching/blob/blogPost1/app/repositories/SimpleRepo.scala), and the [cache](https://github.com/AlexanderDaniel/play2-caching/blob/blogPost1/app/repositories/CachingRepo.scala) with it's [unit tests](https://github.com/AlexanderDaniel/play2-caching/blob/blogPost1/test/repositories/CachingRepoSpec.scala)

If you want to play around just clone the [repo](https://github.com/AlexanderDaniel/play2-caching/tree/blogPost1)!

The [Play caching API](http://www.playframework.com/documentation/2.1.1/ScalaCache) is implemented as a [plugin](https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/Plugins.scala) which uses [Ehcache](http://ehcache.org) under the hood. Interested in the [code](https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/scala/play/api/cache/Cache.scala) or the default [ehcache.xml](https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/resources/ehcache.xml) of Play?
