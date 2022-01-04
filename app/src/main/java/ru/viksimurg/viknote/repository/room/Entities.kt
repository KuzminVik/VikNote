package ru.viksimurg.viknote.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "folder") val folderId: Int,
    @ColumnInfo(name = "priority") val priority: Int = 0
)

@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "priority") val priority: Int = 0,
    @ColumnInfo(name = "count_notes")val countNotes: Int = 0
)