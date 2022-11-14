package com.example.au22_flashcard

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val wordDao : WordDao

    // Spara en instans av objektet som skapas upp utifrån denna klass
    companion object { //Gemensam för alla objekt som skapas upp inifrån klassen
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase { // Ge oss vår databas, returnera databas
            synchronized(this) { /*Ser till att den är låst, den går inte att köra samtidigt på flera trådar*/
                var instance = INSTANCE
                //Finns det en appDatabase redan? Finns det en instance? Första gången instance körs, behöver vi skapa databasen
                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "word_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
        }
    }

