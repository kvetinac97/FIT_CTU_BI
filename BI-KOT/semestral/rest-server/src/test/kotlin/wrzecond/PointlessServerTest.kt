package wrzecond

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PointlessServerTest : StringSpec ({
    "Pointless test" {
        42 shouldBe 21 * 2
    }
})
