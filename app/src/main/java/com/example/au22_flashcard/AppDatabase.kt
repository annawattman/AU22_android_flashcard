package com.example.au22_flashcard

import androidx.room.Database
import androidx.room.RoomDatabase

@Database
abstract class AppDatabase : RoomDatabase() {
    abstract val wordDao : WordDao

}