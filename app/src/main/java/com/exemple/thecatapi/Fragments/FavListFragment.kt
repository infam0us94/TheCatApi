package com.exemple.thecatapi.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exemple.thecatapi.Adapters.FavAdapter
import com.exemple.thecatapi.Api.Model.FavItem
import com.exemple.thecatapi.FavDB.FavDB
import com.exemple.thecatapi.R

class FavListFragment : Fragment() {

    private val favCatList: MutableList<FavItem> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    lateinit var favAdapter: FavAdapter
    lateinit var favDB: FavDB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fav_list_fragment, container, false)

        favDB = FavDB(context!!)
        recyclerView = root.findViewById(R.id.favRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val itemTouchHelper = simpleCallback
        itemTouchHelper.attachToRecyclerView(recyclerView)

        loadData()

        return root
    }

    private fun loadData() {
        if (favCatList != null) {
            favCatList.clear()
        }
        val db = favDB.readableDatabase
        val cursor = favDB.selectAllFavoriteList()
        try {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(FavDB.COL_ID))
                val image = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE))
                val favItem = FavItem(id, image)
                favCatList.add(favItem)
            }
        } finally {
            if (cursor != null && cursor.isClosed)
                cursor.close()
            db.close()
        }
        favAdapter = FavAdapter(activity!!, favCatList)
        recyclerView.adapter = favAdapter

//        favCatList.clear()
//        val db: SQLiteDatabase = favDB.readableDatabase
//        val cursor: Cursor = favDB.selectAllFavoriteList()
//        try {
//            while (cursor.moveToNext()) {
//                val id = cursor.getString(cursor.getColumnIndex(FavDB.COL_ID))
//                val image = cursor.getString(cursor.getColumnIndex(FavDB.ITEM_IMAGE))
//                val favItem = FavItem(id, image)
//                favCatList.add(favItem)
//            }
//        } finally {
//            if (cursor.isClosed) cursor.close()
//            db.close()
//        }
//        val favAdapter = FavAdapter(FragmentActivity(), favCatList)
//
//        favRecyclerView.adapter = favAdapter
    }

    private var simpleCallback =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favItem = favCatList[position]
                if (direction == ItemTouchHelper.LEFT) {
                    favAdapter.notifyItemRemoved(position)
                    favCatList.removeAt(position)
                    favDB.removeFav(favItem.key_id!!)
                }
            }

        })

}