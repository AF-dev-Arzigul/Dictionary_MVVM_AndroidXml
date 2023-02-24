package uz.gita.dictionarymvvm.repository.impl

import android.database.Cursor
import androidx.lifecycle.LiveData
import uz.gita.dictionarymvvm.data.room.AppDatabase
import uz.gita.dictionarymvvm.data.room.WordData
import uz.gita.dictionarymvvm.repository.WordRepository

class WordRepositoryImpl : WordRepository {
    private val wordDao = AppDatabase.instance.getWordDao()

    override fun getWordsCursor(): Cursor = wordDao.getEngUzCursor()

    override fun getFilteredWordsCursor(query: String): Cursor = wordDao.getFilteredCursor(query)

    override fun update(wordData: WordData) {
        if (wordData.isFavourite == 0) {
            wordData.isFavourite = 1
        } else {
            wordData.isFavourite = 0
        }
        wordDao.update(wordData)
    }

    override fun getFavourites(): LiveData<List<WordData>> {
        return wordDao.getFavourite()
    }
}