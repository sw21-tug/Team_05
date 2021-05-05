package at.tugraz05.slimcat

import com.google.firebase.database.Exclude
import java.lang.NumberFormatException

data class CatDataClass(var name: String? = null, var race: String? = null, var age: Int = 0,
                        var size: Int = 0, var weight: Double = 0.0, var gender: Int? = null, var imageString: String = "") {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "race" to race,
            "age" to age,
            "size" to size,
            "weight" to weight,
            "gender" to gender,
            "imageString" to imageString
        )
    }

    @Exclude
    fun getSizeStr(): String {
        return size.toString()
    }
    @Exclude
    fun setSizeStr(s: String) {
        size = try {
            s.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }

    @Exclude
    fun getWeightStr(): String {
        return weight.toString()
    }
    @Exclude
    fun setWeightStr(s: String) {
        weight = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
}