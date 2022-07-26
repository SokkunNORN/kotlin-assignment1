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

    fun String.stringToBigDecimal() : BigDecimal? {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        if (this.toDoubleOrNull() != null) {
            return BigDecimal(df.format(this.toDouble()))
        }

        println("ERROR: Invalid format, Amount should be the number only!!")
        return null
    }

}