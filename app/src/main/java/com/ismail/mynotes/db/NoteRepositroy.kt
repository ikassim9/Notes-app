package com.ismail.mynotes.db

import androidx.lifecycle.LiveData

class NoteRepositroy(private val dao: NoteDao) {
    val notes = dao.getAllNotes()


    suspend fun insert(noteItem: NoteItem) : Long {
        return dao.insertNote(noteItem)
    }

    suspend fun delete(noteItem: NoteItem) : Int{
        return dao.deleteNote(noteItem = noteItem)
    }

    suspend fun update(noteItem: NoteItem): Int {
        return dao.updateNote(noteItem)
    }

    suspend fun deleteAll() : Int{
        return dao.deleteAllNotes()
    }
}

