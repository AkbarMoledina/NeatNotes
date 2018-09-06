package com.akbar26.neatnotes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MainActivity : AppCompatActivity() {

    var notesList = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        LoadQuery("%")

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val intent = Intent(this, EditNoteActivity::class.java)
            startActivity(intent)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun LoadQuery(title: String) {
        var dbManager = DbManager(this)
        val projections = arrayOf("Id", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections, "Title like ?", selectionArgs, "Title")
        notesList.clear()
        if (cursor.moveToFirst()) {
            do {
                val Id = cursor.getInt(cursor.getColumnIndex("Id"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))

                notesList.add(Note(Id, Title, Description))
            } while (cursor.moveToNext())
        }

        var notesAdapter = MyNotesAdaptor(this, notesList)
        notes_list.adapter = notesAdapter
    }

    inner class MyNotesAdaptor : BaseAdapter {
        var notesListAdapter = ArrayList<Note>()
        var context: Context?=null

        constructor(context: Context?, notesListAdapter: ArrayList<Note>) : super() {
            this.notesListAdapter = notesListAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            //inflate list
            var myView = layoutInflater.inflate(R.layout.list_item, null)
            var myNote = notesListAdapter[position]
            myView.title_text_view.text = myNote.nodeName
            myView.note_text_view.text = myNote.nodeDesc
            //delete and edit note button click will go here if needed (47)
            myView.title_text_view.setOnClickListener {
                GoToUpdateFun(myNote)
            }
            return myView
        }

        override fun getItem(position: Int): Any {
            return notesListAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesListAdapter.size
        }

    }

    private fun GoToUpdateFun(myNote: Note) {
        var intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra("Id", myNote.nodeId)
        intent.putExtra("Name", myNote.nodeName)
        intent.putExtra("Desc", myNote.nodeDesc)
        startActivity(intent)
    }
}
