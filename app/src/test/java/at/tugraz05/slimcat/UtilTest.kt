package at.tugraz05.slimcat

import org.junit.Test
import java.time.LocalDate

class UtilTest {
    @Test
    fun testCalcAgeMonthBefore() {
        val date_of_birth = LocalDate.of(2016, 1, 1)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 5)
    }

    @Test
    fun testCalcAgeMonthAfter() {
        val date_of_birth = LocalDate.of(2016, 12, 1)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 4)
    }

    @Test
    fun testCalcAgeDayBefore() {
        val date_of_birth = LocalDate.of(2016, 5, 1)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 5)
    }

    @Test
    fun testCalcAgeDayAfter() {
        val date_of_birth = LocalDate.of(2016, 5, 12)
        val current_date = LocalDate.of(2021, 5, 6)

        val age = Util.calculateAge(date_of_birth, current_date)

        assert(age == 4)
    }

    @Test
    fun testCalorieRecommendation() {
        val obese = true

        val cat = CatDataClass(
            weight = 6.0, overweight_prone = false, hospitalized = true, neutered = false,
            gestation = false, lactation = false
        )


        val calRec = Util.calculateCalories(cat, obese)

        assert(calRec == 510)
    }

    @Test
    fun testCalorieRecommendationAllFalse() {
        val obese = false

        val cat = CatDataClass(
            weight = 6.0, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = false, lactation = false
        )


        val calRec = Util.calculateCalories(cat, obese)

        assert(calRec == 268)
    }

    @Test
    fun testCalorieRecommendationMaleWithFemaleAttr() {
        val obese = false

        val cat = CatDataClass(
            weight = 6.0, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = true, lactation = true, gender = GenderSeeker.MALE
        )


        val calRec = Util.calculateCalories(cat, obese)

        assert(calRec == 268)
    }

    @Test
    fun testCalorieRecommendationFemale() {
        val obese = false

        val cat = CatDataClass(
            weight = 6.0, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = true, lactation = true, gender = GenderSeeker.FEMALE
        )


        val calRec = Util.calculateCalories(cat, obese)

        assert(calRec == 1744)
    }
}

