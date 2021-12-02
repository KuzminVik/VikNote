package ru.viksimurg.viknote.repository.room

import androidx.room.*

@Dao
interface NotesDao{
    @Query("SELECT * FROM Note")
    suspend fun getAll(): List<Note>

    @Query("SELECT * FROM Note WHERE name = :name")
    suspend fun getByName(name: String): Note

    @Query("SELECT * FROM Note WHERE priority = :priority")
    suspend fun getByPriority(priority: Int): List<Note>

    @Query("SELECT * FROM Note WHERE folder = :folderId")
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
    @Query("SELECT * FROM Folder")
    suspend fun getAll(): List<Folder>

    @Query("SELECT * FROM Folder WHERE name = :name")
    suspend fun getByName(name: String): Folder

    @Query("SELECT * FROM Folder WHERE priority = :priority")
    suspend fun getByPriority(priority: Int): List<Folder>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: Folder)

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)
}