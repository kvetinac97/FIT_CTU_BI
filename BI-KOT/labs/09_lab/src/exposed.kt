import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

// IntIdTable has implicit id autogenerate primary key
object CustomerTable : IntIdTable() {
    val name = varchar("name", 64)
}

object ReceiptTable : IntIdTable() {
    val description = varchar  ("description", 64)
    val customer    = reference("customer_id", CustomerTable)
}

object CustomerDaoExposed : CustomerDAO {

    init {
        Database.connect("jdbc:sqlite:c09/pmdomain.sqlite", driver = "org.sqlite.SQLiteJDBCLoader")
        transaction { SchemaUtils.create(CustomerTable, ReceiptTable) }
    }

    private fun getCustomerByID (customerId: Int) = transaction {
        CustomerTable.select { CustomerTable.id eq customerId }.singleOrNull()?.let { Customer(customerId, it[CustomerTable.name]) }
    }

    override fun clearAll () : Unit = transaction {
        CustomerTable.deleteAll()
        ReceiptTable.deleteAll()
    }

    override fun allCustomers () = transaction {
        CustomerTable.selectAll().map {
            val customerId = it[CustomerTable.id].value
            val rcpts = customerReceipts(customerId)
            Customer (customerId, it[CustomerTable.name], rcpts.toMutableList())
        }
    }

    override fun allReceipts() : Collection<Receipt> = transaction {
        ReceiptTable.selectAll().map {
            val customer = getCustomerByID(it[ReceiptTable.customer].value) ?: return@map null
            Receipt (it[ReceiptTable.id].value, it[ReceiptTable.description], customer)
        }.filterNotNull()
    }

    override fun createCustomer (name: String) = transaction {
        val cstId = CustomerTable.insertAndGetId { it[CustomerTable.name] = name }.value
        Customer(cstId, name, mutableListOf())
    }

    override fun createReceipt (customerId: Int, desc: String) = transaction {
        val cst = getCustomerByID(customerId) ?: error("Customer with id $customerId not found")
        val rcptId = ReceiptTable.insertAndGetId {
            it[description] = desc
            it[customer]    = customerId
        }.value
        Receipt(rcptId, desc, cst)
    }

    override fun customerReceipts (customerId: Int) = transaction {
        val customer = getCustomerByID(customerId) ?: return@transaction emptyList<Receipt>()
        ReceiptTable.select { ReceiptTable.customer eq customer.id }.map {
            Receipt (it[ReceiptTable.id].value, it[ReceiptTable.description], customer )
        }
    }

    override fun removeCustomer (customerId: Int) : Unit = transaction {
        CustomerTable.deleteWhere { CustomerTable.id eq customerId }
    }

    override fun removeReceipt (receiptId: Int) : Unit = transaction {
        ReceiptTable.deleteWhere { ReceiptTable.id eq receiptId }
    }

}
