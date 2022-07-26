package service

import command.Extension.khFormat
import model.Transaction
import java.math.BigDecimal

class TransferService {
    private val transferList = mutableListOf<Transaction>()
    private val textField = TextInputService()

    private fun getOwnTransaction (customerService: CustomerService) : List<Transaction> {
        val transactions = mutableListOf<Transaction>()
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
                    "| Sender Name: ${transfer.senderAccount.name}\n" +
                    "| Receiver Name: ${transfer.receiverAccount.name}\n" +
                    "| Amount: ${transfer.amount}\n" +
                    "| Message: ${transfer.message}\n" +
                    "| Created At: ${transfer.createdAt.khFormat()}\n" +
                    "| Sent At: ${transfer.sentAt.khFormat()}\n" +
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
        var amount = textField.amount()

        while (amount > customerService.user!!.balance) {
            println("ERROR: You balance is not enough.\nPlease input again!!")
            amount = textField.amount()
        }
        val message = textField.text("Message")
        val customer = customerService.getReceiver()

        if (customer != null) {
            val transfer = Transaction(
                customerService.user!!,
                customer,
                amount,
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
        val list = getOwnTransaction(customerService)
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
        val list = getOwnTransaction(customerService)
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
        val list = getOwnTransaction(customerService)
        var amountFrom = textField.amount("Amount From")
        var amountTo = textField.amount("Amount To")

        while (amountFrom > amountTo) {
            println("ERROR: Amount From is over than Amount To.\nPlease input again!!")
            amountFrom = textField.amount("Amount From")
            amountTo = textField.amount("Amount To")
        }

        val transactions = list.filter {
            it.amount in amountFrom..amountTo
        }

        listTransaction(transactions)
    }

    fun transactionBySentDate (customerService: CustomerService) {
        val list = getOwnTransaction(customerService)
        var from = textField.localDate("Sent date FROM")
        var to = textField.localDate("Sent date TO")

        while (from.isAfter(to)) {
            println("ERROR: Sent date to cannot be before Sent date from.\nPlease input again!!")
            from = textField.localDate("Sent date FROM")
            to = textField.localDate("Sent date TO")
        }

        val transactions = list.filter {
            (it.sentAt.toLocalDate().isAfter(from) && it.sentAt.toLocalDate().isBefore(to)) ||
            (it.sentAt.toLocalDate().isEqual(from) || it.sentAt.toLocalDate().isEqual(to))
        }

        listTransaction(transactions)
    }
}