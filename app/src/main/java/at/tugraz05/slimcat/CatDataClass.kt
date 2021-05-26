package at.tugraz05.slimcat

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.database.Exclude
import java.lang.NumberFormatException


data class CatDataClass(var name: String? = null, var race: String? = null, var date_of_birth: String? = null, var age: Int = 0,
                        var size: Double? = null, var weight: Double? = null, var gender: Int? = null, var imageString: String? = "",
                        private val _calorieRecommendation: Int = 0, var overweight_prone: Boolean = false, var hospitalized: Boolean = false,
                        var neutered: Boolean = false, var gestation: Boolean = false, var lactation: Boolean = false): Parcelable, BaseObservable() {
    var calorieRecommendation = _calorieRecommendation
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.calorieRecommendation)
        }

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
            "overweight_prone" to overweight_prone,
            "hospitalized" to hospitalized,
            "neutered" to neutered,
            "gestation" to gestation,
            "lactation" to lactation
        )
    }

    @Exclude
    fun getSizeStr(): String {
        return size?.toString() ?: ""
    }

    @Exclude
    fun getWeightStr(): String {
        return weight?.toString() ?: ""
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(race)
        parcel.writeString(date_of_birth)
        parcel.writeInt(age)
        parcel.writeDouble(size ?: 0.0)
        parcel.writeDouble(weight ?: 0.0)
        parcel.writeValue(gender)
        parcel.writeString(imageString)
        parcel.writeInt(calorieRecommendation)
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