package com.example.au22_flashcard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    lateinit var wordView : TextView
    lateinit var flagImg : ImageView
    var currentWord : Word? = null
    val wordList = WordList()

    private lateinit var job : Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    lateinit var db : AppDatabase

    lateinit var revealButton : Button
    lateinit var nextButton : Button
    lateinit var addNewWord : Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        job = Job()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "word-database")
            .fallbackToDestructiveMigration()
            .build()

        db = AppDatabase.getInstance(this)

        wordView = findViewById(R.id.wordTextView)
       // flagImg = findViewById(R.id.flagImg)

        showNewWord()

        wordView.setOnClickListener {
            revealTranslation()
        }

       /* revealButton = findViewById(R.id.revealButton)
        revealButton.setOnClickListener {
            revealTranslation()
            //flagImg.setImageResource(R.drawable.english_flag)
        } */

        nextButton = findViewById(R.id.nextButton)
        nextButton.setOnClickListener {
            showNewWord()
           // flagImg.setImageResource(R.drawable.swedish_flag)
        }
        addNewWord = findViewById(R.id.addNewWord)
        addNewWord.setOnClickListener {
            val intent = Intent(this, AddNewWord::class.java)
            startActivity(intent)
        }
    } //uppdatera listan
    override fun onResume() {
        super.onResume()
        wordList.clearList()
        wordList.initializeWords() //från WordList.kt
        launch {
            val addedWords = loadAllItems() //från databasen
            val list = addedWords.await()
            addNewWord(list)
        }
    }
    fun revealTranslation() {
        if (wordView.text == currentWord?.english) {
            wordView.text = currentWord?.swedish
        } else {
            wordView.text = currentWord?.english
        }
    }
    fun showNewWord() {
        currentWord = wordList.getNewWord()
        wordView.text = currentWord?.swedish
    }
    fun loadAllItems() : Deferred<MutableList<Word>> =
        async(Dispatchers.IO) {
            db.wordDao().getAll()
        }
    fun addNewWord(list : MutableList<Word>) {
        for (word in list) {
            wordList.addWord(word)
        }
    } //har även en knapp som gör samma sak
    override fun onTouchEvent(event : MotionEvent?) : Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }
        return true
    }
}