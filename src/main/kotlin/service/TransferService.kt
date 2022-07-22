package service

import model.Customer
import model.Transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            println("ERROR: Invalid amount.\nPlease input again!!")
            amount = textInput(label).toIntOrNull()
        }

        return amount
    }

    private fun localDateInput (label: String) : LocalDate {
        var splitDate = ""
        var date = textInput(label)

        while (date.length != 10 || date[4].toString() != "-" || date[7].toString() != "-") {
            println("ERROR: Invalid $label format.\nPlease input again!!")
            date = textInput(label)
        }

        for (text : String in date.split("-")) {
            splitDate += text
        }

        return try {
            LocalDate.parse(splitDate, DateTimeFormatter.BASIC_ISO_DATE)
        } catch (_: Exception) {
            println("ERROR: Invalid Date of Birth format.\nPlease input again!!")
            localDateInput(label)
        }
    }


    fun createTransfer (customerService : CustomerService) : Transaction? {
        if (customerService.user == null) {
            println("ERROR: Please sign in to the application first!!")
            return null
        }
        if (customerService.user!!.balance <= BigDecimal(0)) {
            println("ERROR: You balance is ${customerService.user!!.balance}. \nPlease top up your balance!!")
            return null
        }

        println("Please input: ")
        var amount = amountInput()

        while (BigDecimal(amount) > customerService.user!!.balance) {
            println("ERROR: You balance is not enough.\nPlease input again!!")
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
                    println("ERROR: Amount From is over than Amount To.\nPlease input again!!")
                    amountFrom = amountInput("Amount From")
                    amountTo = amountInput("Amount To")
                }

                val list = transactions.filter {
                    it.amount >= BigDecimal(amountFrom) && it.amount <= BigDecimal(amountTo)
                }

                transactions = list
            }
            if (isSentAt) {
                var from = localDateInput("Sent date FROM (yyyy-MM-dd)")
                var to = localDateInput("Sent date TO (yyyy-MM-dd)")

                while (from.isAfter(to)) {
                    println("ERROR: Sent date to cannot be before Sent date from.\nPlease input again!!")
                    from = localDateInput("Sent date FROM (yyyy-MM-dd)")
                    to = localDateInput("Sent date TO (yyyy-MM-dd)")
                }

                val list = transactions.filter {
                    (it.sentAt.toLocalDate().isAfter(from) && it.sentAt.toLocalDate().isBefore(to)) ||
                    (it.sentAt.toLocalDate().isEqual(from) || it.sentAt.toLocalDate().isEqual(to))
                }

                transactions = list
            }

            ownerTransfer.addAll(transactions)

            println("\n============== Transfer History ==============")
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
                println("\n| No Transaction Data...")
            }
        }
    }
}