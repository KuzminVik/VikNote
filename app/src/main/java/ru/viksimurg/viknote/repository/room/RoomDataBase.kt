package ru.viksimurg.viknote.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Note::class, Folder::class), version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {

    abstract fun notesDao(): NotesDao
    abstract fun foldersDao(): FoldersDao
}