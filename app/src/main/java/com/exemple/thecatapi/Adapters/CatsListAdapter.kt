package com.exemple.thecatapi.Adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exemple.thecatapi.Api.Model.Cat
import com.exemple.thecatapi.FavDB.FavDB
import com.exemple.thecatapi.R

class CatsListAdapter(private val context: Context) :
    RecyclerView.Adapter<CatsListAdapter.ViewHolder>() {

    var list: MutableList<Cat> = mutableListOf()
    lateinit var favDB: FavDB

    override fun getItemId(position: Int): Long {
        return list[position].id!!.toLong()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.imageView)
        var favBtn: Button = itemView.findViewById(R.id.favBtn)


        fun bind(data: Cat) {
            val imageUrl = data.url
            Glide.with(image.context).load(imageUrl).into(image)

            favBtn.setOnClickListener {
                val position = adapterPosition
                val catItem: Cat = list[position]
                if (catItem.favStatus == "0") {
                    catItem.favStatus = "1"
                    favDB.insertIntoTheDatabase(
                        catItem.url!!,
                        catItem.id!!, catItem.favStatus
                    )
                    favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
                } else {
                    catItem.favStatus = "0"
                    favDB.remove_fav(catItem.id!!)
                    favBtn.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        favDB = FavDB(context)
        val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val firstStart = prefs.getBoolean("firstStart", true)
        if (firstStart) {
            createTableOnFirstStart()
        }
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cat_item, parent, false)
        return ViewHolder(itemView)
    }

    private fun createTableOnFirstStart() {
        favDB.insertEmpty()

        val prefs: SharedPreferences = context.getSharedPreferences(
            "prefs",
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean("firstStart", false)
        editor.apply()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        readCursorData(currentItem, holder)
        holder.bind(currentItem)
    }

    private fun readCursorData(
        catItem: Cat,
        viewHolder: ViewHolder
    ) {
        val cursor = catItem.id?.let { favDB.read_all_data(it) }
        val db = favDB.readableDatabase
        try {
            while (cursor!!.moveToNext()) {
                val item_fav_status =
                    cursor.getString(cursor.getColumnIndex(favDB.FAVORITE_STATUS))
                catItem.favStatus = item_fav_status
                if (item_fav_status != null && item_fav_status == "1") {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
                } else if (item_fav_status != null && item_fav_status == "0") {
                    viewHolder.favBtn
                        .setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed) cursor.close()
            db.close()
        }
    }
}