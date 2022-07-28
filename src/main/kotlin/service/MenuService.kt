package service

import model.Customer

class MenuService {
    companion object {
        fun showMainMenu () {
            println(
                "\n" +
                "########### Welcome to Mini Bank App ###########\n" +
                "| 1. Customer Accounts                         |\n" +
                "| 2. Transfer Fund                             |\n" +
                "| 3. Transfer History                          |\n" +
                "| 4. Top Up                                    |\n" +
                "| 5. Exit App                                  |\n" +
                "################################################\n"
            )
        }

        fun showCustomerMenu (user : Customer?) {
            println("\n############### Customer Account ###############")
            if (user != null) {
                println(
                    "| 1. View Profile                              |\n" +
                    "| 2. Customer List                             |\n" +
                    "| 3. Sign Out                                  |\n" +
                    "| 4. Exit                                      |"
                )
            } else {
                println(
                    "| 1. Sing In                                   |\n" +
                    "| 2. Sing Up                                   |\n" +
                    "| 3. Exit                                      |"
                )
            }
            println("################################################\n")
        }

        fun showTransferHistoryMenu () {
            println(
                "\n" +
                "############# Transaction History ##############\n" +
                "| 1. All Transactions                          |\n" +
                "| 2. Filter by sender                          |\n" +
                "| 3. Filter by receiver                        |\n" +
                "| 4. Filter by amount                          |\n" +
                "| 5. Filter by Sent Date                       |\n" +
                "| 6. Exit                                      |\n" +
                "################################################\n"
            )
        }

        fun getNumberMenu (listMenu: List<String>) : Int {
            var isValid = false
            var menuNumber: Int = -1

            do {
                print("Input Number: ")
                var menu = readLine()
                when (menu) {
                    in listMenu
                    -> {
                        if (menu != null) {
                            menuNumber = menu.toInt()
                        }
                        isValid = true
                    }
                    else -> println("ERROR: Please input valid number again!!")
                }
            } while (!isValid)

            return menuNumber
        }
    }
}