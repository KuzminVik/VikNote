package ru.viksimurg.viknote.model

import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.Note

sealed class AppState{
    data class SuccessListNotes(val data: List<Note>): AppState()
    data class SuccessListFolders(val data: List<Folder>): AppState()
    data class SuccessNote(val note: Note): AppState()
//    data class SuccessFolder(val folder: Folder): AppState()

    data class Error(val error: Throwable): AppState()

    data class Loading(val progress: Int?): AppState()
}
