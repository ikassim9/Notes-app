package com.ismail.mynotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notes.*

class NoteActivity : AppCompatActivity() {

    companion object {
        const val TITLE_KEY = "TITLE_KEY"
        const val ID_KEY = "ID_KEY"
        const val DESCRIPTION_KEY = "DESCRIPTION_KEY"
        const val ADD_NOTE_REQUEST: Int = 1
        const val EDIT_NOTE_REQUEST: Int = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        val intent = intent
        if (intent.hasExtra(ID_KEY)) {
            title = "Edit Note"
            val title = intent.getStringExtra(TITLE_KEY)
            val description = intent.getStringExtra(DESCRIPTION_KEY)
            editxt_title_view.setText(title)
            edittxt_description.setText(description)
        } else {
            title = "Add Note"


        }
    }


    private fun saveNote() {
        if (edittxt_description.text.toString().isBlank() || editxt_title_view.text.toString()
                .isBlank()
        ) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show()
            return

        }


        val data = Intent()
        val title = editxt_title_view.text.toString()
        val description = edittxt_description.text.toString()
        data.putExtra(TITLE_KEY, title)
        data.putExtra(DESCRIPTION_KEY, description)
        val id: Int = intent.getIntExtra(ID_KEY, -1)
        if (id != -1) {
            data.putExtra(ID_KEY, id)
        }
        Log.i("pass_data", "Title_Data is $title and Description_data is $description")

        setResult(Activity.RESULT_OK, data)
        finish()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.add_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                Log.i("save_note", "Note has been saved")
                return true

            }
        }
        return super.onOptionsItemSelected(item)
    }

}

