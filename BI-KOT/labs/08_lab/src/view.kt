import tornadofx.*

class CustomerView : View ("Customers") {
    private val customerList = CustomerFacade.allCustomers().toMutableList().asObservable()
    override val root =
        titledpane("Customers") {
            tableview(customerList) {
                collapsibleProperty().set(false)
                readonlyColumn("ID", Customer::id)
                readonlyColumn("Name", Customer::name)
            }
            .apply { onSelectionChange { find<ReceiptView>().refresh(it) } }
            .apply {
                contextmenu {
                    item ("New Receipt") {
                        action {
                            find<AddReceiptDialog>(mapOf("customer" to selectedItem)).openModal()
                        }
                    }.enableWhen { selectionModel.selectedItemProperty().isNotNull }
                    item ("Delete customer") {
                        action {
                            CustomerFacade.removeCustomer((selectedItem as Customer).id)
                            customerList.remove(selectedItem)
                        }
                    }.enableWhen { selectionModel.selectedItemProperty().isNotNull }
                }
            }
        }
    fun refresh() { customerList.setAll(CustomerFacade.allCustomers()) }
}

class ReceiptView : View("Receipts") {
    private val receiptList = mutableListOf<Receipt>().asObservable()
    override val root = titledpane("Receipts") {
        tableview(receiptList) {
            collapsibleProperty().set(false)
            readonlyColumn("ID", Receipt::id)
            readonlyColumn("Description", Receipt::desc)
        }
        .apply {
            contextmenu {
                item ("Delete receipt") {
                    action {
                        CustomerFacade.removeReceipt((selectedItem as Receipt).id)
                        receiptList.remove(selectedItem)
                    }
                }.enableWhen { selectionModel.selectedItemProperty().isNotNull }
            }
        }
    }

    fun refresh (customer: Customer? = null) {
        receiptList.setAll(CustomerFacade.customerReceipts(customer?.id ?: 0))
        root.text = "${customer?.name ?: ""} Receipts"
    }
}