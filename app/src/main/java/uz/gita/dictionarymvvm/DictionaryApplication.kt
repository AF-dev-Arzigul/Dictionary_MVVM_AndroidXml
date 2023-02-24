package uz.gita.dictionarymvvm

import android.app.Application
import timber.log.Timber
import uz.gita.dictionarymvvm.data.room.AppDatabase

class DictionaryApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppDatabase.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

    }
}