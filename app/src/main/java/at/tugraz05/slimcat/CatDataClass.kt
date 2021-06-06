package at.tugraz05.slimcat

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.firebase.database.Exclude
import java.lang.NumberFormatException
import java.sql.Timestamp


class CatDataClass(var name: String? = null, var race: String? = null, var date_of_birth: String? = null, var age: Int = 0,
                   var size: Double? = null, var weight: Double? = null, gender: Int = AddcatActivity.FEMALE, var imageString: String? = "",
                   calorieRecommendation: Int = 0, var obese : Boolean = false, var overweight_prone: Boolean = false, var hospitalized: Boolean = false,
                   var neutered: Boolean = false, var gestation: Boolean = false, var lactation: Boolean = false, var timestamp: Long = 0): Parcelable, BaseObservable() {
    var gender = gender
        @Bindable get
        set(value) {
            field = value
            notifyPropertyChanged(BR.gender)
        }

    var calorieRecommendation = calorieRecommendation
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
        parcel.readValue(Int::class.java.classLoader) as? Int ?: AddcatActivity.FEMALE,
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
            && other.calorieRecommendation == calorieRecommendation && other.obese == obese && other.overweight_prone == overweight_prone && other.hospitalized == hospitalized
            && other.neutered == neutered && other.gestation == gestation && other.lactation == lactation
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (race?.hashCode() ?: 0)
        result = 31 * result + (date_of_birth?.hashCode() ?: 0)
        result = 31 * result + age
        result = 31 * result + (size?.hashCode() ?: 0)
        result = 31 * result + (weight?.hashCode() ?: 0)
        result = 31 * result + (imageString?.hashCode() ?: 0)
        result = 31 * result + obese.hashCode()
        result = 31 * result + overweight_prone.hashCode()
        result = 31 * result + hospitalized.hashCode()
        result = 31 * result + neutered.hashCode()
        result = 31 * result + gestation.hashCode()
        result = 31 * result + lactation.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + gender
        result = 31 * result + calorieRecommendation
        return result
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