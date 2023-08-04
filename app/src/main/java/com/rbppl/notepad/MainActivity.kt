package com.rbppl.notepad
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var noteList: MutableList<Note>
    private lateinit var databaseHelper: NoteDatabaseHelper
    private var isFragmentOpened = false // Флаг для отслеживания открытия фрагмента

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        databaseHelper = NoteDatabaseHelper(this)
        noteList = mutableListOf()

        val adapter = NoteListAdapter(this, noteList)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Проверяем, открыт ли фрагмент, и предотвращаем нажатие
            if (!isFragmentOpened) {
                val selectedNote = noteList[position]
                openNoteFragment(selectedNote)
            }
        }

        listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            val selectedNote = noteList[position]
            showDeleteConfirmationDialog(selectedNote)
            true
        }

        val addButton: View = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            openNoteFragment(null)
        }

        loadNotesFromDatabase()
    }

    private fun loadNotesFromDatabase() {
        noteList.clear()
        noteList.addAll(databaseHelper.getAllNotes())
        (listView.adapter as NoteListAdapter).notifyDataSetChanged()
    }

    private fun openNoteFragment(note: Note?) {
        isFragmentOpened = true // Устанавливаем флаг, что фрагмент открыт
        val fragment = NoteFragment.newInstance(note)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun addOrUpdateNoteInDatabase(note: Note) {
        if (note.id == 0L) {
            databaseHelper.addNote(note)
        } else {
            databaseHelper.updateNote(note)
        }
        loadNotesFromDatabase()
    }

    fun deleteNoteFromDatabase(note: Note) {
        databaseHelper.deleteNote(note)
        loadNotesFromDatabase()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isFragmentOpened = false // Устанавливаем флаг, что фрагмент закрыт
    }

    private fun showDeleteConfirmationDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Удаление")
            .setMessage("Вы уверены, что хотите удалить эту запись?")
            .setPositiveButton("Удалить") { _, _ ->
                deleteNoteFromDatabase(note)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
