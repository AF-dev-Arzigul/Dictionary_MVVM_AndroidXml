package uz.gita.dictionarymvvm.ui.screens

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import uz.gita.dictionarymvvm.R
import uz.gita.dictionarymvvm.presenter.WordViewModel
import uz.gita.dictionarymvvm.presenter.impl.WordViewModelImpl
import uz.gita.dictionarymvvm.ui.adapter.WordAdapter
import uz.gita.dictionarymvvm.ui.dialog.WordInfoDialog
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var listWords: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var emptyPlaceholder: View
    private lateinit var tts: TextToSpeech

    private val adapter: WordAdapter by lazy { WordAdapter() }
    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val viewModel: WordViewModel by viewModels<WordViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listWords = findViewById(R.id.listWords)
        searchView = findViewById(R.id.searchView)
        emptyPlaceholder = findViewById(R.id.emptyPlaceholder)

        supportActionBar?.title = "Dictionary"
        supportActionBar?.elevation = 0f
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#006262")))
        window.statusBarColor = Color.parseColor("#006262")

        listWords.layoutManager = LinearLayoutManager(this)
        adapter.setItemClickListener { viewModel.showInfo(it) }
        adapter.setFavouriteClickListener { data ->
            viewModel.changeFavourite(data)
        }
        listWords.adapter = adapter

        tts = TextToSpeech(this, this)
        adapter.setVoiceClickListener {
            tts.speak(it.english, TextToSpeech.QUEUE_FLUSH, null, "")
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                handler.postDelayed({
                    viewModel.filter(newText)
                }, 300)
                return true
            }
        })

        viewModel.showWordInfoLiveData.observe(this) {
            val dialog = WordInfoDialog()
            dialog.setFavouriteClickListener { i ->
                viewModel.changeFavourite(i)
            }
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "Info")
        }

        viewModel.cursorLiveData.observe(this) {
            if (it.count == 0) {
                emptyPlaceholder.visibility = View.VISIBLE
            } else {
                emptyPlaceholder.visibility = View.GONE
            }
            adapter.submitCursor(it)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.updateItemLiveData.observe(this) {
            adapter.notifyItemChanged(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bookmark -> {
                startActivity(Intent(this@MainActivity, FavouriteActivity::class.java))
                finish()
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }
}