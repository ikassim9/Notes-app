package com.ismail.mynotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.ismail.mynotes.Constants.Constant
import kotlinx.android.synthetic.main.activity_notes.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {
   private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    private var dateFormatter = SimpleDateFormat("MMM dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        toolbar = findViewById(R.id.toolBar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        if (intent.hasExtra(Constant.ID_KEY)) {
            title = "Edit Note"
            val title = intent.getStringExtra(Constant.TITLE_KEY)
            val description = intent.getStringExtra(Constant.DESCRIPTION_KEY)
            editxt_title_view.setText(title)
            edittxt_description.setText(description)
        } else {
            title = "Add Note"
        }
    }
    private fun saveNote() {
        if (edittxt_description.text.toString().isBlank() && editxt_title_view.text.toString()
                .isBlank()
        ) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show()
            return
        }
        val data = Intent()
        var title = editxt_title_view.text.toString()
        val description = edittxt_description.text.toString()
        val calendar = Calendar.getInstance().time
        val creationDate = dateFormatter.format(calendar)

        if(title.isEmpty()){
            title =description
            }

        data.putExtra(Constant.TITLE_KEY, title)
        data.putExtra(Constant.DESCRIPTION_KEY, description)
        data.putExtra(Constant.DESCRIPTION_KEY, description)
        data.putExtra(Constant.CREATION_DATE, creationDate)
        val id: Int = intent.getIntExtra(Constant.ID_KEY, -1)
        if (id != -1) {
            data.putExtra(Constant.ID_KEY, id)
        }
        Log.i("pass_data", "Title_Data is $title and Description_data is $description creation date is $creationDate")
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

