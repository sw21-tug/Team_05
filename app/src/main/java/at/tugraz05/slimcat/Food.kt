package at.tugraz05.slimcat

abstract class Food(val name: String, val calPerG: Double, val isWet: Boolean) {
}

class WetFood : Food("Wet Food", 20.0, true) {}
