package external

import org.specs2.mutable.Specification
import external.ExternalRestService.plotd
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ExternalRestServiceSpec extends Specification {

  "plotd" should {
    "return 503 the first time, 200 the second time and than 503 for upcoming request" in {
      status(plotd(FakeRequest())) === SERVICE_UNAVAILABLE
      status(plotd(FakeRequest())) === OK
      status(plotd(FakeRequest())) === SERVICE_UNAVAILABLE
      status(plotd(FakeRequest())) === SERVICE_UNAVAILABLE
    }
  }
}
