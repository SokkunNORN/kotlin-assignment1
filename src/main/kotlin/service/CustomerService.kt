package service

import command.Extension.khFormat
import command.Extension.stringToBigDecimal
import command.Extension.toLocalDate
import enum.Gender
import model.Address
import model.Customer
import model.Transaction
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class CustomerService {
    var user : Customer? = Customer(
        "Tom",
        "12",
        "Male", LocalDate.now(),
        BigDecimal(100000),
        Address(
            "310-2",
            "Building-2",
            "KohPich",
            "Chamkamon",
            "Phnom Penh"
        )
    )
    private val allCustomerList = mutableListOf(
        Customer("Tom", "12", "Male", LocalDate.now(), BigDecimal(100000), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh")),
        Customer("Jenny", "12", "Female", LocalDate.now(), BigDecimal(1000), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh")),
        Customer("Karry", "12", "Female", LocalDate.now(), BigDecimal(1000), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh")),
        Customer("Visa", "12", "Female", LocalDate.now(), BigDecimal(1000), Address("310-2", "Building-2", "KohPich", "Chamkamon", "Phnom Penh"))
    )
    private val receiverList = mutableListOf<Customer>()

    private fun textInput (label: String) : String {
        println("$label: ")
        var text = readLine()
        while (text == null) {
            text = readLine()
        }

        return text
    }

    private fun localDateInput () : LocalDate {
        return textInput("Date of Birth (dd-MM-yyyy)").toLocalDate("Date of Birth") ?: localDateInput()
    }

    private fun genderInput () : String {
        var gender = textInput("Gender (Male of Female)")
        val uppercase = gender.uppercase()
        while (Gender.values().find { it.name == uppercase } == null) {
            println("ERROR: Invalid gender.\nPlease input again!!")
            gender = textInput("Gender (Male of Female)")
        }

        return gender
    }

    fun login () : Boolean {
        println("Input name: ")
        val name = readLine()
        println("Input password: ")
        val password = readLine()
        val allUser = allCustomerList.associateBy { it.name + it.password }
        val auth = allUser[name + password]

        if (auth != null) {
            user = auth
            println("Sign in successfully!!")
            return  true
        }

        println("ERROR: Incorrect name and password!!")
        return false
    }

    fun logout () {
        user = null
    }

    fun createCustomer () : Boolean {
        println("Please input: ")
        val name = textInput("Name")
        val password = textInput("Password")
        val gender = genderInput()
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
            balance,
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
            else -> false
        }
    }

    fun viewProfile () {
        println(
            "\n================= Profile ================="
        )
        if (user != null) {
            println(
                "| Name: ${user!!.name}\n" +
                "| Gender: ${user!!.gender}\n" +
                "| Date of Birth: ${user!!.dateOfBirth.khFormat()}\n" +
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
            "\n================= Profile ================="
        )
        for (customer : Customer in allCustomerList) {
            println(
                "| Name: ${customer.name}\n" +
                "| Gender: ${customer.gender}\n" +
                "| Date of Birth:  ${customer.dateOfBirth.khFormat()}\n" +
                "| Address: \n" +
                "   - Street No: ${customer.address.streetNo}\n" +
                "   - Builder No: ${customer.address.buildingNo}\n" +
                "   - District: ${customer.address.district}\n" +
                "   - Commune: ${customer.address.commune}\n" +
                "   - Province/City: ${customer.address.province}\n" +
                "==========================================="
            )
        }
    }

    fun getReceiver (label : String = "receiver") : Customer? {
        if (user != null) {
            val list = allCustomerList.filter { it.name != user!!.name || it.password != user!!.password }
            receiverList.clear()
            receiverList.addAll(list)
            val menuList = mutableListOf<String>()

            if (receiverList.isNotEmpty()) {
                println("\n======== Please select $label account ========")
                receiverList.forEachIndexed{ index, customer ->
                    println("| ${index + 1}. ${customer.name}")
                    menuList.add("${index + 1}")
                }
                println("")

                val number = MenuService.getNumberMenu(menuList)

                return receiverList[number - 1]
            } else {
                println("\n| Did not have account receiver yet!!")
            }
        }
        return null
    }

    fun topUpBalance () {
        if (user == null) {
            println("Please sign in the application first!!")
        } else {
            var amount = textInput("Amount").stringToBigDecimal()

            while (amount == null || amount < BigDecimal(1)) {
                amount = textInput("Amount").stringToBigDecimal()
            }

            println(
                "1. Top up\n" +
                "2. Cancel"
            )
            when (MenuService.getNumberMenu(listOf("1", "2"))) {
                1 -> {
                    val newBalance = user!!.balance.plus(amount)
                    user!!.balance = newBalance
                    println("You have topped up balance successfully!!")
                }
                2 -> println("You have cancelled top up balance successfully!!")
            }
        }
    }

    fun updateBalance (transaction: Transaction) {
        if (user != null) {
            allCustomerList.forEachIndexed { index, _ ->
                if (
                    allCustomerList[index].name == transaction.senderAccount.name &&
                    allCustomerList[index].password == transaction.senderAccount.password
                ) {
                    val senderBalance = transaction.senderAccount.balance.minus(transaction.amount)
                    user!!.balance = senderBalance
                    allCustomerList[index] = user!!
                }
                if (
                    allCustomerList[index].name == transaction.receiverAccount.name &&
                    allCustomerList[index].password == transaction.receiverAccount.password
                ) {
                    val receiver = transaction.receiverAccount
                    val receiverBalance = receiver.balance.plus(transaction.amount)
                    receiver.balance = receiverBalance
                    allCustomerList[index] = receiver
                }
            }
        }
    }
}