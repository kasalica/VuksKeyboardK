package com.example.vukskeyboardk.database

class Row3 {
    var id : Int = 0
    var character : String = ""
    var x : Float = 0.0f
    var y : Float = 0.0f
    var count = 1

    constructor(character: String, x: Float, y: Float){
        this.character= character
        this.x = x
        this.y = y
    }
    constructor(){}
}
