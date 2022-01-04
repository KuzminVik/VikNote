package ru.viksimurg.viknote.repository

import ru.viksimurg.viknote.model.Count
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.Note

interface DataBase {

    suspend fun getListNotes(): List<Note>
    suspend fun getNoteByName(name: String): Note
    suspend fun getListNotesByFolder(folderId: Int): List<Note>
    suspend fun getNoteByPriority(priority: Int): List<Note>
    suspend fun getNoteById(id: Int): Note
    suspend fun saveNote(note: Note)
    suspend fun saveNewNote(name: String, text: String?, folderId: Int, priority: Int)
    suspend fun deleteNote(id: Int)
    suspend fun updateNote(id: Int, name: String, text: String?, folderId: Int, priority: Int)

    suspend fun getListFolders(): List<Folder>
    suspend fun getFolderByName(name: String): Folder
    suspend fun getFolderByPriority(priority: Int): List<Folder>
    suspend fun getFolderById(id: Int): Folder
    suspend fun saveFolder(folder: Folder)
    suspend fun saveNewFolder(name: String, priority: Int)
    suspend fun deleteFolder(id: Int)
    suspend fun updateFolder(id: Int, name: String, priority: Int, countNotes: Int)

    suspend fun getCountNotes(id: Int): Int
    suspend fun updateCountNotes(id: Int, editValue: Int)
    suspend fun getNameFolder(id: Int): String
}