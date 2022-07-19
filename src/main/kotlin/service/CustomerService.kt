package service

import model.Address
import model.Customer
import java.math.BigDecimal
import java.time.LocalDate

class CustomerService {
    var user : Customer? = null
    private val customerList = mutableListOf<Customer>()

    fun login () : Boolean {
        println("Input name: ")
        val name = readLine()
        println("Input password: ")
        val password = readLine()

        val customer = customerList.firstOrNull() { it.name == name && it.password == password} ?: kotlin.run {
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

    fun createCustomer () : Boolean {
        println("Please input: ")
        val name = textInput("Name")
        val password = textInput("Password")
        val gender = textInput("Gender")
        val dateOfBirth = LocalDate.now()
        val balance = textInput("Balance")
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
                customerList.add(customer)
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
                "| Date of Birth:  ${user!!.dateOfBirth}\n" +
                "| Balance:  ${user!!.balance}\n" +
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
        for (customer : Customer in customerList) {
            println(
                "| Name: ${customer!!.name}\n" +
                "| Gender: ${customer!!.gender}\n" +
                "| Date of Birth:  ${customer!!.dateOfBirth}\n" +
                "| Balance:  ${customer!!.balance}\n" +
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
}