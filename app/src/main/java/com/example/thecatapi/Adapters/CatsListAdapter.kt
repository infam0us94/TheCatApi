package com.example.thecatapi.Adapters

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.thecatapi.Api.Model.Cat
import com.example.thecatapi.FavDB.FavDB
import com.example.thecatapi.R
import java.io.OutputStream

class CatsListAdapter(private val context: Context, var list: List<Cat>) :
    RecyclerView.Adapter<CatsListAdapter.ViewHolder>() {

//    var list: MutableList<Cat> = mutableListOf()

    lateinit var favDB: FavDB

    lateinit var outputStream: OutputStream
    lateinit var drawable: BitmapDrawable
    lateinit var bitmap: Bitmap

    override fun getItemId(position: Int): Long {
        return list[position].id!!.toLong()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var image: ImageView = itemView.findViewById(R.id.imageView)
        var favBtn: Button = itemView.findViewById(R.id.favBtn)
        var btnDownload: Button = itemView.findViewById(R.id.downloadBtn)


        fun bind(data: Cat) {
            val imageUrl = data.url
            Glide.with(image.context).load(imageUrl).into(image)

            favBtn.setOnClickListener {
                val position = adapterPosition
                val catItem = list[position]
                if (catItem.favStatus.equals("0")) {
                    catItem.favStatus = "1"
                    favDB.insertIntoTheDatabase(catItem.url!!, catItem.id!!, catItem.favStatus!!)
                    favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
                } else {
                    catItem.favStatus = "0"
                    favDB.removeFav(catItem.id!!)
                    favBtn.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
                }
            }

//            btnDownload.setOnClickListener {
//                drawable = image.drawable as BitmapDrawable
//                bitmap = drawable.bitmap
//                val filepath = Environment.getExternalStorageDirectory()
//                val dir = File(filepath.absolutePath + "/Downloads")
//                dir.mkdir()
//                val file =
//                    File(dir, System.currentTimeMillis().toString() + ".jpg")
//                try {
//                    outputStream = FileOutputStream(file)
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//                    Toast.makeText(context, "Image Save", Toast.LENGTH_SHORT).show()
//                    outputStream.flush()
//                    outputStream.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
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
        val cursor = favDB.readAllData(catItem.id!!)
        val db = favDB.readableDatabase
        try {
            while (cursor.moveToNext()) {
                val itemFavStatus = cursor.getString(cursor.getColumnIndex(favDB.FAVORITE_STATUS))
                catItem.favStatus = itemFavStatus
                if (itemFavStatus != null && itemFavStatus.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_red_24dp)
                } else if (itemFavStatus != null && itemFavStatus.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_favorite_border_red_24dp)
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed)
                cursor.close()
            db.close()
        }
    }
}