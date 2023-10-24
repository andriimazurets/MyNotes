package com.mazurets.mynotes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val PREFS_NAME = "MyPrefs"
    private val PREF_KEY_NOTES = "notes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonAddNote: Button = findViewById(R.id.button_addNote)
        val textAdd: EditText = findViewById(R.id.text_addText)
        val listContent: ListView = findViewById(R.id.list_content)
        val listElement = mutableListOf<String>()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listElement)
        listContent.adapter = adapter

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedNotes = prefs.getStringSet(PREF_KEY_NOTES, HashSet<String>())?.toMutableList()
        if (savedNotes != null) {
            listElement.addAll(savedNotes)
            adapter.notifyDataSetChanged()
        }

        listContent.setOnItemClickListener { parent, view, position, id ->
            val item = adapter.getItem(position)
            adapter.remove(item)
            adapter.notifyDataSetChanged()
            saveNotesToSharedPreferences(listElement)
            Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT ).show()
        }

        buttonAddNote.setOnClickListener {
            val text = textAdd.text.toString().trim()
            if (text.isNotEmpty()) {
                adapter.add(text)
                saveNotesToSharedPreferences(listElement)
                Toast.makeText(this, "Note added!", Toast.LENGTH_SHORT).show()
                textAdd.text.clear()
            }
        }
    }

    private fun saveNotesToSharedPreferences(notes: List<String>) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putStringSet(PREF_KEY_NOTES, notes.toSet())
        editor.apply()
    }
}

