package ru.viksimurg.viknote.repository

import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.FoldersDao
import ru.viksimurg.viknote.repository.room.Note
import ru.viksimurg.viknote.repository.room.NotesDao

class DataBaseImpl(
    private val notesDao: NotesDao,
    private val foldersDao: FoldersDao
    ): DataBase {

    override suspend fun getListNotes(): List<Note> {
        return notesDao.getAll()
    }

    override suspend fun getNoteByName(name: String): Note {
        return notesDao.getByName(name)
    }

    override suspend fun getNoteByFolder(folderId: Int): List<Note> {
        return notesDao.getByFolder(folderId)
    }

    override suspend fun getNoteByPriority(priority: Int): List<Note> {
        return notesDao.getByPriority(priority)
    }

    override suspend fun saveNote(note: Note) {
        notesDao.insert(note)
    }

    override suspend fun deleteNote(note: Note) {
        notesDao.insert(note)
    }

    override suspend fun updateNote(note: Note) {
        notesDao.update(note)
    }

    override suspend fun getListFolders(): List<Folder> {
        return  foldersDao.getAll()
    }

    override suspend fun getFolderByName(name: String): Folder {
        return foldersDao.getByName(name)
    }

    override suspend fun getFolderByPriority(priority: Int): List<Folder> {
        return foldersDao.getByPriority(priority)
    }

    override suspend fun saveFolder(folder: Folder) {
        foldersDao.insert(folder)
    }

    override suspend fun deleteFolder(folder: Folder) {
        foldersDao.delete(folder)
    }

    override suspend fun updateFolder(folder: Folder) {
        foldersDao.update(folder)
    }
}