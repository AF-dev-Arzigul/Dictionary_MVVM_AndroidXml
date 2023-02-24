package uz.gita.dictionarymvvm.presenter.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import uz.gita.dictionarymvvm.data.room.WordData
import uz.gita.dictionarymvvm.repository.impl.WordRepositoryImpl

class FavouriteViewModelImpl : ViewModel() {
    private val favouriteRepository = WordRepositoryImpl()
    val favourites: LiveData<List<WordData>> = favouriteRepository.getFavourites()

    fun update(wordData: WordData) {
        favouriteRepository.update(wordData)
    }
}