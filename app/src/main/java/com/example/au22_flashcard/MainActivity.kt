package com.example.au22_flashcard

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    lateinit var wordView: TextView
    var currentWord: Word? = null
    var wordList = mutableListOf<Word>()
    private lateinit var job: Job
    private lateinit var db: AppDatabase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        /*------------- ---------------- */
        db = AppDatabase.getInstance(this)

        wordView = findViewById(R.id.wordTextView)

        wordView.setOnClickListener {
            revealTranslation()
        }

        val list = loadAllWords()

        launch {
            wordList = list.await() as MutableList<Word>
            showNewWord()
        }

        /* ---------ADD WORDS BUTTON --------- */
        val addWordsBtn = findViewById<Button>(R.id.addWordsBtn) // Lägger till min knapp
        addWordsBtn.setOnClickListener {
            val intent = Intent(this, AddWordsActivity::class.java)
            startActivity(intent)
        }
    }


    fun getNewWord(): Word {
        val usedWords = mutableListOf<Word>()
        if (wordList.size == usedWords.size) {
            usedWords.clear()
        }

        var word: Word? = null

        do {
            val rnd = (0 until wordList.size).random()
            word = wordList[rnd]
        } while (usedWords.contains(word))

        usedWords.add(word!!)

        return word
    }


    fun loadAllWords(): Deferred<List<Word>> =
        async(Dispatchers.IO) {
            db.wordDao().getAllWord()
        }


    fun revealTranslation() {
        wordView.text = currentWord?.english
    }

    fun showNewWord() {
        currentWord = getNewWord()
        wordView.text = currentWord?.swedish
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            showNewWord()
        }

        return true
    }

}
/*
    fun showNewWord()  {
        currentWord = wordList[4]
       // wordView.text = currentWord?.english
        // val rnd = (0 until wordList.size).random()
      //  currentWord = wordList[rnd] // Random word
        // currentWord = wordList.getNewWord()
        wordView.setText(currentWord?.swedish)
      /*= currentWord?.swedish */
        // return wordList[rnd]
/*
        for (x in 0 until wordList.size) {
            wordView.setText(result.get(x))
        } */

    } */

/*
    fun delete(word : Word) =
        launch(Dispatchers.IO) {
            db.wordDao().delete(word)
        }
*/
