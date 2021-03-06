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
        val cat = CatDataClass(
            age = 3, weight = 6.0, obese = true, overweight_prone = false, hospitalized = true, neutered = false,
            gestation = false, lactation = false
        )
        val calRec = Util.calculateCalories(cat)
        assert(calRec == 242)
    }

    @Test
    fun testCalorieRecommendationAllFalse() {
        val cat = CatDataClass(
            age = 2, weight = 6.0, obese = false, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = false, lactation = false
        )
        val calRec = Util.calculateCalories(cat)
        assert(calRec == 268)
    }

    @Test
    fun testCalorieRecommendationMaleWithFemaleAttr() {
        val cat = CatDataClass(
            age = 2, weight = 6.0, obese = true, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = true, lactation = true, gender = AddcatActivity.MALE
        )
        val calRec = Util.calculateCalories(cat)

        assert(calRec == 242)
    }

    @Test
    fun testCalorieRecommendationFemaleGestation() {
        val cat = CatDataClass(
            age = 4, weight = 6.0, obese = false, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = true, lactation = false, gender = AddcatActivity.FEMALE
        )
        val calRec = Util.calculateCalories(cat)
        assert(calRec == 671)
    }

    @Test
    fun testCalorieRecommendationFemaleLactation() {
        val cat = CatDataClass(
            age = 2, weight = 6.0, obese = true, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = false, lactation = true, gender = AddcatActivity.FEMALE
        )
        val calRec = Util.calculateCalories(cat)

        assert(calRec == 966)
    }

    @Test
    fun testCalorieRecommendationFemaleGestationAndLactation() {
        val cat = CatDataClass(
            age = 2, weight = 6.0, obese = false, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = true, lactation = true, gender = AddcatActivity.FEMALE
        )
        val calRec = Util.calculateCalories(cat)
        assert(calRec == 1073)
    }

    @Test
    fun testIsKitten() {
        val cat = CatDataClass(
            age = 0, weight = 6.0, obese = false, overweight_prone = false, hospitalized = false, neutered = false,
            gestation = false, lactation = false, gender = AddcatActivity.FEMALE
        )
        val calRec = Util.calculateCalories(cat)
        assert(calRec == 671)
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
        val f = FoodDetailsDataClass(calories = 123)
        assert(Util.calcGramsOfFood(f, f.calories) == 100)
    }

    @Test
    fun testCalculateWetFoodCalories() {
        val f = FoodDetailsDataClass("wet",10.0, 5.5, 2.5, 1.0,79.0)
        assert(Util.calcFoodCals(f) == 422)
    }

    @Test
    fun testCalculateDryFoodCalories() {
        val f = FoodDetailsDataClass("dry",30.0, 12.0, 6.0, 3.0,8.0)
        assert(Util.calcFoodCals(f) == 380)
    }

    @Test
    fun testGrammToLbs(){
        val gram1 = 250
        val gram2 = 0

        assert(Util.convertGrammToLbs(gram1) in 0.54..0.56)
        assert(Util.convertGrammToLbs(gram2) == 0.0)
    }

}

