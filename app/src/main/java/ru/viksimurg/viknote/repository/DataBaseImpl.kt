package ru.viksimurg.viknote.repository

import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.FoldersDao
import ru.viksimurg.viknote.repository.room.Note
import ru.viksimurg.viknote.repository.room.NotesDao

class DataBaseImpl(
    private val notesDao: NotesDao,
    private val foldersDao: FoldersDao
    ): DataBase {

    // Notes >>>>
    override suspend fun getListNotes(): List<Note> {
        return notesDao.getAll()
    }

    override suspend fun getNoteByName(name: String): Note {
        return notesDao.getByName(name)
    }

    override suspend fun getListNotesByFolder(folderId: Int): List<Note> {
        return notesDao.getByFolder(folderId)
    }

    override suspend fun getNoteByPriority(priority: Int): List<Note> {
        return notesDao.getByPriority(priority)
    }

    override suspend fun getNoteById(id: Int): Note {
        return notesDao.getById(id)
    }

    override suspend fun saveNote(note: Note) {
        notesDao.insert(note)
    }

    override suspend fun saveNewNote(name: String, text: String?, folderId: Int, priority: Int){
        val note = Note(name = name, text = text, folderId = folderId, priority = priority)
        notesDao.insert(note)
    }

    override suspend fun deleteNote(id: Int) {
        notesDao.delete(id)
    }

    override suspend fun updateNote(id: Int, name: String, text: String?, folderId: Int, priority: Int) {
        val note = Note(id = id, name = name, text = text, folderId = folderId, priority = priority)
        notesDao.update(note)
    }

    // Folders >>>>
    override suspend fun getListFolders(): List<Folder> {
        return  foldersDao.getAll()
    }

    override suspend fun getFolderByName(name: String): Folder {
        return foldersDao.getByName(name)
    }

    override suspend fun getFolderByPriority(priority: Int): List<Folder> {
        return foldersDao.getByPriority(priority)
    }

    override suspend fun getFolderById(id: Int): Folder {
        return foldersDao.getById(id)
    }

    override suspend fun saveFolder(folder: Folder) {
        foldersDao.insert(folder)
    }

    override suspend fun saveNewFolder(name: String, priority: Int){
        val folder = Folder(name = name, priority = priority)
        foldersDao.insert(folder)
    }

    override suspend fun deleteFolder(id: Int) {
        foldersDao.delete(id)
    }

    override suspend fun updateFolder(id: Int, name: String, priority: Int, countNotes: Int) {
        val folder = Folder(id = id, name = name, priority = priority, countNotes = countNotes)
        foldersDao.update(folder)
    }

    override suspend fun getCountNotes(id: Int): Int {
        return foldersDao.getCountNotes(id)
    }

    override suspend fun updateCountNotes(id: Int, editValue: Int) {
        val oldValue = foldersDao.getCountNotes(id)
        foldersDao.updateCountNotes(id, oldValue+editValue)
    }

    override suspend fun getNameFolder(id: Int): String {
        return foldersDao.getNameFolder(id).nameFolder
    }
}