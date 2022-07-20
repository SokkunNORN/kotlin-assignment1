package service

import model.Customer
import model.Transaction
import java.math.BigDecimal

class TransferService {
    private val transferList = mutableListOf<Transaction>()
    private val ownerTransfer = mutableListOf<Transaction>()

    private fun textInput (label: String) : String {
        println("$label: ")
        var text = readLine()
        while (text == null) {
            text = readLine()
        }

        return text
    }

    fun createTransfer (customerService : CustomerService) : Transaction? {
        if (customerService.user == null) {
            println("Please sign in to the application first!!")
            return null
        }
        if (customerService.user!!.balance <= BigDecimal(0)) {
            println("You balance is ${customerService.user!!.balance}. \nPlease top up your balance!!")
            return null
        }

        println("Please input: ")
        var amount = textInput("Amount")
        while (BigDecimal(amount.toInt()) > customerService.user!!.balance) {
            println("You balance is not enough. Please input again...")
            amount = textInput("Amount")
        }
        val message = textInput("Message")
        val customer = customerService.getReceiver()

        if (customer != null) {
            val transfer = Transaction(
                customerService.user!!,
                customer,
                amount = BigDecimal(amount.toInt()),
                message
            )

            println(
                "1. Send\n" +
                "2. Cancel"
            )

            when (MenuService.getNumberMenu(listOf("1", "2"))) {
                1 -> {
                    transferList.add(transfer)
                    println("Transaction is sent successfully!!")
                    return transfer
                }
                2 -> println("You have cancelled fund transfer!!")
            }
        }
        return null
    }

    fun showTransferHistory (user: Customer) {
        val list = transferList.filter {
            (it.senderAccount.name == user.name && it.senderAccount.password == user.password) ||
            (it.receiverAccount.name == user.name && it.receiverAccount.password == user.password)
        }
        ownerTransfer.clear()
        ownerTransfer.addAll(list)

        println("============== Transfer History ==============")
        if (ownerTransfer.isNotEmpty()) {
            for (transfer : Transaction in ownerTransfer) {
                println(
                    "| Receiver Name: ${transfer.senderAccount.name}\n" +
                    "| Receiver Name: ${transfer.receiverAccount.name}\n" +
                    "| Amount: ${transfer.amount}\n" +
                    "| Message: ${transfer.message}\n" +
                    "| Created At: ${transfer.createdAt}\n" +
                    "| Sent At: ${transfer.sentAt}\n" +
                    "=============================================="
                )
            }
        } else {
            println("| You did have any transfer yet!!")
        }

    }
}