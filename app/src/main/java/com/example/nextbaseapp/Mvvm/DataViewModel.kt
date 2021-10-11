package com.example.nextbaseapp.Mvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nextbaseapp.Constants.Constants.RESPONSE_TAG
import com.example.nextbaseapp.model.Journey_model
import com.example.nextbaseapp.repo.Repository
import com.example.nextbaseapp.util.coroutine
import kotlinx.coroutines.delay
import java.io.IOException

class DataViewModel (val repository: Repository): ViewModel() {

    var ResponseList = MutableLiveData<List<Journey_model>>()


    init {
        refreshNetworkCall()
    }

    fun refreshNetworkCall() {

        coroutine {

            var response = repository.getData()
            if (response.isSuccessful) {
                response.body()?.let {

                    ResponseList.postValue(response.body())

                }
            } else {
                retryIO {response = repository.getData() }
                //we can just handle the network error somehow here, like
                Log.e(RESPONSE_TAG, response.errorBody().toString())
            }
        }
    }

    suspend fun <T> retryIO(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 1000,    // 1 second
        factor: Double = 2.0,
        block: suspend () -> T): T
    {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: IOException) {
                // you can log an error here and/or make a more finer-grained
                // analysis of the cause to see if retry is needed
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }
}