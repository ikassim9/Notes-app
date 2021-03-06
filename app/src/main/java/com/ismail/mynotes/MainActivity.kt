package com.ismail.mynotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.ismail.mynotes.Constants.Constant
import com.ismail.mynotes.db.NoteDao
import com.ismail.mynotes.db.NoteDatabase
import com.ismail.mynotes.db.NoteItem
import com.ismail.mynotes.db.NoteRepositroy
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var noteAdapter: RecyclerViewAdapter
    lateinit var toolbar: Toolbar
    private lateinit var selectedTheme: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "My notes"
        title = "Note App"
        selectedTheme = getSharedPreferences("index", Context.MODE_PRIVATE)
        setUpViewModel()
        setUpRecyclerView()
        setUpSwipeHandler()
        setUpFab()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTheme()
    }

    private fun setTheme() {
        val getPrefTheme = selectedTheme.getInt("index", -1)

        when (getPrefTheme) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

            }
        }
    }

    private fun noteItemShortClick(noteItem: NoteItem) {
        updateNote(noteItem)
    }

    private fun noteItemLongClickListener(noteItem: NoteItem, position: Int): Boolean {
        val dialogBuilder = MaterialAlertDialogBuilder(this, R.style.DialogTheme)
        dialogBuilder.setMessage("Do you want to delete this note?")
            .setTitle("Delete Note")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                viewModel.delete(noteItem)
                undoNoteDeletionSnackBar(noteItem)
                Log.i("note_delete", "$noteItem is deleted at position $position")
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }.show()
        return true
    }

    private fun updateNote(noteItem: NoteItem) {
        val intent = Intent(this@MainActivity, NoteActivity::class.java)
        intent.putExtra(Constant.TITLE_KEY, noteItem.title)
        intent.putExtra(Constant.DESCRIPTION_KEY, noteItem.description)
        intent.putExtra(Constant.ID_KEY, noteItem.id)
        startActivityForResult(intent, Constant.EDIT_NOTE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constant.ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title: String? = data?.getStringExtra(Constant.TITLE_KEY)
            val description: String? = data?.getStringExtra(Constant.DESCRIPTION_KEY)
            val creationDate: String? = data?.getStringExtra(Constant.CREATION_DATE)
            val note = NoteItem(0, title, description, creationDate)
            viewModel.insert(note)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()

        } else if (requestCode == Constant.EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id: Int? = data?.getIntExtra(Constant.ID_KEY, -1)
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                Log.i("-1", "note has -1 as id")
                return
            }
            val creationDate: String? = data?.getStringExtra(Constant.CREATION_DATE)
            val title: String? = data?.getStringExtra(Constant.TITLE_KEY)
            val description: String? = data?.getStringExtra(Constant.DESCRIPTION_KEY)
            val note = id?.let { NoteItem(it, title, description, creationDate) }
            if (note != null) {
                viewModel.update(note)
            }
            Log.i("note_update", "$note is updated and id is $id and note id is $note.id")
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Note not updated", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        setUpSearchFilter(menu)

        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpSearchFilter(menu: Menu?): Boolean {

        val searchItem: MenuItem? = menu?.findItem(R.id.search_filter)


        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return true
            }
        })

//        searchItem?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.queryHint = "find your notes"


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapter.filter.filter(newText)
                noteAdapter.notifyDataSetChanged()
                return false

            }
        })

        return true
    }


    private fun handleBackPressForSearchView() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                deleteAllNotes()
                return true
            }

            R.id.settings -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotes() {
        val dialogBuilder = MaterialAlertDialogBuilder(this, R.style.DialogTheme)
        dialogBuilder.setTitle("Delete all notes?")
        dialogBuilder.setMessage("Do you want to delete all your notes?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                viewModel.deleteAllNotes()
                Toast.makeText(this, "All notes have been deleted", Toast.LENGTH_SHORT).show()
                Log.i("note_delete", "All notes are deleted")
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }.show()
    }

    private fun setUpSwipeHandler() {
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val dialogBuilder =
                    MaterialAlertDialogBuilder(viewHolder.itemView.context, R.style.DialogTheme)
                dialogBuilder.setTitle("Delete Note")
                dialogBuilder.setMessage("Do you want to delete this note?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.delete(noteAdapter.removeItem(viewHolder.adapterPosition))
                        undoNoteDeletionSnackBar(noteAdapter.addItem(viewHolder.adapterPosition))
                        Log.i("swipe", "note is deleted at position ${viewHolder.adapterPosition}")
                    }
                    .setNegativeButton("Cancel") { dialog, id ->
                        noteAdapter.notifyItemChanged(viewHolder.adapterPosition)
                    }.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        noteAdapter =
            RecyclerViewAdapter(
                { noteItem: NoteItem, position: Int ->
                    noteItemLongClickListener(
                        noteItem,
                        position
                    )
                },
                { note: NoteItem, position: Int -> noteItemShortClick(note) })
        recyclerView.adapter = noteAdapter
    }

    private fun setUpViewModel() {
        val dao: NoteDao = NoteDatabase.getInstance(applicationContext).noteDao
        val repository = NoteRepositroy(dao)
        val factory = NoteViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NoteViewModel::class.java)
        viewModel.notes.observe(this, Observer {
            noteAdapter.setList(it)
            Log.i("list_set", "$it")
        })
    }

    private fun setUpFab() {
        fabBtn.setOnClickListener {
            val intent = Intent(this, NoteActivity::class.java)
            startActivityForResult(intent, Constant.ADD_NOTE_REQUEST)
        }
    }

    private fun undoNoteDeletionSnackBar(noteItem: NoteItem) {
        val snackbar: Snackbar =
            Snackbar.make(coordinatorLayout, "Note has been deleted", Snackbar.LENGTH_SHORT)
        snackbar.apply {
            setActionTextColor(Color.parseColor("#056ffa"))
            //  setTextColor(Color.parseColor("#ffffff"))
            snackbar.setAction("Undo") {
                undoDelete(noteItem)
            }
        }
        snackbar.show()
    }

    private fun undoDelete(noteItem: NoteItem) {
        viewModel.insert(noteItem)
        Log.i("pos", "$noteItem")


    }

}
