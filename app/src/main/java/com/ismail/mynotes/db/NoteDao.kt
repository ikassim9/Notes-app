package com.ismail.mynotes.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun insertNote(noteItem: NoteItem): Long

    @Delete
    suspend fun deleteNote(noteItem: NoteItem): Int

    @Update
    suspend fun updateNote(noteItem: NoteItem): Int

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes(): Int

    @Query("SELECT * FROM note_table order by note_id desc")

    fun getAllNotes(): LiveData<List<NoteItem>>

}