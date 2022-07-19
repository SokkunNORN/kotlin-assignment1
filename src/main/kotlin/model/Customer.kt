package model

import java.math.BigDecimal
import java.time.LocalDate

data class Customer(
    var name: String,
    val password: String,
    val gender: String,
    val dateOfBirth: LocalDate,
    var balance: BigDecimal,
    var address: Address
)
