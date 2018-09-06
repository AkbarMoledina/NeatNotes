package com.akbar26.neatnotes

import android.content.ContentValues
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.android.synthetic.main.list_item.*

class EditNoteActivity : AppCompatActivity() {

    val dbTable = "Notes"
    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        save_button.setOnClickListener { view ->
            addNote(view)
        }

        try {
            val bundle:Bundle = intent.extras
            id = bundle.getInt("Id", 0)
            if (id!=0){
                save_button.text = "Update"
                title_edit_text.setText(bundle.getString("name"))
                note_edit_text.setText(bundle.getString("desc"))
            }
        } catch (ex:Exception){}
    }

    fun addNote(view:View) {
        var dbManager = DbManager(this)

        var values = ContentValues()
        values.put("Title", title_edit_text.text.toString())
        values.put("Description", note_edit_text.text.toString())

        if (id == 0) {
            val Id = dbManager.insert(values)
            if (Id>0) {
                Toast.makeText(this, "Note Added!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(this, "Error adding note", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            var selectionArgs = arrayOf(id.toString())
            val Id = dbManager.update(values, "Id=?", selectionArgs)
            if (Id>0) {
                Toast.makeText(this, "Note Added!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Toast.makeText(this, "Error adding note", Toast.LENGTH_SHORT).show()
            }
        }
    }

}