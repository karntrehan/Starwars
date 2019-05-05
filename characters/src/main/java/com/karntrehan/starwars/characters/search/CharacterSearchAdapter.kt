package com.karntrehan.starwars.characters.search

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.karntrehan.starwars.characters.R
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import kotlinx.android.synthetic.main.item_character_search.view.*

class CharacterSearchAdapter(private val interaction: Interaction? = null) :
        ListAdapter<CharacterSearchModel, CharacterSearchAdapter.CharacterSearchVH>(CharacterSearchModelDC()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CharacterSearchVH(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_character_search, parent, false), interaction
    )

    override fun onBindViewHolder(holder: CharacterSearchVH, position: Int) = holder.bind(getItem(position))

    fun swapData(data: List<CharacterSearchModel>) {
        submitList(data.toMutableList())
    }

    inner class CharacterSearchVH(itemView: View, private val interaction: Interaction?)
        : RecyclerView.ViewHolder(itemView), OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION) return
            interaction?.characterClicked(getItem(adapterPosition))
        }

        fun bind(item: CharacterSearchModel) = with(itemView) {
            tvName.text = item.name
            tvDOB.text = item.birthYear
        }
    }

    interface Interaction {
        fun characterClicked(character: CharacterSearchModel)
    }

    private class CharacterSearchModelDC : DiffUtil.ItemCallback<CharacterSearchModel>() {
        override fun areItemsTheSame(oldItem: CharacterSearchModel,
                                     newItem: CharacterSearchModel) = oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: CharacterSearchModel,
                                        newItem: CharacterSearchModel) = oldItem == newItem
    }
}