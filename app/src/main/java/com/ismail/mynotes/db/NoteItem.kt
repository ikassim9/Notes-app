package com.ismail.mynotes.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteItem(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Int,

    @ColumnInfo(name = "note_title")
    val title: String?,


    @ColumnInfo(name = "note_description")
    val description: String?,


    @ColumnInfo(name = "creation_date")
    val creation_date: String?


)



