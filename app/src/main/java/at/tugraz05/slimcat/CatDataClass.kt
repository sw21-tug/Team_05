package at.tugraz05.slimcat

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.database.Exclude
import java.lang.NumberFormatException
import java.sql.Timestamp


data class CatDataClass(var name: String? = null, var race: String? = null, var date_of_birth: String? = null, var age: Int = 0,
                        var size: Double? = null, var weight: Double? = null, var gender: Int? = null, var imageString: String? = "",
                        private val _calorieRecommendation: Int = 0, var obese : Boolean = false, var overweight_prone: Boolean = false, var hospitalized: Boolean = false,
                        var neutered: Boolean = false, var gestation: Boolean = false, var lactation: Boolean = false, var timestamp: Long = 0): Parcelable, BaseObservable() {
    var calorieRecommendation = _calorieRecommendation
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.calorieRecommendation)
            timestamp = System.currentTimeMillis()
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
        parcel.readValue(Int::class.java.classLoader) as Boolean,
        parcel.readValue(Int::class.java.classLoader) as Boolean,
        parcel.readLong()
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
            "lactation" to lactation,
            "timestamp" to timestamp
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
        parcel.writeValue(obese)
        parcel.writeValue(overweight_prone)
        parcel.writeValue(hospitalized)
        parcel.writeValue(neutered)
        parcel.writeValue(gestation)
        parcel.writeValue(lactation)
        parcel.writeLong(timestamp)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is CatDataClass)
            return super.equals(other)
        return other.name == name && other.race == race && other.date_of_birth == date_of_birth && other.age == age
            && other.size == size && other.weight == weight && other.gender == gender && other.imageString == imageString
            && other.calorieRecommendation == calorieRecommendation && other.obese == obese  && other.overweight_prone == overweight_prone && other.hospitalized == hospitalized
            && other.neutered == neutered && other.gestation == gestation && other.lactation == lactation
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