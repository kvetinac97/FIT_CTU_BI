import javafx.scene.control.TextField
import tornadofx.*

abstract class Dialog (entity: String, private val fieldTitle: String, block: (Dialog, TextField) -> Unit) : Fragment("New $entity") {
    override val root = form {
        var name: TextField by singleAssign()
        fieldset {
            field ("$fieldTitle:") { name = textfield() }
        }
        buttonbar {
            button ("Cancel") { action { close() } }
            button ("OK") {
                action {
                    block(this@Dialog, name)
                    find<CustomerView>().refresh()
                    close()
                }
            }.enableWhen { name.textProperty().isNotEmpty }
        }
    }
}

class AddCustomerDialog : Dialog ("Customer", "Name", { _, name ->
    CustomerFacade.createCustomer(name.text)
})

class AddReceiptDialog : Dialog ("Receipt", "Description", { dialog, name ->
    dialog.params["customer"]?.apply {
        CustomerFacade.createReceipt( (this as Customer).id, name.text )
    }
})