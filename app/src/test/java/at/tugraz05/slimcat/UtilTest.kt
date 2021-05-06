package at.tugraz05.slimcat

import org.junit.Test
import java.time.LocalDate

class UtilTest{
    @Test
    fun testCalcAgeMonthBefore(){
        val date_of_birth = LocalDate.of(2016, 1, 1)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 5)
    }

    @Test
    fun testCalcAgeMonthAfter(){
        val date_of_birth = LocalDate.of(2016, 12, 1)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 4)
    }

    @Test
    fun testCalcAgeDayBefore(){
        val date_of_birth = LocalDate.of(2016, 5, 1)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 5)
    }

    @Test
    fun testCalcAgeDayAfter(){
        val date_of_birth = LocalDate.of(2016, 5, 12)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 4)
    }
}

