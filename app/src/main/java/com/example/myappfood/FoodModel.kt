package com.example.myappfood

import java.util.*

data class FoodModel (
    var id:Int = getAutoId(),
    var name: String ="",
    var form: String =""
){
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }

}