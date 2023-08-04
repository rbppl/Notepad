package com.rbppl.notepad

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoteListAdapter(private val context: Context, private val notes: List<Note>) : ArrayAdapter<Note>(context, 0, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false)
        }

        val currentNote = notes[position]

        val titleTextView: TextView = listItemView!!.findViewById(R.id.textViewTitle)
        titleTextView.text = currentNote.title

        val textTextView: TextView = listItemView.findViewById(R.id.textViewText)
        val textLines = currentNote.text.split("\n").size
        textTextView.text = if (textLines > 3) {
            val lines = currentNote.text.split("\n")
            val truncatedText = lines.subList(0, 3).joinToString("\n")
            "$truncatedText..."
        } else {
            currentNote.text
        }

        return listItemView
    }
}





