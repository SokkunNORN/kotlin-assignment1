package service

import model.Customer
import model.Transaction
import java.math.BigDecimal
import java.time.LocalDate

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

    fun createTransfer (customerService : CustomerService) : BigDecimal? {
        if (customerService.user == null) {
            println("Please sign in to the application first!!")
            return null
        }
        if (customerService.user!!.balance < BigDecimal(0)) {
            println("You balance is ${customerService.user!!.balance}. \nPlease top up your balance!!")
            return null
        }

        println("Please input: ")
        var amount = textInput("Amount")
        while (BigDecimal(amount.toInt()) > customerService.user!!.balance) {
            println("Amount is over than your balance. Please input again...")
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
                    return BigDecimal(amount)
                }
                2 -> println("You have cancelled fund transfer!!")
            }
        }
        return null
    }
}