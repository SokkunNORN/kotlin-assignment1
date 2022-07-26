package command

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Extension {

    fun LocalDate.khFormat() : String {
        return this.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    fun LocalDateTime.khFormat() : String {
        return this.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"))
    }

    fun String.toLocalDate(label: String) : LocalDate? {
        val list = this.split("-")
        if (this.length != 10) {
            println("ERROR: Invalid $label format.\nPlease input again!!")
            return null
        }
        if (list.size < 3) {
            println("ERROR: Invalid $label format.\nPlease input again!!")
            return null
        }
        val date = "${list[2]}-${list[1]}-${list[0]}"

        return try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (_: Exception) {
            println("ERROR: Invalid $label format.\nPlease input again!!")
            null
        }
    }

    fun String.stringToBigDecimal() : BigDecimal? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        if (this.toDoubleOrNull() != null) {
            return BigDecimal(df.format(this.toDouble()))
        }

        println("ERROR: Invalid format, Amount should be the number only!!")
        return null
    }

    fun String?.isNull(label: String) : String? {
        return if (this.isNullOrEmpty()) {
            println("ERROR: The $label field is required.")
            null
        } else {
            this
        }
    }
}