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

    @Test
    fun testCalculateWetFoodCalories() {
        val f = FoodDetailsDataClass("wet",10.0, 5.5, 2.5, 1.0,79.0)
        var nfe = 100.0 - f.rawProtein - f.rawFat - f.crudeAsh - f.rawFiber - f.water
        val ts = 100 - f.water
        val protein = f.rawProtein / ts
        val fat = f.rawFat / ts
        nfe /= ts

        val result = ((protein * Util.ATWATER_PROTEIN_FACTOR_PER_G) + (fat * Util.ATWATER_FAT_FACTOR_PER_G) + (nfe * Util.ATWATER_NFE_FACTOR_PER_G)).toInt()
        assert(Util.calcFoodCals(f) == result)
    }

    @Test
    fun testCalculateDryFoodCalories() {
        val f = FoodDetailsDataClass("dry",30.0, 10.0, 6.5, 2.5,9.0)
        var nfe = 100.0 - f.rawProtein - f.rawFat - f.crudeAsh - f.rawFiber - f.water
        val ts = 100 - f.water
        val protein = f.rawProtein / ts
        val fat = f.rawFat / ts
        nfe /= ts

        val result = ((protein * Util.ATWATER_PROTEIN_FACTOR_PER_G) + (fat * Util.ATWATER_FAT_FACTOR_PER_G) + (nfe * Util.ATWATER_NFE_FACTOR_PER_G)).toInt()
        assert(Util.calcFoodCals(f) == result)
    }
}

