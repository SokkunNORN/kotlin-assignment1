import model.Customer
import service.CustomerService
import service.MenuService

fun main(args: Array<String>) {
//    Mini Bank Application
    mainMenu()
}

val customer = CustomerService()

fun mainMenu () {
    MenuService.showMainMenu()
    val listMenu = listOf("1", "2", "3", "4", "5")
    val menu = listOf(2, 3, 4)

    when (MenuService.getNumberMenu(listMenu)) {
        1 -> {
            customerMenu()
        }
        in menu -> {
            if (customer.user != null) {
                when (MenuService.getNumberMenu(listMenu)) {

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
            } else {
                customer.login()
            }
        }
        2 -> {
            if (customer.user != null) {
                customer.customerListView()
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
                customer.viewProfile()
            } else {
                mainMenu()
            }
        }
        4 -> mainMenu()
    }
}
