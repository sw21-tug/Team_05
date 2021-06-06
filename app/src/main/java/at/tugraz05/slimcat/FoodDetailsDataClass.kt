package at.tugraz05.slimcat

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import java.lang.NumberFormatException

data class FoodDetailsDataClass(var name: String? = null, var rawProtein: Double? = null, var rawFat: Double? = null,
                                var crudeAsh: Double? = null, var rawFiber: Double? = null, var water: Double? = null, var calories: Int = 0) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt()
    ) {
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "rawProtein" to (rawProtein ?: 0.0),
            "rawFat" to (rawFat ?: 0.0),
            "crudeAsh" to (crudeAsh ?: 0.0),
            "rawFiber" to (rawFiber ?: 0.0),
            "water" to (water ?: 0.0),
            "calories" to calories
        )
    }

    @Exclude
    fun getRawProteinStr(): String {
        return rawProtein?.toString() ?: ""
    }
    @Exclude
    fun setRawProteinStr(s: String) {
        rawProtein = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    @Exclude
    fun getRawFatStr(): String {
        return rawFat?.toString() ?: ""
    }
    @Exclude
    fun setRawFatStr(s: String) {
        rawFat = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    @Exclude
    fun getCrudeAshStr(): String {
        return crudeAsh?.toString() ?: ""
    }
    @Exclude
    fun setCrudeAshStr(s: String) {
        crudeAsh = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    @Exclude
    fun getRawFiberStr(): String {
        return rawFiber?.toString() ?: ""
    }
    @Exclude
    fun setRawFiberStr(s: String) {
        rawFiber = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    @Exclude
    fun getWaterStr(): String {
        return water?.toString() ?: ""
    }
    @Exclude
    fun setWaterStr(s: String) {
        water = try {
            s.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(rawProtein ?: 0.0)
        parcel.writeDouble(rawFat ?: 0.0)
        parcel.writeDouble(crudeAsh ?: 0.0)
        parcel.writeDouble(rawFiber ?: 0.0)
        parcel.writeDouble(water ?: 0.0)
        parcel.writeInt(calories)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FoodDetailsDataClass> {
        override fun createFromParcel(parcel: Parcel): FoodDetailsDataClass {
            return FoodDetailsDataClass(parcel)
        }

        override fun newArray(size: Int): Array<FoodDetailsDataClass?> {
            return arrayOfNulls(size)
        }
    }
}