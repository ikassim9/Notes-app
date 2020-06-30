package com.ismail.mynotes.db

import androidx.lifecycle.LiveData

class NoteRepositroy(private val dao: NoteDao) {
    val notes = dao.getAllNotes()


    suspend fun insert(noteItem: NoteItem) {
        return dao.insertNote(noteItem)
    }

    suspend fun delete(noteItem: NoteItem) {
        return dao.deleteNote(noteItem = noteItem)
    }

    suspend fun update(noteItem: NoteItem) {
        return dao.updateNote(noteItem)
    }

    suspend fun deleteAll() {
        return dao.deleteAllNotes()
    }
}

