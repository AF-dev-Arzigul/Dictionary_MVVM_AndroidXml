package uz.gita.dictionarymvvm.presenter.impl

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.dictionarymvvm.data.room.WordData
import uz.gita.dictionarymvvm.presenter.WordViewModel
import uz.gita.dictionarymvvm.repository.WordRepository
import uz.gita.dictionarymvvm.repository.impl.WordRepositoryImpl

class WordViewModelImpl : WordViewModel, ViewModel() {
    private val wordRepository: WordRepository = WordRepositoryImpl()

    override val cursorLiveData = MutableLiveData<Cursor>()
    override val showWordInfoLiveData = MutableLiveData<WordData>()
    override val updateItemLiveData = MutableLiveData<Int>()

    private var query: String = ""

    override fun showInfo(wordData: WordData) {
        showWordInfoLiveData.value = wordData
    }

    override fun changeFavourite(wordData: WordData) {
        wordRepository.update(wordData)
        if (query.isEmpty()) {
            cursorLiveData.value = wordRepository.getWordsCursor()
        } else {
            cursorLiveData.value = wordRepository.getFilteredWordsCursor(query)
        }
    }

    override fun filter(query: String) {
        this.query = query
        cursorLiveData.value = wordRepository.getFilteredWordsCursor(query)
    }

    init {
        cursorLiveData.value = wordRepository.getWordsCursor()
    }
}