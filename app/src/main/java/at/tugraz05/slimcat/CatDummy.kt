package at.tugraz05.slimcat

class CatDummy {
    private val name: String
    private val age: Int
    private val weight: Double

    constructor(name: String, age: Int, weight: Double){
        this.name = name
        this.age = age
        this.weight = weight
    }

    fun getName(): String{
        return this.name
    }

    fun getAge(): Int{
        return this.age
    }

    fun getWeight(): Double{
        return this.weight
    }
}