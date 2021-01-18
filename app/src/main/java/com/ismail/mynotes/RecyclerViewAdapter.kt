package com.ismail.mynotes

//import kotlinx.android.synthetic.main.list_item.view.description_text_view
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ismail.mynotes.db.NoteItem
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(
    private val longClickListener: (NoteItem, Int) -> Boolean,
    private val clickListener: (NoteItem, Int) -> Unit

) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(), Filterable {
    private var noteList: ArrayList<NoteItem> = ArrayList()
    lateinit var searchList: List<NoteItem>


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
        noteList = noteItem as ArrayList<NoteItem>
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int): NoteItem {
        return noteList[position]
    }

    fun addItem(position: Int): NoteItem {
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

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            searchList = ArrayList(noteList)
        }

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

// Filter search results
override fun getFilter(): Filter {
    return object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterList: ArrayList<NoteItem> = ArrayList()
            // if search field is empty(user has not search yet), show all results(cardViews)
            if (constraint == null || constraint.isEmpty()) {
//
                filterList.addAll(searchList)
                notifyDataSetChanged()

            } else {
                val filterPattern: String = constraint.toString().toLowerCase(Locale.ROOT)
                    .trim()
                for (item: NoteItem in searchList) {
                    if (item.title?.toLowerCase(Locale.ROOT)?.contains(filterPattern) == true ||
                        item.description?.toLowerCase(Locale.ROOT)
                            ?.contains(filterPattern) == true
                    ) {
                        filterList.add(item)
                    }
                }
            }
            val searchResult = FilterResults()
            searchResult.values = filterList
            return searchResult
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            noteList.clear()
            //     notifyDataSetChanged()
//                if(constraint == null || constraint.length == 0){
//
//                }
            noteList.addAll((results?.values) as List<NoteItem>)
            notifyDataSetChanged()

        }
    }
}
}

