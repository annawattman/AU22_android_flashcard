package com.example.au22_flashcard

class WordList() {

    //Skapar listorna
    val wordList = mutableListOf<Word>()
    private val usedWords = mutableListOf<Word>()

    init
    {
        initializeWords()
    }
    //Lägger till några ord för att se till att det fungerar även om man inte har några ord
    fun initializeWords() {
        wordList.add(Word(1,"Monkey", "Apa"))
        wordList.add(Word(2,"Rabbit", "Kanin"))
        wordList.add(Word(3,"Table", "Bord"))
        wordList.add(Word(4,"Welcome", "Välkommen"))
        wordList.add(Word(6, "Bird", "Fågel"))
    }
    /* Funktioner */
    fun clearList() {
        wordList.clear()
    }
    fun addWord(word : Word) {
        wordList.add(word)
    }
    fun getNewWord() : Word {
        if (wordList.size == usedWords.size) {
            usedWords.clear()
        }
        var word : Word? = null
        do {
            val rnd = (0 until wordList.size).random()
            word = wordList[rnd]
        } while (usedWords.contains(word))

        usedWords.add(word!!)

        return word
    }
}


    // 1. en till lista med redan använda ord
    // 2. lista med index på använda ord
    // 3. använt ord tas bort från listan
    // 4. ordet håller reda på om det redan är använt







