package uz.gita.dictionarymvvm.ui.screens

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import uz.gita.dictionarymvvm.R
import uz.gita.dictionarymvvm.data.room.WordData
import uz.gita.dictionarymvvm.presenter.impl.FavouriteViewModelImpl
import uz.gita.dictionarymvvm.ui.adapter.FavAdapter
import java.util.*

class FavouriteActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var listWords: RecyclerView
    private lateinit var placeHolder: LinearLayout
    private lateinit var tts: TextToSpeech


    private lateinit var viewModel: FavouriteViewModelImpl
    private val adapter: FavAdapter by lazy { FavAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        listWords = findViewById(R.id.listFavourites)
        placeHolder = findViewById(R.id.emptyPlaceholder1)
        listWords.adapter = adapter

        supportActionBar?.title = "Favourite list"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#006262")))
        window.statusBarColor = Color.parseColor("#006262")

        viewModel = ViewModelProvider(this)[FavouriteViewModelImpl::class.java]


        viewModel.favourites.observe(this) {
            if (viewModel.favourites.value?.size == 0) placeHolder.visibility = View.VISIBLE
            adapter.submitList(it)
        }

        adapter.setFavouriteClickListener {
            viewModel.update(
                WordData(
                    it.id, it.english, it.type, it.transcript, it.uzbek, it.countable,
                    1
                )
            )
            adapter.submitList(viewModel.favourites.value!!)
        }

        tts = TextToSpeech(this, this)
        adapter.setVoiceClickListener {
            tts.speak(it.english, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        super.onBackPressed()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }
}