import model.Customer
import service.CustomerService
import service.MenuService
import java.io.Console

fun main(args: Array<String>) {
//    Mini Bank Application
    mainMenu()
}

val customer = CustomerService()

fun mainMenu () {
    MenuService.showMainMenu()
    val listMenu = listOf("1", "2", "3", "4", "5")
    val menu = listOf(2, 3, 4)

    when (val menuNumber = MenuService.getNumberMenu(listMenu)) {
        1 -> {
            customerMenu()
        }
        in menu -> {
            if (customer.user != null) {
                when (menuNumber) {

                }
            } else {
                println("Please sign in the application first!!!")
                mainMenu()
            }
        }
        5 -> println("Shutdown...")
    }
}

fun customerMenu() {
    MenuService.showCustomerMenu(customer.user)
    val list = mutableListOf("1", "2", "3")

    if (customer.user != null) {
        list.add("4")
    }

    when (MenuService.getNumberMenu(list)) {
        1 -> {
            if (customer.user != null) {
                customer.viewProfile()
                customerMenu()
            } else {
                customer.login()
                customerMenu()
            }
        }
        2 -> {
            if (customer.user != null) {
                customer.customerListView()
                customerMenu()
            } else {
                if (customer.createCustomer()) {
                    println("You have sign up a user successfully!!")
                } else {
                    println("You have cancelled sign up a user!!")
                }
                customerMenu()
            }
        }
        3 -> {
            if (customer.user != null) {
                customer.logout()
                customerMenu()
            } else {
                mainMenu()
            }
        }
        4 -> mainMenu()
    }
}
