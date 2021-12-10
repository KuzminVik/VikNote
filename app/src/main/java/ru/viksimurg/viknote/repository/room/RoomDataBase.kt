package ru.viksimurg.viknote.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(Note::class, Folder::class), version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {

    abstract fun notesDao(): NotesDao
    abstract fun foldersDao(): FoldersDao

}