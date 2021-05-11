package at.tugraz05.slimcat

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import java.lang.NumberFormatException


data class CatDataClass(var name: String? = null, var race: String? = null, var age: Int = 0,
                        var size: Int = 0, var weight: Double = 0.0, var gender: Int? = null, var imageString: String? = "", var calorieRecommendation: Int? = null): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "race" to race,
            "age" to age,
            "size" to size,
            "weight" to weight,
            "gender" to gender,
            "imageString" to imageString,
            "calorieRecommendation" to calorieRecommendation
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(race)
        parcel.writeInt(age)
        parcel.writeInt(size)
        parcel.writeDouble(weight)
        parcel.writeValue(gender)
        parcel.writeString(imageString)
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