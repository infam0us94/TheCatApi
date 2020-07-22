package com.exemple.thecatapi.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.exemple.thecatapi.Adapters.CatsListAdapter
import com.exemple.thecatapi.Api.Model.Cat
import com.exemple.thecatapi.R
import kotlinx.android.synthetic.main.cat_list_fragment.*

class CatsListFragment : Fragment() {

    private lateinit var viewModel: CatsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cat_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CatsListViewModel::class.java)
        viewModel.getCatList()
        viewModel.catListLiveData.observe(viewLifecycleOwner, catsListObserver)
    }

    private fun showData(list: List<Cat>?) {
        if (list != null) {
            val adapter = CatsListAdapter(context!!,list)
            adapter.list = list as MutableList<Cat>
            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private val catsListObserver = Observer<List<Cat>> { result ->
        showData(result)
    }
}