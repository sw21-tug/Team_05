package at.tugraz05.slimcat

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

object Util {
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(date_of_birth: LocalDate, current_date : LocalDate) : Int{
        var age = current_date.year - date_of_birth.year

        if (current_date.month < date_of_birth.month){
            age--
        }

        return age
    }
}