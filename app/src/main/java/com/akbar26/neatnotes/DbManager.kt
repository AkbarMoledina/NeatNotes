package com.akbar26.neatnotes

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.widget.Toast

class DbManager {

    //database name
    var dbName = "MyNotes"
    //table name
    var dbTable = "Notes"
    //column names
    var colId = "Id"
    var colTitle = "Title"
    var colDesc = "Description"
    //db version
    var dbVersion = 1

    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colId + " INTEGER PRIMARY KEY," + colTitle + " TEXT, "+ colDesc + " TEXT);"
    var sqlDB:SQLiteDatabase?=null

    constructor(context: Context){
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes:SQLiteOpenHelper{
        var context: Context?=null
        constructor(context: Context):super(context,dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(this.context, "Database Created", Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
            db!!.execSQL("Drop table if Exists" + dbTable)
        }
    }


    fun insert(values:ContentValues):Long{
        val Id = sqlDB!!.insert(dbTable, "", values)
        return Id
    }

    fun Query(projection:Array<String>, selection:String, selectionArgs:Array<String>, sorOrder:String):Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB, projection, selection, selectionArgs, null, null, sorOrder)
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>):Int {
        val count = sqlDB!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>):Int {
        val count = sqlDB!!.update(dbTable, values, selection, selectionArgs)
        return count
    }

}
