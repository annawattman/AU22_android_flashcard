package com.example.au22_flashcard

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.internal.AddLastDesc

@Dao
interface WordDao {

    //här lägger man in eller skapar olika funktioner. typ "hitta alla word som börjar med H", hitta alla "djur"


    @Insert
    fun insert(word : Word)

    @Delete
    fun delete(word : Word)

    @Query("SELECT * FROM word_table")
    fun getAll() : MutableList<Word>



/*
    @Query("SELECT * FROM item_table WHERE category LIKE :categoryName")
    fun findByCategory(categoryName : String) : List<Word>
 */
}