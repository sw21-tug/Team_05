package at.tugraz05.slimcat

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude
import java.lang.NumberFormatException

data class FoodDetailsDataClass(var name: String? = null, var rawProtein: Double = 0.0, var rawFat: Double = 0.0,
                                var crudeAsh: Double = 0.0, var rawFiber: Double = 0.0, var water: Double = 0.0) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "rawProtein" to rawProtein,
            "rawFat" to rawFat,
            "crudeAsh" to crudeAsh,
            "rawFiber" to rawFiber,
            "water" to water,
        )
    }

    @Exclude
    fun getRawProteinStr(): String {
        return rawProtein.toString()
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
        return rawFat.toString()
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
        return crudeAsh.toString()
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
        return rawFiber.toString()
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
        return water.toString()
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
        parcel.writeDouble(rawProtein)
        parcel.writeDouble(rawFat)
        parcel.writeDouble(crudeAsh)
        parcel.writeDouble(rawFiber)
        parcel.writeDouble(water)
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
