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

    private fun amountInput (label: String = "Amount") : Int {
        var amount = textInput(label).toIntOrNull()
        while (amount == null || amount < 1) {
            println("Invalid amount, Please input again!!")
            amount = textInput(label).toIntOrNull()
        }

        return amount
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
        var amount = amountInput()

        while (BigDecimal(amount) > customerService.user!!.balance) {
            println("You balance is not enough. Please input again...")
            amount = amountInput()
        }
        val message = textInput("Message")
        val customer = customerService.getReceiver()

        if (customer != null) {
            val transfer = Transaction(
                customerService.user!!,
                customer,
                amount = BigDecimal(amount),
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

    fun showTransferHistory (
        customerService: CustomerService,
        isSender : Boolean = false,
        isReceiver : Boolean = false,
        isAmount : Boolean = false,
        isSentAt : Boolean = false
    ) {
        if (customerService.user != null) {
            ownerTransfer.clear()
            var transactions = transferList.filter {
                (it.senderAccount.name == customerService.user!!.name && it.senderAccount.password == customerService.user!!.password) ||
                (it.receiverAccount.name == customerService.user!!.name && it.receiverAccount.password == customerService.user!!.password)
            }

            if (isSender) {
                val sender = customerService.getReceiver("sender")
                if (sender != null) {
                    val list = transactions.filter {
                        it.senderAccount.name == sender.name && it.senderAccount.password == sender.password
                    }

                    transactions = list
                }
            }
            if (isReceiver) {
                val receiver = customerService.getReceiver()
                if (receiver != null) {
                    val list = transactions.filter {
                        it.receiverAccount.name == receiver.name && it.receiverAccount.password == receiver.password
                    }

                    transactions = list
                }
            }
            if (isAmount) {
                var amountFrom = amountInput("Amount From")
                var amountTo = amountInput("Amount To")

                while (amountFrom > amountTo) {
                    println("Amount From is over than Amount To. Please input again!!")
                    amountFrom = amountInput("Amount From")
                    amountTo = amountInput("Amount To")
                }

                val list = transactions.filter {
                    it.amount >= BigDecimal(amountFrom) && it.amount <= BigDecimal(amountTo)
                }

                transactions = list
            }
            if (isSentAt) {

            }

            ownerTransfer.addAll(transactions)

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
                println("| No Transaction Data...")
            }
        }
    }
}