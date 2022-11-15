package com.example.au22_flashcard

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface WordDao {

    @Insert // Lägger till ett word
    fun insert(word: Word)

    //Delete
    @Delete //Raderar ett ord
    fun delete(word: Word)

    //getAllWords
    @Query("SELECT * FROM word_table") // Hämtar alla orden
    fun getAllWord(): List<Word>

    @Insert
    fun insertAll(word: Word)


    @Query("DELETE FROM word_table")
    fun deleteAll()

}