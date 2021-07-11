import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.*

class PMDomainTest : StringSpec({
    "Basic test" {
        val dao = CustomerFacade
        dao.clearAll()
        dao.allCustomers() shouldHaveSize 0
        dao.allReceipts()  shouldHaveSize 0
        val john1 = dao.createCustomer("John")
        dao.allCustomers() shouldContain john1
        dao.customerReceipts(john1.id) shouldHaveSize 0
        val pepa  = dao.createCustomer("Pepa")
        val john2 = dao.createCustomer("John")
        dao.allCustomers() shouldContainExactly listOf(john1, pepa, john2)
        println(dao.allCustomers())
        val rc1 = dao.createReceipt(john1.id, "Lenovo G50")
        dao.allReceipts() shouldContain rc1
        dao.customerReceipts(john1.id) shouldContain rc1
        dao.customerReceipts(john2.id) shouldNotContain rc1
        val rc2 = dao.createReceipt(john2.id, "MacBook Pro")
        val rc3 = dao.createReceipt(john2.id, "iPhone 4s")
        dao.allReceipts() shouldHaveSize 3
        dao.allReceipts() shouldContainExactly listOf(rc1, rc2, rc3)
        println(dao.allReceipts())
        dao.customerReceipts(john1.id) shouldHaveSize 1
        dao.customerReceipts(pepa.id) shouldHaveSize 0
        dao.customerReceipts(john2.id) shouldHaveSize 2
        dao.customerReceipts(john2.id) shouldContainExactly listOf(rc2, rc3)
        shouldThrow<IllegalStateException> { dao.createReceipt(-4523456, "") }
    }
})