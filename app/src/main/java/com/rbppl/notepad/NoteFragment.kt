package com.rbppl.notepad
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.ImageButton


class NoteFragment : Fragment() {

    private lateinit var titleEditText: EditText
    private lateinit var textEditText: EditText
    private var currentNote: Note? = null

    companion object {
        fun newInstance(note: Note?): NoteFragment {
            val fragment = NoteFragment()
            fragment.currentNote = note
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)

        titleEditText = view.findViewById(R.id.titleEditText)
        textEditText = view.findViewById(R.id.textEditText)

        val backButton: ImageButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        val checkButton: ImageButton = view.findViewById(R.id.checkButton)
        checkButton.setOnClickListener {
            saveNote()
            activity?.onBackPressed()
        }

        currentNote?.let {
            titleEditText.setText(it.title)
            textEditText.setText(it.text)
        }

        return view
    }

    private fun saveNote() {
        val title = titleEditText.text.toString()
        val text = textEditText.text.toString()
        if (currentNote == null) {
            val newNote = Note(title = title, text = text)
            (activity as MainActivity).addOrUpdateNoteInDatabase(newNote)
        } else {
            currentNote?.title = title
            currentNote?.text = text
            (activity as MainActivity).addOrUpdateNoteInDatabase(currentNote!!)
        }
    }
}
