package com.gmail.ivan.morozyk.moviesearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.databinding.ItemTitleBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeGone
import com.gmail.ivan.morozyk.moviesearch.extentions.makeInvisible

class TitleAdapter(private val onClickAction: (String) -> Unit) :
    RecyclerView.Adapter<TitleAdapter.TitleHolder>() {

    private val titleList = mutableListOf<Title>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TitleHolder(
        ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TitleHolder, position: Int) {
        holder.bind(titleList[position])
    }

    override fun getItemCount() = titleList.size

    fun showTitles(titleList: List<Title>) {
        this.titleList.clear()
        this.titleList.addAll(titleList)
        notifyDataSetChanged()
    }

    inner class TitleHolder(private val binding: ItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: Title) {
            with(binding) {
                root.setOnClickListener { onClickAction(title.id) }

                titleNameText.text = title.name

                if (!title.type.isNullOrEmpty()) {
                    titleType.text = title.type
                } else {
                    titleType.makeGone()
                }

                if (!title.year.isNullOrEmpty()) {
                    titleProductionYearText.text = title.year
                } else {
                    titleProductionYearText.makeInvisible()
                }

                if (title.rating.isNullOrEmpty()) {
                    titleRatingText.makeInvisible()
                    ratingIconImage.makeInvisible()
                } else {
                    titleRatingText.text = title.rating
                }

                Glide.with(itemView).load(title.image).centerCrop()
                    .placeholder(R.drawable.icon_title_placeholder).into(titlePoster)
            }
        }
    }
}