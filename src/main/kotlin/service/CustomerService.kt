package service

import model.Customer

class CustomerService {
    var user : Customer? = null
    private val customerList = mutableListOf<Customer>()

    fun login () : Boolean {
        println("Input name: ")
        val name = readLine()
        println("Input password: ")
        val password = readLine()

        val customer = customerList.firstOrNull() { it.name == name && it.password == password} ?: kotlin.run {
            throw NullPointerException("Incorrect name and password!!")
            return false
        }

        user = customer
        println("Sign in successfully!!")
        return  true
    }

    fun createCustomer () : Boolean {
        return false
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
                "   - Street No: ${user!!.address.streetNumber}\n" +
                "   - Builder No: ${user!!.address.builderNote}\n" +
                "   - District: ${user!!.address.district}\n" +
                "   - Commune: ${user!!.address.commune}\n" +
                "   - Province/City: ${user!!.address.province}\n" +
                "========================================="
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
                "   - Street No: ${customer!!.address.streetNumber}\n" +
                "   - Builder No: ${customer!!.address.builderNote}\n" +
                "   - District: ${customer!!.address.district}\n" +
                "   - Commune: ${customer!!.address.commune}\n" +
                "   - Province/City: ${customer!!.address.province}\n" +
                "========================================="
            )
        }
    }
}