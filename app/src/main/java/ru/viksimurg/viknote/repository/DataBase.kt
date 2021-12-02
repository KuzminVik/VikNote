package ru.viksimurg.viknote.repository

import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.Note

interface DataBase {

    suspend fun getListNotes(): List<Note>
    suspend fun getNoteByName(name: String): Note
    suspend fun getNoteByFolder(folderId: Int): List<Note>
    suspend fun getNoteByPriority(priority: Int): List<Note>
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)

    suspend fun getListFolders(): List<Folder>
    suspend fun getFolderByName(name: String): Folder
    suspend fun getFolderByPriority(priority: Int): List<Folder>
    suspend fun saveFolder(folder: Folder)
    suspend fun deleteFolder(folder: Folder)
    suspend fun updateFolder(folder: Folder)
}