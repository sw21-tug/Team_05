package at.tugraz05.slimcat

import android.os.Parcel
import android.os.Parcelable
import android.renderscript.Sampler
import com.google.firebase.database.Exclude
import java.lang.NumberFormatException
import java.time.LocalDate


data class CatDataClass(var name: String? = null, var race: String? = null, var date_of_birth: String? = null, var age: Int = 0,
                        var size: Double = 0.0, var weight: Double = 0.0, var gender: Int? = null, var imageString: String? = "",
                        var calorieRecommendation: Int = 0, var obese : Boolean = false, var overweight_prone: Boolean = false, var hospitalized: Boolean = false,
                        var neutered: Boolean = false, var gestation: Boolean = false, var lactation: Boolean = false): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as Boolean,
        parcel.readValue(Int::class.java.classLoader) as Boolean,
        parcel.readValue(Int::class.java.classLoader) as Boolean,
        parcel.readValue(Int::class.java.classLoader) as Boolean,
        parcel.readValue(Int::class.java.classLoader) as Boolean,
        parcel.readValue(Int::class.java.classLoader) as Boolean
    ) {
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "race" to race,
            "date_of_birth" to date_of_birth,
            "age" to age,
            "size" to size,
            "weight" to weight,
            "gender" to gender,
            "imageString" to imageString,
            "calorieRecommendation" to calorieRecommendation,
            "obese" to obese,
            "overweight_prone" to overweight_prone,
            "hospitalized" to hospitalized,
            "neutered" to neutered,
            "gestation" to gestation,
            "lactation" to lactation
        )
    }

    @Exclude
    fun getSizeStr(): String {
        return size.toString()
    }
    @Exclude
    fun setSizeStr(s: String) {
        size = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.0
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(race)
        parcel.writeString(date_of_birth)
        parcel.writeInt(age)
        parcel.writeDouble(size)
        parcel.writeDouble(weight)
        parcel.writeValue(gender)
        parcel.writeString(imageString)
        parcel.writeInt(calorieRecommendation)
        parcel.writeValue(obese)
        parcel.writeValue(overweight_prone)
        parcel.writeValue(hospitalized)
        parcel.writeValue(neutered)
        parcel.writeValue(gestation)
        parcel.writeValue(lactation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatDataClass> {
        override fun createFromParcel(parcel: Parcel): CatDataClass {
            return CatDataClass(parcel)
        }

        override fun newArray(size: Int): Array<CatDataClass?> {
            return arrayOfNulls(size)
        }
    }
}