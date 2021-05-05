package at.tugraz05.slimcat

data class CatDummy(val name: String = "", val age: Int = 0, val weight: Double = 0.0) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
                "name" to name,
                "age" to age,
                "weight" to weight
        )
    }

}

/* Reference for functions to use databaseHelper
    fun printCat() {
        val cat: CatDummy = databaseHelper.readUserCat(catName) ?: return
        var catName: String = cat.name!!
        var catAge: Int = cat.age!!
        var catWeight: Double = cat.weight!!

        Toast.makeText(applicationContext, "$catName $catAge $catWeight", Toast.LENGTH_LONG).show()
    }

    fun addCat() {
        var catClass: CatDummy = CatDummy("Phil", 15, 23.4)
        databaseHelper.writeNewCat(catClass)
    }
    */