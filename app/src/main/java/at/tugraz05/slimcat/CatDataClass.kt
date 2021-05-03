package at.tugraz05.slimcat

data class CatDataClass(val name: String? = null, val race: String? = null, val age: Int? = null,
                        val size: Int? = null, val weight: Double? = null, val gender: String? = null) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "name" to name,
                "race" to race,
                "age" to age,
                "size" to size,
                "weight" to weight,
                "gender" to gender
        )
    }

}