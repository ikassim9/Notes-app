package com.ismail.mynotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ismail.mynotes.db.NoteRepositroy


class NoteViewModelFactory(private val repository: NoteRepositroy) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
