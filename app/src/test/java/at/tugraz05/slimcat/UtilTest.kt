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
        val weight: Double = 6.0
        val obese = true
        val overweightProne = false
        val hospitalized = true
        val neutered = false
        val gestation = false
        val lactation = false

        val calRec = Util.calculateCalories(weight, obese, overweightProne, hospitalized,
            neutered, gestation, lactation)

        assert(calRec == 510)
    }

    @Test
    fun testCalorieRecommendationAllFalse() {
        val weight: Double = 6.0
        val obese = false
        val overweightProne = false
        val hospitalized = false
        val neutered = false
        val gestation = false
        val lactation = false

        val calRec = Util.calculateCalories(weight, obese, overweightProne, hospitalized,
            neutered, gestation, lactation)

        assert(calRec == 268)
    }

    @Test
    fun testKgToLbs() {
        val kg1 = 17.5
        val kg2 = 0.0

        assert(Util.convertKgToLbs(kg1) in 38.580..38.59)
        assert(Util.convertKgToLbs(kg2) == 0.0)
    }

    @Test
    fun testLbsToKG() {
        val lbs1 = 43.5
        val lbs2 = 0.0

        assert(Util.convertLbsToKg(lbs1) in 19.72..19.73)
        assert(Util.convertLbsToKg(lbs2) == 0.0)
    }

    @Test
    fun testCmToInch() {
        val cm1 = 15.5
        val cm2 = 0.0

        assert(Util.convertCmToInch(cm1) in 6.09..6.11)
        assert(Util.convertCmToInch(cm2) == 0.0)
    }

    @Test
    fun testInchToCm() {
        val inch1 = 21.5
        val inch2 = 0.0

        assert(Util.convertInchToCm(inch1) in 54.60..54.62)
        assert(Util.convertInchToCm(inch2) == 0.0)
    }
    
    @Test
    fun testWetFoodGivesCorrectCalories1G() {
        val f = Food.wetFood
        assert(Util.calcGramsOfFood(f, f.kcalPer100G) == 100)

    }
}

