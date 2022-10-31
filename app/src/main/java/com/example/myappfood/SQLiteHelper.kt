package com.example.myappfood

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "food.db"
        private const val TBL_FOOD = "tbl_food"
        private const val ID = "id"
        private const val NAME = "name"
        private const val FORM = "form"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblFood = ("CREATE TABLE " + TBL_FOOD + "("
                + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + FORM + " TEXT" + ")")
        db?.execSQL(createTblFood)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TBL_FOOD")
        onCreate(db)
    }

    fun insertFood(food: FoodModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, food.id)
        contentValues.put(NAME, food.name)
        contentValues.put(FORM, food.form)
        val success = db.insert(TBL_FOOD, null, contentValues)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getAllFood(): ArrayList<FoodModel>{
        val foodList :ArrayList<FoodModel> =ArrayList()
        val selectQuery = "SELECT * FROM $TBL_FOOD"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id:Int
        var name:String
        var form:String

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                form = cursor.getString(cursor.getColumnIndex("form"))
                val food = FoodModel(id = id, name =name, form=form)
                foodList.add(food)
            }while (cursor.moveToNext())
        }

        return foodList
    }

    fun updateFood(food:FoodModel):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, food.id)
        contentValues.put(NAME, food.name)
        contentValues.put(FORM, food.form)

        val success = db.update(TBL_FOOD, contentValues, "id=" + food.id, null)
        db.close()
        return success
    }

    fun deleteFoodById(id:Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID,id)
        val success = db.delete(TBL_FOOD, "id=$id", null)
        db.close()
        return success
    }
}