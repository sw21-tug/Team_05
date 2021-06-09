package at.tugraz05.slimcat

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.pow
import kotlin.math.roundToInt

object Util {
    const val FACTOR_RESTING_ENERGY_REQUIREMENT = 70
    const val POW_RESTING_ENERGY_REQUIREMENT = 0.75
    const val FACTOR_OBESE = 0.9
    const val FACTOR_OVERWEIGHT_PRONE = 1.0
    const val FACTOR_HOSPITALIZED = 1.0
    const val FACTOR_NEUTERED = 1.2
    const val FACTOR_GESTATION = 2.5
    const val FACTOR_LACTATION = 4
    const val FACTOR_KITTEN = 2.5
    const val FACTOR_KG_TO_LBS = 2.205
    const val FACTOR_CM_TO_INCHES = 2.54
    const val FACTOR_GRAMM_TO_LBS = (2.205/1000)


    const val ATWATER_PROTEIN_FACTOR_PER_G = 3.5
    const val ATWATER_FAT_FACTOR_PER_G = 8.5
    const val ATWATER_NFE_FACTOR_PER_G = 3.5

    fun calculateAge(date_of_birth: String): Int {
        return try {
            calculateAge(LocalDate.parse(date_of_birth, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)), LocalDate.now())
        } catch (e: DateTimeParseException) {
            0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(date_of_birth: LocalDate, current_date : LocalDate): Int {
        var age = current_date.year - date_of_birth.year

        if (current_date.month < date_of_birth.month){
            age--
        }
        else if (current_date.month == date_of_birth.month) {
            if (current_date.dayOfMonth < date_of_birth.dayOfMonth)
                age--
        }
        return age
    }

    fun calculateCalories(cat: CatDataClass) : Int {
        val restingEnergyRequirements = FACTOR_RESTING_ENERGY_REQUIREMENT * (cat.weight?.pow(POW_RESTING_ENERGY_REQUIREMENT) ?: 0.0)
        var maintenceEnergyRequirements = restingEnergyRequirements

        if (cat.age <= 1) {
            maintenceEnergyRequirements *= FACTOR_KITTEN
        }
        if(cat.obese) {
            maintenceEnergyRequirements *= FACTOR_OBESE
        }
        if(cat.overweight_prone) {
            maintenceEnergyRequirements *= FACTOR_OVERWEIGHT_PRONE
        }
        if(cat.hospitalized) {
            maintenceEnergyRequirements *= FACTOR_HOSPITALIZED
        }
        if(cat.neutered) {
            maintenceEnergyRequirements *= FACTOR_NEUTERED
        }

        if(cat.gender == AddcatActivity.FEMALE){
            if(cat.gestation && cat.lactation){
                maintenceEnergyRequirements *= FACTOR_LACTATION
            }
            else if(cat.gestation) {
                maintenceEnergyRequirements *= FACTOR_GESTATION
            }
            else if(cat.lactation) {
                maintenceEnergyRequirements *= FACTOR_LACTATION
            }
        }

        return maintenceEnergyRequirements.roundToInt()
    }

    fun convertKgToLbs(kg: Double): Double {
        return kg * FACTOR_KG_TO_LBS
    }

    fun convertGrammToLbs(g: Int): Double {
        return g * FACTOR_GRAMM_TO_LBS
    }

    fun convertLbsToKg(lbs: Double): Double {
        return lbs / FACTOR_KG_TO_LBS
    }

    fun convertCmToInch(cm: Double): Double {
        return cm / FACTOR_CM_TO_INCHES
    }

    fun convertInchToCm(inch: Double): Double {
        return inch * FACTOR_CM_TO_INCHES
    }

    fun calcGramsOfFood(food: FoodDetailsDataClass, kcal: Int): Int {
        return ((kcal.toDouble() / food.calories) * 100).roundToInt()
    }

    fun calcFoodCals(food: FoodDetailsDataClass) :Int {
        var nfe = 100.0 - (food.rawProtein ?: 0.0) - (food.rawFat ?: 0.0) - (food.crudeAsh ?: 0.0) - (food.rawFiber ?: 0.0) - (food.water ?: 0.0)
        val ts = 1 - (food.water ?: 0.0) / 100
        val protein = (food.rawProtein ?: 0.0) / ts
        val fat = (food.rawFat ?: 0.0) / ts
        nfe /= ts

       return ((protein * ATWATER_PROTEIN_FACTOR_PER_G) + (fat * ATWATER_FAT_FACTOR_PER_G) + (nfe * ATWATER_NFE_FACTOR_PER_G)).toInt()
        //asserten dass nicht unter null nach abziehen aller prozent
    }
}
