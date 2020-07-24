package com.ismail.mynotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ismail.mynotes.db.NoteItem
import com.ismail.mynotes.db.NoteRepositroy
import kotlinx.coroutines.launch


class NoteViewModel(private val repository: NoteRepositroy) : ViewModel() {
    val notes: LiveData<List<NoteItem>> = repository.notes

    fun insert(note: NoteItem) {
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun update(note: NoteItem) {
        viewModelScope.launch {
            repository.update(note)
        }
    }

    fun delete(note: NoteItem) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            repository.deleteAll()
        }

    }
}