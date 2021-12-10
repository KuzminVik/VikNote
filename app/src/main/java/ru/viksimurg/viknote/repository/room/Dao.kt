package ru.viksimurg.viknote.repository.room

import androidx.room.*

@Dao
interface NotesDao{
    @Query("SELECT * FROM notes")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM notes WHERE name = :name")
    suspend fun getByName(name: String): Note

    @Query("SELECT * FROM notes WHERE priority = :priority")
    suspend fun getByPriority(priority: Int): List<Note>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Int): Note

    @Query("SELECT * FROM notes WHERE folder = :folderId")
    suspend fun getByFolder(folderId: Int): List<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}

@Dao
interface FoldersDao{
    @Query("SELECT * FROM folders")
    suspend fun getAll(): List<Folder>

    @Query("SELECT * FROM folders WHERE name = :name")
    suspend fun getByName(name: String): Folder

    @Query("SELECT * FROM folders WHERE priority = :priority")
    suspend fun getByPriority(priority: Int): List<Folder>

    @Query("SELECT * FROM folders WHERE id = :id")
    suspend fun getById(id: Int): Folder

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: Folder)

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)
}