package at.tugraz05.slimcat

data class Food(val name: String, val kcalPer100G: Int, val isWet: Boolean) {
    companion object {
        val wetFood = Food("Wet Food", 350, true)
        val dryFood = Food("Dry Food", 375, false)
        val foods = listOf(wetFood, dryFood)
    }

}
