import tornadofx.*

class CustomerManager : App (InitialView::class)

class InitialView : View (title = "Customer Manager") {
    override val root = borderpane {
        top {
            menubar {
                menu("Customers") {
                    item("Add") {
                        action {
                            find<AddCustomerDialog>().openModal()
                        }
                    }
                }
            }
        }
        center<CustomerView>()
        right<ReceiptView>()
    }
}

fun main() {
    launch<CustomerManager>()
}
