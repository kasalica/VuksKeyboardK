package com.example.vukskeyboardk.database

class Row {
    var id : Int = 0
    var character : String = ""
    var count : Int = 1

    constructor(character: String, count: Int){
        this.character= character
        this.count = count
    }
    constructor(){}
}
