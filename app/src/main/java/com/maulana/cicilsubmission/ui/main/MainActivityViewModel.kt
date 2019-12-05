package com.maulana.cicilsubmission.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maulana.cicilsubmission.BuildConfig
import com.maulana.cicilsubmission.model.MovieModel
import com.maulana.cicilsubmission.model.ResponseMovieModel
import com.maulana.cicilsubmission.network.RetrofitClient
import com.maulana.cicilsubmission.network.RetrofitInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class MainActivityViewModel : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    private val _errorMessage = MutableLiveData<String>()
    private val _showProgress = MutableLiveData<Boolean>()
    val _listData = MutableLiveData<ArrayList<MovieModel>>()
    val _title = MutableLiveData<String>()
    val _page = MutableLiveData<Int>()
    val _isLoading = MutableLiveData<Boolean>()
    val _showRv = MutableLiveData<Boolean>()

    var listData = ArrayList<MovieModel>()
    var isLoading: Boolean = false
    var canLoadMore: Boolean = true
    var page: Int = 1
    var title: String = ""

    fun getData(query: HashMap<String, String>) {
        page = 1
        title = query["s"].toString()
//        _showProgress.postValue(true)
        val apiService = RetrofitClient.getClient().create(RetrofitInterface::class.java)
        query["apikey"] = BuildConfig.API_KEY
        query["page"] = page.toString()
//        viewModelScope.launch {
            apiService.getListMovie(query).enqueue(object : Callback<ResponseMovieModel> {
                override fun onFailure(call: Call<ResponseMovieModel>, t: Throwable) {
                    _showProgress.postValue(false)
                    _errorMessage.postValue(t.message)
                }

                override fun onResponse(
                    call: Call<ResponseMovieModel>,
                    response: Response<ResponseMovieModel>
                ) {
//                    _showProgress.postValue(false)
                    if (response.isSuccessful) {
                        try{
                            _showRv.postValue(true)
                            val data = response.body()
                                if (data?.search?.size!! < 10) {
                                    canLoadMore = false
                                }
                                page++
                                listData.clear()
                                listData.addAll(data.search)
                                _listData.postValue(listData)

                        }catch (e: Exception) {
                            _showRv.postValue(false)
//                            _errorMessage.postValue("Film Tidak Ada")
                        }
                    } else {
                        val error = response.errorBody()
                        _errorMessage.postValue(error.toString())
                    }
                }
            })
//        }
    }

    fun loadMoreData() {
        if (page > 4) {
            _errorMessage.postValue("Anda Telah Mencapai Halaman ke 4")
            return
        }
        if (canLoadMore) {
            if (!isLoading) {
                isLoading = true
                _isLoading.postValue(true)
                val query = HashMap<String, String>()
                query["apikey"] = BuildConfig.API_KEY
                query["page"] = page.toString()
                query["s"] = title
                val apiService = RetrofitClient.getClient().create(RetrofitInterface::class.java)
                viewModelScope.launch {
                    apiService.getListMovie(query).enqueue(object : Callback<ResponseMovieModel> {
                        override fun onFailure(call: Call<ResponseMovieModel>, t: Throwable) {
                            isLoading = false
                            _isLoading.postValue(false)
                            _errorMessage.postValue(t.message)
                        }

                        override fun onResponse(
                            call: Call<ResponseMovieModel>,
                            response: Response<ResponseMovieModel>
                        ) {
                            isLoading = false
                            _isLoading.postValue(false)
                            if (response.isSuccessful) {
                                try{
                                    page++
                                    val data = response.body()
                                    if(data?.search?.size!! < 10) {
                                        canLoadMore = false
                                    }
                                    listData.addAll(data.search as ArrayList<MovieModel>)
                                    _listData.postValue(listData)
                                }catch (e: Exception) {
                                    _errorMessage.postValue(e.message)
                                }
                            } else {
                                val error = response.errorBody()
                                _errorMessage.postValue(error.toString())
                            }
                        }
                    })
                }
            }

        }
    }

    fun observableData(): LiveData<ArrayList<MovieModel>> {
        return _listData
    }

    fun observableShowProgress(): LiveData<Boolean> {
        return _showProgress
    }

    fun observableIsLoading(): LiveData<Boolean> {
        return _isLoading
    }

    fun observableErroMessage(): LiveData<String> {
        return _errorMessage
    }
    fun observableTitle(): LiveData<String> {
        return _title
    }
    fun observableShowRv(): LiveData<Boolean> {
        return _showRv
    }

    init {
        val data = HashMap<String, String>()
        data["s"] = "toy"
        getData(data)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun postTitle(toString: String) {
        title = toString
        _title.postValue(toString)
    }


}