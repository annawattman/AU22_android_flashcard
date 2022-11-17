package com.example.au22_flashcard

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AddNewWord : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var swedishWord: EditText
    lateinit var englishWord: EditText
    lateinit var addButton: Button
    lateinit var backButton: FloatingActionButton

    private lateinit var db: AppDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_word)
        supportActionBar?.hide()

        job = Job()

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "word-database")
            .fallbackToDestructiveMigration()
            .build()

        db = AppDatabase.getInstance(this)

        swedishWord = findViewById(R.id.swedishWordInput)
        englishWord = findViewById(R.id.englishWordInput)
        addButton = findViewById(R.id.addWordButton)
        addButton.setOnClickListener {
            addNewWord()
            finish()
        }
        var goBackBtn = findViewById<Button>(R.id.goBackBtn)
        goBackBtn.setOnClickListener {
            finish()
        }
    }
    fun addNewWord() {
        val sweWord = swedishWord.text.toString()
        val enWord = englishWord.text.toString()
        if (sweWord.isNotEmpty() && enWord.isNotEmpty()) {
            val word = Word(0, enWord, sweWord)
            launch(Dispatchers.IO) {
                db.wordDao().insert(word)
            }
        }
    }
}