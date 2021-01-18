package com.ismail.mynotes

import android.view.View
import com.ismail.mynotes.db.NoteItem

interface AdapterCallBack {
    fun checkBoxListener(view : View, isChecked : Boolean, noteItem: NoteItem, position: Int)

}