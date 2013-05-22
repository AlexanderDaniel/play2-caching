# Caching of Futures in Play 2.1
ATTENTION! STILL WORK IN PROGRESS!

Simple app demonstrating the caching of futures which are returned by the [Play WS API](http://www.playframework.com/documentation/2.1.1/ScalaWS).

The [Play WS API](http://www.playframework.com/documentation/2.1.1/ScalaWS) returns [futures](http://www.scala-lang.org/api/current/index.html#scala.concurrent.Future).
We are mapping theses futures in our repositories and return a future with the mapped value.

How to introduce a caching proxy? To keep everything non-blocking it has to return a future as well.
But can we cache the future? Well, the future is just an object which is not bound to any thread.

The straight-forward implementation of a caching proxy would look like this:
<script src="https://gist.github.com/AlexanderDaniel/5628015.js"></script>

But it would also cache a error response of the external service, e.g. `503/ServiceUnavailable`. We do not want to cache
that because the external service might be back to normal very soon. I.e. we only want to cache successful
responses of the external service.

<script src="https://gist.github.com/AlexanderDaniel/5628079.js"></script>

You can see the implementation at https://github.com/AlexanderDaniel/play2-caching/blob/master/app/repositories/CachingRepo.scala

The Play caching API

Default ehcache.xml of Play
https://github.com/playframework/Play20/blob/master/framework/src/play/src/main/resources/ehcache.xml

http://en.wikipedia.org/wiki/Domain-driven_design

http://www.playframework.com/documentation/api/2.1.1/scala/index.html#play.api.libs.ws.WS$