package ru.viksimurg.viknote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.FoldersDao
import ru.viksimurg.viknote.repository.room.Note
import ru.viksimurg.viknote.repository.room.NotesDao

class DataBaseImpl(
    private val notesDao: NotesDao,
    private val foldersDao: FoldersDao
    ): DataBase {

    // Notes >>>>
    override suspend fun getListNotes(): List<Note> =
    withContext(Dispatchers.IO) { notesDao.getAll() }

    override suspend fun getNoteByName(name: String): Note =
        withContext(Dispatchers.IO) { notesDao.getByName(name) }

    override suspend fun getListNotesByFolder(folderId: Int): List<Note> =
        withContext(Dispatchers.IO) { notesDao.getByFolder(folderId) }

    override suspend fun getNoteByPriority(priority: Int): List<Note> =
        withContext(Dispatchers.IO) { notesDao.getByPriority(priority) }

    override suspend fun getNoteById(id: Int): Note =
        withContext(Dispatchers.IO) { notesDao.getById(id) }

    override suspend fun saveNote(note: Note) =
        withContext(Dispatchers.IO) { notesDao.insert(note) }

    override suspend fun saveNewNote(name: String, text: String?, folderId: Int, priority: Int, date: String){
        val note = Note(name = name, text = text, folderId = folderId, priority = priority, date = date)
        withContext(Dispatchers.IO) { notesDao.insert(note) }
    }

    override suspend fun deleteNote(id: Int) =
        withContext(Dispatchers.IO) { notesDao.delete(id) }

    override suspend fun updateNote(id: Int, name: String, text: String?, folderId: Int, priority: Int, date: String) {
        val note = Note(id = id, name = name, text = text, folderId = folderId, priority = priority, date = date)
        withContext(Dispatchers.IO) { notesDao.update(note) }
    }

    // Folders >>>>
    override suspend fun getListFolders(): List<Folder> =
        withContext(Dispatchers.IO) { foldersDao.getAll() }

    override suspend fun getFolderByName(name: String): Folder =
        withContext(Dispatchers.IO) { foldersDao.getByName(name) }

    override suspend fun getFolderByPriority(priority: Int): List<Folder> =
        withContext(Dispatchers.IO) { foldersDao.getByPriority(priority) }

    override suspend fun getFolderById(id: Int): Folder =
        withContext(Dispatchers.IO) { foldersDao.getById(id) }

    override suspend fun saveFolder(folder: Folder) =
        withContext(Dispatchers.IO) { foldersDao.insert(folder) }

    override suspend fun saveNewFolder(name: String, priority: Int){
        val folder = Folder(name = name, priority = priority)
        withContext(Dispatchers.IO) { foldersDao.insert(folder) }
    }

    override suspend fun deleteFolder(id: Int) =
        withContext(Dispatchers.IO) { foldersDao.delete(id) }

    override suspend fun updateFolder(id: Int, name: String, priority: Int, countNotes: Int) {
        val folder = Folder(id = id, name = name, priority = priority, countNotes = countNotes)
        withContext(Dispatchers.IO) { foldersDao.update(folder) }
    }

    override suspend fun getCountNotes(id: Int): Int =
        withContext(Dispatchers.IO) { foldersDao.getCountNotes(id) }

    override suspend fun updateCountNotes(id: Int, editValue: Int) {
        val oldValue = foldersDao.getCountNotes(id)
        withContext(Dispatchers.IO) { foldersDao.updateCountNotes(id, oldValue+editValue) }
    }

    override suspend fun getNameFolder(id: Int): String =
        withContext(Dispatchers.IO) { foldersDao.getNameFolder(id).nameFolder }

}