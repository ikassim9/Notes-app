package com.ismail.mynotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.mynotes.db.NoteItem
import kotlinx.android.synthetic.main.cardview.view.*
import kotlinx.android.synthetic.main.list_item.view.*
//import kotlinx.android.synthetic.main.list_item.view.description_text_view
import kotlinx.android.synthetic.main.list_item.view.title_text_view

class RecyclerViewAdapter(
    private val longClickListener: (NoteItem, Int) -> Boolean,
    private val clickListener: (NoteItem, Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    private var noteList: List<NoteItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {
        val currentItem = noteList[position]
        holder.bind(currentItem, longClickListener, clickListener)
    }
    override fun getItemCount(): Int = noteList.size

    fun setList(noteItem: List<NoteItem>) {
        val oldList = noteList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            NoteItemDiffCall(oldList, noteItem)
        )
        noteList = noteItem
        diffResult.dispatchUpdatesTo(this)
    }
    fun removeItem(position: Int): NoteItem {
        return noteList[position]
    }

    fun addItem(position: Int) : NoteItem{
        return noteList[position]
    }

    class NoteItemDiffCall(var oldNoteList: List<NoteItem>, var newNoteList: List<NoteItem>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id)
        }
        override fun getOldListSize(): Int {
            return oldNoteList.size
        }
        override fun getNewListSize(): Int {
            return newNoteList.size
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldNoteList[oldItemPosition] == (newNoteList[newItemPosition])
        }
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            noteItem: NoteItem,
            longClickListener: (NoteItem, Int) -> Boolean,
            clickListener: (NoteItem, Int) -> Unit
        ) {
            val position = adapterPosition
            itemView.title_text_view.text = noteItem.title
            itemView.description_text_view.text = noteItem.description
            itemView.creationUpdateDate_text_view.text = noteItem.creation_date
            itemView.setOnLongClickListener() {
                longClickListener(noteItem, position)
            }
            itemView.setOnClickListener() {
                clickListener(noteItem, position)
            }
        }
    }
}


