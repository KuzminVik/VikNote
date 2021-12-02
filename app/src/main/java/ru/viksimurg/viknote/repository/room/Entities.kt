package ru.viksimurg.viknote.repository.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "desc") val desc: String?,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "folder") val folderId: Int,
    @ColumnInfo(name = "priority") val priority: Int = 0
)

@Entity
class Folder(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "desc") val desc: String?,
    @ColumnInfo(name = "priority") val priority: Int = 0
)