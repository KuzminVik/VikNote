package ru.viksimurg.viknote.model

import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.Note

sealed class EditingModeState {
    data class FolderState(val folder: Folder?): EditingModeState()
    data class NoteState(val data: Pair<List<Folder>?, Note?>?): EditingModeState()
    data class Error(val error: Throwable): EditingModeState()

}