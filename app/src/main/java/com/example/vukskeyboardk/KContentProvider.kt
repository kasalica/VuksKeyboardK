package com.example.vukskeyboardk

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.vukskeyboardk.database.*


//МЫ НЕ ИСПОЛЬЗУЕМ ЭТОТ КОНТЕНТ ПРОВАЙДЕР. НО ОН ТУТ СИДИТ :)

class KContentProvider : ContentProvider() {

    private var myDB: DataBaseHandler? = null

    private val TABLE1 = 1
    private val TABLE2 = 2
    private val TABLE3 = 3

    private val sURIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sURIMatcher.addURI(AUTHORITY, "CHAR", TABLE1)
        sURIMatcher.addURI(AUTHORITY, "TIME", TABLE2)
        sURIMatcher.addURI(AUTHORITY, "ACCURACY", TABLE3)
    }

    companion object {
        val AUTHORITY = "com.example.vukskeyboardk.KContentProvider"
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun onCreate(): Boolean {
        myDB = context?.let {
            DataBaseHandler(
                it,
                null,
                null,
                1
            )
        }
        return false
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val uriType = sURIMatcher.match(uri)

        Log.d("URI TAJPPP " , uriType.toString())
        if(uriType == TABLE1) {
            return myDB!!.readableDatabase.query(
                TABLE1_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
        }

        if(uriType == TABLE2){
            return myDB!!.readableDatabase.query(
                TABLE2_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
        }
        if(uriType == TABLE3){
            return myDB!!.readableDatabase.query(
                TABLE3_NAME, projection, selection, selectionArgs, null, null, sortOrder
            )
        }

        else throw IllegalArgumentException("Unknown URI: " + uri)
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
