package uz.gita.dictionarymvvm.ui.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import uz.gita.dictionarymvvm.R
import uz.gita.dictionarymvvm.data.room.WordData

class FavAdapter : RecyclerView.Adapter<FavAdapter.VH>() {
    private val oldList: ArrayList<WordData> = ArrayList()
    private var itemClickListener: ((WordData) -> Unit)? = null
    private var favouriteClickListener: ((WordData) -> Unit)? = null
    private var voiceClickListener: ((WordData) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<WordData>) {
        oldList.clear()
        oldList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setFavouriteClickListener(block: (WordData) -> Unit) {
        favouriteClickListener = block
    }

    fun setVoiceClickListener(block: (WordData) -> Unit) {
        voiceClickListener = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val txtWord: TextView = view.findViewById(R.id.txtWord)
        private val ivStar: ImageView = view.findViewById(R.id.btnStar)
        private val txtType: TextView = view.findViewById(R.id.txtType)
        private val btnVoice: ImageButton = view.findViewById(R.id.btnVoice)
        private val txtRead: TextView = view.findViewById(R.id.txtRead)

        fun bind() {
            val item = oldList[adapterPosition]

            txtWord.text = item.english
            txtType.text = item.type
            txtRead.text = item.transcript

            if (item.isFavourite == 0) {
                ivStar.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
            } else {
                ivStar.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }
        }

        init {
            view.setOnClickListener {
                itemClickListener?.invoke(oldList[adapterPosition])
            }

            ivStar.setOnClickListener {
                favouriteClickListener?.invoke(oldList[adapterPosition])
            }

            btnVoice.setOnClickListener {
                val data = oldList[adapterPosition]
                voiceClickListener?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = oldList.size

}