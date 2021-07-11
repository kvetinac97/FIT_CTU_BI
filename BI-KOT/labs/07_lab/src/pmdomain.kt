
data class Customer (val id: Int, val name: String, val receipts: MutableList<Receipt> = mutableListOf()) {
    override fun hashCode () = id.hashCode()
    override fun equals (other: Any?) = other is Customer && other.id == id
}
data class Receipt  (val id: Int, val desc: String, val customer: Customer)

interface CustomerDAO {
    fun clearAll ()
    fun createCustomer (name: String) : Customer
    fun createReceipt  (customerId: Int, desc: String) : Receipt
    fun removeCustomer (customerId: Int)
    fun removeReceipt  (receiptId : Int)
    fun allCustomers     () : Collection<Customer>
    fun allReceipts      () : Collection<Receipt>
    fun customerReceipts (customerId: Int) : Collection<Receipt>
}

object CustomerDAOMemory : CustomerDAO {
    // Storage
    private val cust = mutableMapOf<Int, Customer>()
    private val rcpt = mutableMapOf<Int, Receipt >()

    override fun clearAll() {
        cust.clear()
        rcpt.clear()
    }

    override fun createCustomer (name: String)
        = Customer((cust.maxBy { it.key }?.key ?: 0) + 1, name, mutableListOf()).apply { cust[id] = this }
    override fun createReceipt (customerId: Int, desc: String) : Receipt {
        val customer = cust[customerId] ?: error("Customer with ID $customerId not found")
        return Receipt( (rcpt.maxBy { it.key }?.key ?: 0) + 1, desc, customer).apply {
            rcpt[id] = this
            customer.receipts.add(this)
        }
    }

    override fun removeCustomer (customerId: Int) {
        cust.remove(customerId)
        rcpt.values.removeIf { it.customer.id == customerId }
    }

    override fun removeReceipt (receiptId: Int) : Unit
        = let { rcpt.remove(receiptId) }

    override fun allCustomers     () = cust.values
    override fun allReceipts      () = rcpt.values
    override fun customerReceipts (customerId: Int)
        = allReceipts().filter { it.customer.id == customerId }
}

// We are delegating it directly to CustomerDAOMemory, but we could also change it to database...
var daoImplementation = CustomerDaoExposed
object CustomerFacade : CustomerDAO by daoImplementation

// Tests are in test/PMDomainTest