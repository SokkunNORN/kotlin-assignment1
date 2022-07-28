package service

import command.Extension.isNull
import command.Extension.stringToBigDecimal
import command.Extension.toLocalDate
import enum.Gender
import java.math.BigDecimal
import java.time.LocalDate

class TextInputService {
    fun text (label: String) : String {
        print("$label: ")
        return readLine().isNull(label) ?: text(label)
    }

    fun gender () : String {
        var gender = text("Gender [${Gender.values().joinToString()}]").uppercase()
        while (Gender.values().find { it.name == gender } == null) {
            println("ERROR: Invalid gender.\nPlease input again!!")
            gender = text("Gender [${Gender.values().joinToString()}]").uppercase()
        }

        return gender
    }

    fun amount (label: String = "Amount") : BigDecimal {
        var amount = text(label).stringToBigDecimal()
        while (amount == null || amount < BigDecimal(1)) {
            amount = text(label).stringToBigDecimal()
        }

        return amount
    }

    fun localDate (label: String) : LocalDate {
        return text("$label (dd-MM-yyyy)").toLocalDate(label) ?: localDate(label)
    }
}