package com.akbar26.neatnotes

import android.app.SearchManager
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
import android.widget.SearchView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.list_item.view.*

class MainActivity : AppCompatActivity() {

    var notesList = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        loadQuery("%")

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            val intent = Intent(this, EditNoteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val sv: android.support.v7.widget.SearchView = menu!!.findItem(R.id.app_bar_search).actionView as android.support.v7.widget.SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))

        sv.setOnQueryTextListener(object: android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                loadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadQuery("%" + newText + "%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun loadQuery(title: String) {
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


            //edit button action
            myView.editBtn.setOnClickListener {
                GoToUpdateFun(myNote)
            }

            //share button action
            myView.shareBtn.setOnClickListener {
                //get title
                val title = myView.title_text_view.text.toString()
                //get description
                val desc = myView.note_text_view.text.toString()
                //concatenate
                val s = title +"\n"+ desc
                //share intent
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, s)
                startActivity(Intent.createChooser(shareIntent, s))
            }

            //delete button action
            myView.deleteBtn.setOnClickListener {
                var dbManager = DbManager(this.context!!)
                val selectionArgs = arrayOf(myNote.nodeId.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
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
