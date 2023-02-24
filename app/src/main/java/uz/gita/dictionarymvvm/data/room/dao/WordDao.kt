package uz.gita.dictionarymvvm.data.room.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import uz.gita.dictionarymvvm.data.room.WordData

@Dao
interface WordDao {

    @Insert
    fun insert(wordData: WordData)

    @Delete
    fun delete(wordData: WordData)

    @Update
    fun update(wordData: WordData)

    @Query("SELECT * FROM dictionary")
    fun getEngUzCursor(): Cursor

    @Query("SELECT * FROM dictionary WHERE is_favourite=1")
    fun getFavourite(): LiveData<List<WordData>>

    @Query("SELECT * FROM dictionary ORDER BY uzbek")
    fun getUzEngCursor(): Cursor

    @Query("SELECT * FROM dictionary WHERE english LIKE '%' || :query || '%'")
    fun getFilteredCursor(query: String): Cursor

}