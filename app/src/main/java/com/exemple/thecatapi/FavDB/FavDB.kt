package com.exemple.thecatapi.FavDB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class FavDB(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {


     val FAVORITE_STATUS = "fStatus"

    companion object {
        private const val DB_VERSION = 1
        private const val DATABASE_NAME = "CatDB"
        private const val TABLE_NAME = "favoriteTable"

        const val KEY_ID = "id"
        const val ITEM_IMAGE = "itemImage"

    }


    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE =
            ("CREATE TABLE $TABLE_NAME ($KEY_ID TEXT, $ITEM_IMAGE TEXT, $FAVORITE_STATUS TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertEmpty() {
        val db = this.writableDatabase
        val cv = ContentValues()
        for (x in 1..10) {
            cv.put(KEY_ID, x)
            cv.put(FAVORITE_STATUS, "0")
            db.insert(TABLE_NAME, null, cv)
        }
    }

    fun insertIntoTheDatabase(
        item_image: String,
        id: String?,
        fav_status: String
    ) {
        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()
        cv.put(ITEM_IMAGE, item_image)
        cv.put(KEY_ID, id)
        cv.put(FAVORITE_STATUS, fav_status)
        db.insert(TABLE_NAME, null, cv)
    }

    fun readAllData(id: String): Cursor {
        val db = this.readableDatabase
        val sql =
            "SELECT * FROM $TABLE_NAME WHERE $KEY_ID= '?'"
        return db.rawQuery(sql, null, null)
    }

    fun removeFav(id: String) {
        val db = this.writableDatabase
        val sql = "UPDATE $TABLE_NAME SET $FAVORITE_STATUS ='0' WHERE $KEY_ID= '?'"
        db.execSQL(sql)
        Log.d("remove", id)
    }

    fun selectAllFavoriteList(): Cursor {
        val db = this.readableDatabase
        val sql =
            "SELECT * FROM $TABLE_NAME WHERE $FAVORITE_STATUS ='1'"
        return db.rawQuery(sql, null, null)
    }
}