import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class RationalTest : StringSpec ({
    "Basic test" {
        Rational(1).toString() shouldBe "1/1"
        Rational(2, 3).toString() shouldBe "2/3"
        Rational(4, 2).norm.toString() shouldBe "2/1"
        Rational(1).inverse() shouldBe Rational(1)
        Rational(3, 5).inverse() shouldBe Rational(100, 60)
    }
    "Operator test" {
        !3 + 5 shouldBe !8
        !1/2 + !1/2 shouldBe !1
        !3/2 * !2/3 shouldBe !1
        ( (!4/3 - !2/3) + !1/3 ) * !4 shouldBe !4
    }
})