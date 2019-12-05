package com.maulana.cicilsubmission.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maulana.cicilsubmission.BuildConfig
import com.maulana.cicilsubmission.model.MovieDetailModel
import com.maulana.cicilsubmission.network.RetrofitClient
import com.maulana.cicilsubmission.network.RetrofitInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMovieViewModel: ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val _data = MutableLiveData<MovieDetailModel>()
    val _imdbId = MutableLiveData<String>()
    val _showProgress = MutableLiveData<Boolean>()
    val _errorMessage = MutableLiveData<String>()
    lateinit var imdbId : LiveData<String>

    fun getData(imdb: String) {
        val query = HashMap<String, String>()
        query["i"] = imdb
        query["apikey"] = BuildConfig.API_KEY
        _imdbId.postValue(imdb)
        imdbId = _imdbId
        val apiService = RetrofitClient.getClient().create(RetrofitInterface::class.java)
        viewModelScope.launch {
            apiService.getDetail(query).enqueue(object : Callback<MovieDetailModel> {
                override fun onFailure(call: Call<MovieDetailModel>, t: Throwable) {
                    _showProgress.postValue(false)
                    _errorMessage.postValue(t.message)                }

                override fun onResponse(
                    call: Call<MovieDetailModel>,
                    response: Response<MovieDetailModel>
                ) {
                    _showProgress.postValue(false)
                    if(response.isSuccessful) {
                        val data = response.body()
                        _data.postValue(data)
                    } else {
                        val error = response.errorBody()
                        _errorMessage.postValue(error.toString())
                    }
                }
            })
        }
    }

    fun observableShowProgress(): LiveData<Boolean> {
        return _showProgress
    }

    fun observableData() : LiveData<MovieDetailModel> {
        return _data
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}