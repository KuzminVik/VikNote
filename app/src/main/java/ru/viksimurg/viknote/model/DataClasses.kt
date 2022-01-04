package ru.viksimurg.viknote.model

import androidx.room.ColumnInfo

data class Count(
    @ColumnInfo(name = "count_notes")val countNotes: Int
)

data class NameFolder(
    @ColumnInfo(name = "name")val nameFolder: String
)
