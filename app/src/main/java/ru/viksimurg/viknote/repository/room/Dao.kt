package ru.viksimurg.viknote.repository.room

import androidx.room.*
import ru.viksimurg.viknote.model.Count
import ru.viksimurg.viknote.model.NameFolder

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

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Int)
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

    @Query("SELECT count_notes FROM folders WHERE id = :id")
    suspend fun getCountNotes(id: Int): Int

    @Query("UPDATE folders SET count_notes = :count WHERE id = :id")
    suspend fun updateCountNotes(id: Int, count: Int): Int

    @Query("SELECT name FROM folders WHERE id = :id")
    suspend fun getNameFolder(id: Int): NameFolder

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: Folder)

    @Update
    suspend fun update(folder: Folder)

    @Query("DELETE FROM folders WHERE id = :id")
    suspend fun delete(id: Int)
}