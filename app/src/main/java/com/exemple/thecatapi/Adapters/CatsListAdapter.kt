package com.exemple.thecatapi.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exemple.thecatapi.Api.Model.Cat
import com.exemple.thecatapi.R

class CatsListAdapter: RecyclerView.Adapter<CatsListAdapter.ViewHolder>() {

    var list: MutableList<Cat> = mutableListOf()

    override fun getItemId(position: Int): Long {
        return list[position].id!!.toLong()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(data: Cat) {
            val imageUrl = data.url
            Glide.with(image.context).load(imageUrl).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cat_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }
}