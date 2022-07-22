package service

import model.Transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TransferService {
    private val transferList = mutableListOf<Transaction>()

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
        var date = textInput(label)

        while (date.length != 10 || date[4].toString() != "-" || date[7].toString() != "-") {
            println("ERROR: Invalid $label format.\nPlease input again!!")
            date = textInput(label)
        }

        return try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (_: Exception) {
            println("ERROR: Invalid Date of Birth format.\nPlease input again!!")
            localDateInput(label)
        }
    }

    private fun getOwnTransaction (customerService: CustomerService) : List<Transaction> {
        var transactions = mutableListOf<Transaction>()
        if (customerService.user != null) {
            val incomingTransaction = transferList.groupBy { it.receiverAccount.name + it.receiverAccount.password }
            val outgoingTransaction = transferList.groupBy { it.senderAccount.name + it.senderAccount.password }

            if (incomingTransaction[customerService.user!!.name + customerService.user!!.password] != null) {
                transactions.addAll(
                    incomingTransaction[customerService.user!!.name + customerService.user!!.password]!!
                )
            }
            if (outgoingTransaction[customerService.user!!.name + customerService.user!!.password] != null) {
                transactions.addAll(
                    outgoingTransaction[customerService.user!!.name + customerService.user!!.password]!!
                )
            }
        }
        return transactions
    }

    private fun listTransaction (transactions: List<Transaction>) {
        println("\n============== Transfer History ==============")
        if (transactions.isNotEmpty()) {
            for (transfer : Transaction in transactions) {
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

    fun allTransactions (customerService: CustomerService) {
        listTransaction(getOwnTransaction(customerService))
    }

    fun transactionBySender (customerService: CustomerService) {
        var list = getOwnTransaction(customerService)
        val transactions = mutableListOf<Transaction>()
        val sender = customerService.getReceiver("sender")
        if (sender != null) {
            val map = list.groupBy { it.senderAccount.name + it.senderAccount.password }
            if (map[sender.name + sender.password] != null) {
                transactions.addAll(map[sender.name + sender.password]!!)
            }
        } else {
            transactions.addAll(list)
        }
        listTransaction(transactions)
    }

    fun transactionByReceiver (customerService: CustomerService) {
        var list = getOwnTransaction(customerService)
        val transactions = mutableListOf<Transaction>()
        val receiver = customerService.getReceiver("receiver")
        if (receiver != null) {
            val map = list.groupBy { it.receiverAccount.name + it.receiverAccount.password }
            if (map[receiver.name + receiver.password] != null) {
                transactions.addAll(map[receiver.name + receiver.password]!!)
            }
        } else {
            transactions.addAll(list)
        }
        listTransaction(transactions)
    }

    fun transactionByAmount (customerService: CustomerService) {
        var list = getOwnTransaction(customerService)
        var amountFrom = amountInput("Amount From")
        var amountTo = amountInput("Amount To")

        while (amountFrom > amountTo) {
            println("ERROR: Amount From is over than Amount To.\nPlease input again!!")
            amountFrom = amountInput("Amount From")
            amountTo = amountInput("Amount To")
        }

        val transactions = list.filter {
            it.amount >= BigDecimal(amountFrom) && it.amount <= BigDecimal(amountTo)
        }

        listTransaction(transactions)
    }

    fun transactionBySentDate (customerService: CustomerService) {
        var list = getOwnTransaction(customerService)
        var from = localDateInput("Sent date FROM (yyyy-MM-dd)")
        var to = localDateInput("Sent date TO (yyyy-MM-dd)")

        while (from.isAfter(to)) {
            println("ERROR: Sent date to cannot be before Sent date from.\nPlease input again!!")
            from = localDateInput("Sent date FROM (yyyy-MM-dd)")
            to = localDateInput("Sent date TO (yyyy-MM-dd)")
        }

        val transactions = list.filter {
            (it.sentAt.toLocalDate().isAfter(from) && it.sentAt.toLocalDate().isBefore(to)) ||
            (it.sentAt.toLocalDate().isEqual(from) || it.sentAt.toLocalDate().isEqual(to))
        }

        listTransaction(transactions)
    }
}