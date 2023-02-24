package uz.gita.dictionarymvvm.ui.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import uz.gita.dictionarymvvm.R
import uz.gita.dictionarymvvm.data.room.WordData

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordItemViewHolder>() {

    private var cursor: Cursor? = null
    private var itemClickListener: ((WordData) -> Unit)? = null
    private var favouriteClickListener: ((WordData) -> Unit)? = null
    private var voiceClickListener: ((WordData) -> Unit)? = null

    fun setItemClickListener(block: (WordData) -> Unit) {
        itemClickListener = block
    }

    fun setFavouriteClickListener(block: (WordData) -> Unit) {
        favouriteClickListener = block
    }

    fun setVoiceClickListener(block: (WordData) -> Unit) {
        voiceClickListener = block
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitCursor(cursor: Cursor) {
        Timber.d("Submitted Cursor")
        this.cursor = cursor
        Timber.d("Count ${cursor.count}")
        notifyDataSetChanged()
    }

    inner class WordItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtWord: TextView = view.findViewById(R.id.txtWord)
        private val txtType: TextView = view.findViewById(R.id.txtType)
        private val txtRead: TextView = view.findViewById(R.id.txtRead)
        private val btnVoice: ImageButton = view.findViewById(R.id.btnVoice)
        private val btnStar: ImageButton = view.findViewById(R.id.btnStar)

        fun bind() {
            cursor!!.moveToPosition(adapterPosition)
            val data = cursor!!.getWordData()

            txtWord.text = data.english
            txtType.text = data.type
            txtRead.text = data.transcript

            if (data.isFavourite == 0) {
                btnStar.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
            } else {
                btnStar.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }

        }

        init {
            view.setOnClickListener {
                Timber.d("ITEM CLICKED $adapterPosition")
                cursor!!.moveToPosition(adapterPosition)
                val data = cursor!!.getWordData()
                itemClickListener?.invoke(data)
            }
            btnStar.setOnClickListener {
                cursor!!.moveToPosition(adapterPosition)
                val data = cursor!!.getWordData()
                favouriteClickListener?.invoke(data)
            }

            btnVoice.setOnClickListener {
                cursor!!.moveToPosition(adapterPosition)
                val data = cursor!!.getWordData()
                voiceClickListener?.invoke(data)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
    )

    override fun onBindViewHolder(holder: WordItemViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int {
        val count = cursor?.count ?: 0
        Timber.d(count.toString())
        return count
    }
}

fun Cursor.getWordData(): WordData {
    return WordData(
        getLong(0),
        getString(1),
        getString(2),
        getString(3),
        getString(4),
        getString(5),
        getInt(6)
    )
}

//private val WordCallBack = object : DiffUtil.ItemCallback<WordData>() {
//    override fun areItemsTheSame(oldItem: WordData, newItem: WordData): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: WordData, newItem: WordData): Boolean {
//        return oldItem.id == newItem.id &&
//                oldItem.countable == newItem.countable &&
//                oldItem.english == newItem.english &&
//                oldItem.transcript == newItem.transcript &&
//                oldItem.uzbek == newItem.uzbek &&
//                oldItem.isFavourite == newItem.isFavourite &&
//                oldItem.type == newItem.type
//    }
//
//}