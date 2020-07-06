package com.exemple.thecatapi.Fragments

import android.app.Activity
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exemple.thecatapi.Adapters.FavAdapter
import com.exemple.thecatapi.Api.Model.FavItem
import com.exemple.thecatapi.FavDB.FavDB
import com.exemple.thecatapi.R
import kotlinx.android.synthetic.main.fav_list_fragment.*

class FavListFragment: Fragment() {
    private val favCatList: MutableList<FavItem> = mutableListOf()

    lateinit var favDB: FavDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fav_list_fragment, container, false)

        favDB = FavDB(Activity())
        val recyclerView: RecyclerView = root.findViewById(R.id.favRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        loadData()

        return root
    }

    private fun loadData() {
        if (favCatList != null) {
            favCatList.clear()
        }
        val db: SQLiteDatabase = favDB.readableDatabase
        val cursor: Cursor = favDB.select_all_favorite_list()
        try {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(FavDB.KEY_ID))
                val image = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE))
                val favItem = FavItem(id, image)
                favCatList.add(favItem)
            }
        } finally {
            if (cursor != null && cursor.isClosed) cursor.close()
            db.close()
        }
        val favAdapter = FavAdapter(FragmentActivity(), favCatList)

        favRecyclerView.adapter = favAdapter
    }
}