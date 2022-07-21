package service

import model.Address
import model.Customer
import model.Transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.parseOrNull

class CustomerService {
    var user : Customer? = null
    private val allCustomerList = mutableListOf<Customer>(
        Customer("Tom", "12", "Male", LocalDate.now(), BigDecimal(0), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh")),
        Customer("Jenny", "12", "Female", LocalDate.now(), BigDecimal(1000), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh")),
        Customer("Karry", "12", "Female", LocalDate.now(), BigDecimal(1000), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh")),
        Customer("Visa", "12", "Female", LocalDate.now(), BigDecimal(1000), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh"))
    )
    private var receiverList = mutableListOf<Customer>()

    fun login () : Boolean {
        println("Input name: ")
        val name = readLine()
        println("Input password: ")
        val password = readLine()

        val customer = allCustomerList.firstOrNull() {
            it.name == name && it.password == password
        } ?: kotlin.run {
            println("Incorrect name and password!!")
            return false
        }

        user = customer
        println("Sign in successfully!!")
        return  true
    }

    fun logout () {
        user = null
    }

    private fun textInput (label: String) : String {
        println("$label: ")
        var text = readLine()
        while (text == null) {
            text = readLine()
        }

        return text
    }

    private fun localDateInput () : LocalDate {
        var splitDate = ""
        var date = textInput("Date of Birth (yyyy-MM-dd)")

        while (date.length != 10 || date[4].toString() != "-" || date[7].toString() != "-") {
            println("Invalid Date of Birth format, Please input again!!")
            date = textInput("Date of Birth (yyyy-MM-dd)")
        }

        for (text : String in date.split("-")) {
            splitDate += text
        }

        return try {
            LocalDate.parse(splitDate, DateTimeFormatter.BASIC_ISO_DATE)
        } catch (_: Exception) {
            println("Invalid Date of Birth format, Please input again!!")
            localDateInput()
        }
    }

    fun createCustomer () : Boolean {
        println("Please input: ")
        val name = textInput("Name")
        val password = textInput("Password")
        val gender = textInput("Gender")
        val dateOfBirth = localDateInput()
        val balance = BigDecimal(0)
        val streetNo = textInput("Street No")
        val buildingNo = textInput("Building No")
        val district = textInput("District")
        val commune = textInput("Commune")
        val province = textInput("City/Province")

        val address = Address(
            streetNo,
            buildingNo,
            district,
            commune,
            province
        )

        val customer = Customer(
            name,
            password,
            gender,
            dateOfBirth,
            BigDecimal(balance.toInt()),
            address
        )

        println(
            "1. Save\n" +
            "2. Cancel"
        )

        return when (MenuService.getNumberMenu(listOf("1", "2"))) {
            1 -> {
                allCustomerList.add(customer)
                user = customer
                true
            }
            2 -> false
            else -> false
        }
    }

    fun viewProfile () {
        println(
            "================= Profile ================="
        )
        if (user != null) {
            println(
                "| Name: ${user!!.name}\n" +
                "| Gender: ${user!!.gender}\n" +
                "| Date of Birth: ${user!!.dateOfBirth}\n" +
                "| Balance: ${user!!.balance}\n" +
                "| Address: \n" +
                "   - Street No: ${user!!.address.streetNo}\n" +
                "   - Builder No: ${user!!.address.buildingNo}\n" +
                "   - District: ${user!!.address.district}\n" +
                "   - Commune: ${user!!.address.commune}\n" +
                "   - Province/City: ${user!!.address.province}\n" +
                "==========================================="
            )
        }
    }

    fun customerListView () {
        println(
            "================= Profile ================="
        )
        for (customer : Customer in allCustomerList) {
            println(
                "| Name: ${customer!!.name}\n" +
                "| Gender: ${customer!!.gender}\n" +
                "| Date of Birth:  ${customer!!.dateOfBirth}\n" +
                "| Address: \n" +
                "   - Street No: ${customer!!.address.streetNo}\n" +
                "   - Builder No: ${customer!!.address.buildingNo}\n" +
                "   - District: ${customer!!.address.district}\n" +
                "   - Commune: ${customer!!.address.commune}\n" +
                "   - Province/City: ${customer!!.address.province}\n" +
                "==========================================="
            )
        }
    }

    fun getReceiver () : Customer? {
        if (user != null) {
            val list = allCustomerList.filter { it.name != user!!.name || it.password != user!!.password }
            receiverList.clear()
            receiverList.addAll(list)
            val menuList = mutableListOf<String>()

            if (receiverList.isNotEmpty()) {
                println("======== Please select receiver account ========")
                receiverList.forEachIndexed{ index, customer ->
                    println("| ${index + 1}. ${customer.name}")
                    menuList.add("${index + 1}")
                }

                val number = MenuService.getNumberMenu(menuList)

                return receiverList[number - 1]
            } else {
                println("Did not have account receiver yet!!")
            }
        }
        return null
    }

    fun topUpBalance () {
        if (user == null) {
            println("Please sign in the application first!!")
        } else {
            var amount = textInput("Amount").toIntOrNull()

            while (amount == null || amount < 1) {
                println("Invalid amount, Please input again!!")
                amount = textInput("Amount").toIntOrNull()
            }

            println(
                "1. Top up\n" +
                "2. Cancel"
            )
            when (MenuService.getNumberMenu(listOf("1", "2"))) {
                1 -> {
                    val newBalance = user!!.balance.plus(BigDecimal(amount))
                    user!!.balance = newBalance
                    println("You have topped up balance successfully!!")
                }
                2 -> println("You have cancelled top up balance successfully!!")
            }
        }
    }

    fun updateBalance (transaction: Transaction) {
        if (user != null) {
            var index = allCustomerList.indexOf(transaction.senderAccount)
            val senderBalance = transaction.senderAccount.balance.minus(transaction.amount)
            user!!.balance = senderBalance
            allCustomerList[index] = user!!

            val receiver = transaction.receiverAccount
            index = allCustomerList.indexOf(receiver)
            val receiverBalance = receiver.balance.plus(transaction.amount)
            receiver.balance = receiverBalance
            allCustomerList[index] = receiver
        }
    }
}