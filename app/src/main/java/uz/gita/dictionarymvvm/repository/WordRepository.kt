package uz.gita.dictionarymvvm.repository

import android.database.Cursor
import androidx.lifecycle.LiveData
import uz.gita.dictionarymvvm.data.room.WordData

interface WordRepository {

    fun getWordsCursor(): Cursor

    fun getFilteredWordsCursor(query:String): Cursor

    fun update(wordData: WordData)

    fun getFavourites(): LiveData<List<WordData>>
}