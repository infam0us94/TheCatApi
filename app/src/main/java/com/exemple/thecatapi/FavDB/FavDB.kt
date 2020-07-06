package com.exemple.thecatapi.FavDB

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class FavDB(context: Context?): SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION ){

    var FAVORITE_STATUS = "fStatus"
    private val CREATE_TABLE =
        "CREATE TABLE $TABLE_NAME ($KEY_ID TEXT, $ITEM_IMAGE TEXT, $FAVORITE_STATUS TEXT)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
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

    fun read_all_data(id: String): Cursor {
        val db = this.readableDatabase
        val sql =
            "select * from $TABLE_NAME where $KEY_ID=$id"
        return db.rawQuery(sql, null, null)
    }

    fun remove_fav(id: String) {
        val db = this.writableDatabase
        val sql = "UPDATE $TABLE_NAME SET $FAVORITE_STATUS ='0' WHERE $KEY_ID=$id"
        db.execSQL(sql)
        Log.d("remove", id)
    }

    fun select_all_favorite_list(): Cursor {
        val db = this.readableDatabase
        val sql =
            "SELECT * FROM $TABLE_NAME WHERE $FAVORITE_STATUS ='1'"
        return db.rawQuery(sql, null, null)
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DATABASE_NAME = "CatDB"
        private const val TABLE_NAME = "favoriteTable"

        var KEY_ID = "id"
        var ITEM_IMAGE = "itemImage"

    }
}