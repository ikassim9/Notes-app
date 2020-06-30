package com.ismail.mynotes.db

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(noteItem: NoteItem)

    @Delete
    suspend fun deleteNote(noteItem: NoteItem)

    @Update
    suspend fun updateNote(noteItem: NoteItem)

    @Query("DELETE FROM note_table")
   suspend fun deleteAllNotes()

    @Query("SELECT * FROM note_table")

    fun getAllNotes(): LiveData<List<NoteItem>>

}