package com.example.au22_flashcard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
// Skapar variabler
    lateinit var wordTextView : TextView
    var currentWord : Word? = null
    val wordList = WordList()

    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var db : AppDatabase

    // Lägger till mina knappar
    lateinit var nextWordBtn : Button
    lateinit var addNewWordBtn : Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
// Kopplar databasen till variabeln db
        job = Job()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "word-database")
            .fallbackToDestructiveMigration()
            .build()

        db = AppDatabase.getInstance(this)

        wordTextView = findViewById(R.id.wordTextView)

        showNewWord()

        wordTextView.setOnClickListener {
            revealTranslation()
        }


        nextWordBtn = findViewById(R.id.nextWordBtn)
        nextWordBtn.setOnClickListener {
            showNewWord()
        }

        addNewWordBtn = findViewById<Button>(R.id.addNewWordBtn)
        addNewWordBtn.setOnClickListener {
            val intent = Intent(this, AddNewWord::class.java)
            startActivity(intent)
        }
    }
    //Uppdaterar listan
    override fun onResume() {
        super.onResume()
        wordList.clearList()
        wordList.initializeWords() // från WordList.kt
        launch {
            val addedWords = loadAllItems() // från room-databasen
            val list = addedWords.await()
            addNewWord(list)
        }
    }
    /* Funktioner */
    fun revealTranslation() {
        if (wordTextView.text == currentWord?.english) {
            wordTextView.text = currentWord?.swedish
        } else {
            wordTextView.text = currentWord?.english
        }
    }
    fun showNewWord() {
        currentWord = wordList.getNewWord()
        wordTextView.text = currentWord?.swedish
    }
    fun loadAllItems() : Deferred<MutableList<Word>> =
        async(Dispatchers.IO) {
            db.wordDao().getAll()
        }
    fun addNewWord(list : MutableList<Word>) {
        for (word in list) {
            wordList.addWord(word)
        }
    } // Knapp som också byter ord
    override fun onTouchEvent(event : MotionEvent?) : Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }
        return true
    }
}