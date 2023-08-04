package com.rbppl.notepad
import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "note_database"
        private const val DATABASE_VERSION = 1

        const val TABLE_NOTE = "notes"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_TEXT = "text"

        private const val CREATE_TABLE_NOTE =
            "CREATE TABLE $TABLE_NOTE (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_TITLE TEXT, " +
                    "$COLUMN_TEXT TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTE")
        onCreate(db)
    }
    @SuppressLint("Range")
    fun getAllNotes(): MutableList<Note> {
        val notes = mutableListOf<Note>()
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID, COLUMN_TITLE, COLUMN_TEXT)
        val sortOrder = "$COLUMN_ID DESC"
        val cursor = db.query(TABLE_NOTE, columns, null, null, null, null, sortOrder)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                val text = cursor.getString(cursor.getColumnIndex(COLUMN_TEXT))
                notes.add(Note(id, title, text))
            } while (cursor.moveToNext())

            cursor.close()
        }

        db.close()
        return notes
    }

    // Метод для добавления новой записи в базу данных
    fun addNote(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_TEXT, note.text)
        }
        db.insert(TABLE_NOTE, null, values)
        db.close()
    }

    // Метод для обновления существующей записи в базе данных
    fun updateNote(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_TEXT, note.text)
        }
        db.update(TABLE_NOTE, values, "$COLUMN_ID=?", arrayOf(note.id.toString()))
        db.close()
    }
    fun deleteNote(note: Note) {
        val db = this.writableDatabase
        db.delete(TABLE_NOTE, "$COLUMN_ID=?", arrayOf(note.id.toString()))
        db.close()
    }
}
