package model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    var senderAccount: Customer,
    var receiverAccount: Customer,
    var amount : BigDecimal,
    var createdAt : LocalDateTime = LocalDateTime.now(),
    val sentAt : LocalDateTime = LocalDateTime.now()
)
