package external

import org.specs2.mutable.Specification
import external.ExternalRestService.plotd
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ExternalRestServiceSpec extends Specification {

  "plotd" should {
    "return 503 the first time and then 200 for upcoming request" in {
      status(plotd(FakeRequest())) === SERVICE_UNAVAILABLE
      status(plotd(FakeRequest())) === OK
      status(plotd(FakeRequest())) === OK
      status(plotd(FakeRequest())) === OK
    }
  }
}
