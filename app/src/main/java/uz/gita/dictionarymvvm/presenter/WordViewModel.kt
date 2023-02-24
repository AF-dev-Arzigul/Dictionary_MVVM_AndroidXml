package uz.gita.dictionarymvvm.presenter

import android.database.Cursor
import androidx.lifecycle.LiveData
import uz.gita.dictionarymvvm.data.room.WordData

interface WordViewModel {
    val cursorLiveData: LiveData<Cursor>
    val showWordInfoLiveData: LiveData<WordData>
    val updateItemLiveData: LiveData<Int>

    fun showInfo(wordData: WordData)

    fun changeFavourite(wordData: WordData)

    fun filter(query: String)


}