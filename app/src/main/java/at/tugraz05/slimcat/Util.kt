package at.tugraz05.slimcat

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
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
    const val FACTOR_KG_TO_LBS = 2.205
    const val FACTOR_CM_TO_INCHES = 2.54


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateAge(date_of_birth: LocalDate, current_date : LocalDate) : Int{
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

    fun calculateCalories(cat: CatDataClass, obese : Boolean) : Int {
        var maintenceEnergyRequirements = 0.0
        var restingEnergyRequirements = FACTOR_RESTING_ENERGY_REQUIREMENT * cat.weight.pow(POW_RESTING_ENERGY_REQUIREMENT)

        if(obese) {
            maintenceEnergyRequirements += FACTOR_OBESE * restingEnergyRequirements
        }
        if(cat.overweight_prone) {
            maintenceEnergyRequirements += FACTOR_OVERWEIGHT_PRONE * restingEnergyRequirements
        }
        if(cat.hospitalized) {
            maintenceEnergyRequirements += FACTOR_HOSPITALIZED * restingEnergyRequirements
        }
        if(cat.neutered) {
            maintenceEnergyRequirements += FACTOR_NEUTERED * restingEnergyRequirements
        }

        if(cat.gender == GenderSeeker.FEMALE){
            if(cat.gestation) {
                maintenceEnergyRequirements += FACTOR_GESTATION * restingEnergyRequirements
            }
            if(cat.lactation) {
                maintenceEnergyRequirements += FACTOR_LACTATION * restingEnergyRequirements
            }
            if(!obese && !cat.overweight_prone && !cat.hospitalized && !cat.neutered && !cat.gestation && !cat.lactation){
                maintenceEnergyRequirements = restingEnergyRequirements
            }
        }
        else{
            if(!obese && !cat.overweight_prone && !cat.hospitalized && !cat.neutered){
                maintenceEnergyRequirements = restingEnergyRequirements
            }
        }

        return maintenceEnergyRequirements.roundToInt()
    }

    fun convertKgToLbs(kg: Double): Double {
        return kg * FACTOR_KG_TO_LBS
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

    fun calcGramsOfFood(food: Food, kcal: Int): Int {
        return ((kcal.toDouble() / food.kcalPer100G) * 100).roundToInt()

    }
}
