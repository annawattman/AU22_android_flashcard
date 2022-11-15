package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    lateinit var wordView : TextView
    var currentWord : Word? = null
    val wordList = WordList()
    private lateinit var job: Job
    private lateinit var db : AppDatabase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        /*------------- ---------------- */
        db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java,
            "word-items").fallbackToDestructiveMigration().build()



        val word1 = Word(0, "Hello", "Hej")
        val word2 = Word(0, "Good bye", "Hej då")
        val word3 = Word(0, "Thank you", "Tack")
        val word4 = Word(0, "Welcome", "Välkommen")
        val word5 = Word(0, "Computer", "Dator")

     /*    saveWord(word1)
        saveWord(word2)
        saveWord(word3)
        saveWord(word4)
        saveWord(word5) */

        val list = loadAllWords()

        launch{
            val wordList = list.await()

            for (word in wordList) {
                Log.d("!!!", "word: $word")
            }
        }


        db = AppDatabase.getInstance(this)

        wordView = findViewById(R.id.wordTextView)

        showNewWord()

        wordView.setOnClickListener {
            revealTranslation()
        }


        /* ---------ADD WORDS BUTTON --------- */
        val addWordsBtn = findViewById<Button>(R.id.addWordsBtn) // Lägger till min knapp
        addWordsBtn.setOnClickListener {
            val intent = Intent(this, AddWordsActivity::class.java)
            startActivity(intent)
        }
    }

    fun saveWord(word : Word) {
        launch(Dispatchers.IO) {
            db.wordDao().insert(word)
        }
    }

    fun loadAllWords() : Deferred<List<Word>> =
        async(Dispatchers.IO) {
            db.wordDao().getAllWord()
        }


    fun revealTranslation() {
        wordView.text = currentWord?.english
    }


    fun showNewWord() {

        currentWord = wordList.getNewWord()
        wordView.text = currentWord?.swedish
    }

    fun delete(word : Word) =
        launch(Dispatchers.IO) {
            db.wordDao().delete(word)
        }




    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }

        return true
    }










}