package com.exemple.thecatapi.Fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exemple.thecatapi.Api.Model.Cat
import com.exemple.thecatapi.Api.RemoteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsListViewModel: ViewModel() {

    private val repository = RemoteRepository.getInstance()

    val catListLiveData: MutableLiveData<List<Cat>> = MutableLiveData()

    fun getCatList() {
        repository.getCatList()
            .enqueue(object : Callback<List<Cat>> {
                override fun onFailure(call: Call<List<Cat>>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<List<Cat>>,
                    response: Response<List<Cat>>
                ) {
                    catListLiveData.postValue(response.body())
                }

            })
    }
}