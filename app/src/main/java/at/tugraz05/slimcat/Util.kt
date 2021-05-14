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

    fun calculateCalories(weight: Double, obese: Boolean, overweightProne: Boolean, hospitalized: Boolean, neutered: Boolean, gestation: Boolean, lactation: Boolean) : Int {
        var maintenceEnergyRequirements = 0.0
        var restingEnergyRequirements = FACTOR_RESTING_ENERGY_REQUIREMENT * weight.pow(POW_RESTING_ENERGY_REQUIREMENT)

        if(obese) {
            maintenceEnergyRequirements += FACTOR_OBESE * restingEnergyRequirements
        }
        if(overweightProne) {
            maintenceEnergyRequirements += FACTOR_OVERWEIGHT_PRONE * restingEnergyRequirements
        }
        if(hospitalized) {
            maintenceEnergyRequirements += FACTOR_HOSPITALIZED * restingEnergyRequirements
        }
        if(neutered) {
            maintenceEnergyRequirements += FACTOR_NEUTERED * restingEnergyRequirements
        }
        if(gestation) {
            maintenceEnergyRequirements += FACTOR_GESTATION * restingEnergyRequirements
        }
        if(lactation) {
            maintenceEnergyRequirements += FACTOR_LACTATION * restingEnergyRequirements
        }
        if(!obese && !overweightProne && !hospitalized && !neutered && !gestation && !lactation){
            maintenceEnergyRequirements = restingEnergyRequirements
        }

        return maintenceEnergyRequirements.roundToInt()
    }
}
