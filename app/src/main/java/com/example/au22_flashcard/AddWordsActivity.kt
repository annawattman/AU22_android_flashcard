package com.example.au22_flashcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import kotlin.coroutines.CoroutineContext

class AddWordsActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var db: AppDatabase


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    lateinit var swedish: EditText
    lateinit var english: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_words)
        job = Job()

        /*------------- ---------------- */
        db = Room.databaseBuilder(applicationContext,
            AppDatabase::class.java,
            "word_items")
            .fallbackToDestructiveMigration()
            .build()

        swedish = findViewById<EditText>(R.id.newSwedishWord)
        english = findViewById<EditText>(R.id.newEnglishWord)

        // onClick {
        // val swedish = view1.text.toString()
        // val englis =
        //   val word = Word( 0, swedish, english)
        // saveWord(word)
        //
        // }
        val saveWordBtn = findViewById<Button>(R.id.addWordBtn)
        saveWordBtn.setOnClickListener {
            val swedishText = swedish.text.toString()
            val englishText = english.text.toString()
            var word = Word(0, englishText, swedishText)
            saveWord(word)

            val intent = Intent( this, MainActivity::class.java)
            startActivity(intent)
        }

        val mainActivityBtn = findViewById<Button>(R.id.mainActivityBtn) // Lägger till min knapp
        mainActivityBtn.setOnClickListener {
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)
            finish()
        }
    }


    fun saveWord(word: Word) {
        launch(Dispatchers.IO) {

            db.wordDao().insert(word)

        }
    }
}
