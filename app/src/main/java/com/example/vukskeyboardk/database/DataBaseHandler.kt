package com.example.vukskeyboardk.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

val DATABASE_NAME = "DB"
val DATABASE_TEMP_NAME = "DBtemp"
val DATABASE_VERSION = 1
val TABLE1_NAME = "CHAR"
val TABLE2_NAME = "TIME"
val TABLE3_NAME = "ACCURACY"
val COL_ID = "id"
val COL_CHAR = "string"
val COL_C = "count"
val COL_TIME = "time"
val COL_X = "coorx"
val COL_Y = "coory"

class DataBaseHandler(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context,
    name, factory,
    DATABASE_VERSION
){

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable1 = "CREATE TABLE " + TABLE1_NAME +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CHAR + " VARCHAR(256)," +
                COL_C +" INTEGER)"

        val createTable2 = "CREATE TABLE " + TABLE2_NAME +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CHAR + " VARCHAR(256)," +
                COL_TIME +" INTEGER," +
                COL_C + " INTEGER)"

        val createTable3 = "CREATE TABLE " + TABLE3_NAME +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CHAR + " VARCHAR(256)," +
                COL_X + " REAL," +
                COL_Y + " REAL," +
                COL_C +" INTEGER)"

        db?.execSQL(createTable1)
        db?.execSQL(createTable2)
        db?.execSQL(createTable3)

    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int,newVersion: Int) {
        TODO("not implemented")
    }


    fun insertData1(character: String) {

        val db =this.writableDatabase
        val query = "Select * from " + TABLE1_NAME + " WHERE " + COL_CHAR + "='$character'"
        val result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(
                    COL_C, result.getInt(result.getColumnIndex(
                        COL_C
                    ))+1)
                cv.put(COL_CHAR, character)

                db.update(
                    TABLE1_NAME,cv, COL_ID + "=? AND " + COL_CHAR + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),character))
            }while (result.moveToNext())
        }

        else {
            val cv = ContentValues()
            cv.put(COL_CHAR, character)
            cv.put(COL_C, 1)
            db.insert(TABLE1_NAME, null, cv)
        }
        result.close()
        db.close()

    }

    fun insertData2(characters: String, time: Int){

        val db =this.writableDatabase
        val query = "Select * from " + TABLE2_NAME + " WHERE " + COL_CHAR + "='$characters'"
        val result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do {
                var t = result.getInt(result.getColumnIndex(COL_TIME))
                var c = result.getInt(result.getColumnIndex(COL_C))
                var cv = ContentValues()

                cv.put(COL_TIME, t*c/(c+1) + time/(c+1))

                cv.put(
                    COL_C, result.getInt(result.getColumnIndex(
                        COL_C
                    ))+1)
                db.update(
                    TABLE2_NAME,cv,
                    COL_ID + "=? AND " + COL_CHAR + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),characters))

            }while (result.moveToNext())
        }

        else {
            val cv = ContentValues()
            cv.put(COL_C, 1)
            cv.put(COL_CHAR, characters)
            cv.put(COL_TIME, time)
            db.insert(TABLE2_NAME, null, cv)
        }
        result.close()
        db.close()
    }



    fun insertData3(character: String, x: Float, y: Float){

        val db =this.writableDatabase
        val query = "Select * from " + TABLE3_NAME + " WHERE " + COL_CHAR + "='$character'"
        val result = db.rawQuery(query,null)

        if(result.moveToFirst()){
            do {
                var xx = result.getFloat(result.getColumnIndex(COL_X))
                var yy = result.getFloat(result.getColumnIndex(COL_Y))
                var c = result.getInt(result.getColumnIndex(COL_C)).toFloat()

                var cv = ContentValues()
                cv.put(COL_X, xx*c/(c+1f) + x/(c+1f))
                cv.put(COL_Y, yy*c/(c+1f) + y/(c+1f))
                cv.put(
                    COL_C, result.getInt(result.getColumnIndex(
                        COL_C
                    ))+1)

                db.update(
                    TABLE3_NAME,cv, COL_ID + "=? AND " + COL_CHAR + "=?",
                    arrayOf(result.getString(result.getColumnIndex(COL_ID)),character))
            }while (result.moveToNext())
        }

        else {
            val cv = ContentValues()
            cv.put(COL_CHAR, character)
            cv.put(COL_C, 1)
            cv.put(COL_X, x)
            cv.put(COL_Y, y)
            db.insert(TABLE3_NAME, null, cv)
        }
        result.close()
        db.close()
    }

    fun readData1() : MutableList<Row>{

        var list : MutableList<Row> = ArrayList()
        val db = this.readableDatabase

        val query = "Select * from " + TABLE1_NAME + " ORDER BY " + COL_CHAR

        val result = db.rawQuery(query,null)
        Log.d("KURSOR: ", result.toString())
        if(result.moveToFirst()){
            do {
                var row = Row()
                row.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                row.character = result.getString(result.getColumnIndex(COL_CHAR))
                row.count = result.getString(result.getColumnIndex(COL_C)).toInt()
                list.add(row)
            }
            while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun readData2() : MutableList<Row2>{

        var list : MutableList<Row2> = ArrayList()
        val db = this.readableDatabase

        val query = "Select * from " + TABLE2_NAME

        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var row = Row2()
                row.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                row.characters = result.getString(result.getColumnIndex(COL_CHAR))
                row.time = result.getString(result.getColumnIndex(COL_TIME)).toInt()
                row.count = result.getString(result.getColumnIndex(COL_C)).toInt()

                list.add(row)
            }
            while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun readData3() : MutableList<Row3>{

        var list : MutableList<Row3> = ArrayList()
        val db = this.readableDatabase

        val query = "Select * from " + TABLE3_NAME

        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var row = Row3()
                row.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                row.character = result.getString(result.getColumnIndex(COL_CHAR))
                row.x = result.getString(result.getColumnIndex(COL_X)).toFloat()
                row.y = result.getString(result.getColumnIndex(COL_Y)).toFloat()
                row.count = result.getString(result.getColumnIndex(COL_C)).toInt()
                list.add(row)

            }
            while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }


    fun deleteData(){
        val db = this.writableDatabase
        db.delete(TABLE1_NAME,null,null)
        db.delete(TABLE2_NAME,null,null)
        db.delete(TABLE3_NAME,null,null)
        db.close()
    }

}
