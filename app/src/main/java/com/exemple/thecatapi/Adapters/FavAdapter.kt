package com.exemple.thecatapi.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.exemple.thecatapi.Api.Model.FavItem
import com.exemple.thecatapi.FavDB.FavDB
import com.exemple.thecatapi.R

class FavAdapter(private var context: Context, private var favItemList: MutableList<FavItem>) :
    RecyclerView.Adapter<FavAdapter.ViewHolder>() {
    lateinit var favDB: FavDB

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fav_item, parent, false)
        favDB = FavDB(context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.favImage.setImageResource(favItemList[position].item_image!!.toInt())
    }

    override fun getItemCount(): Int {
        return favItemList.size
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var favBtn: Button = itemView.findViewById(R.id.favBtnData)
        var favImage: ImageView = itemView.findViewById(R.id.favImageData)

        init {
            favBtn.setOnClickListener {
                val position = adapterPosition
                val favItem: FavItem = favItemList[position]
                favDB.removeFav(favItem.key_id!!)
                removeItem(position)
            }
        }
    }

    private fun removeItem(position: Int) {
        favItemList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, favItemList.size)
    }
}