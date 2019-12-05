package com.maulana.cicilsubmission.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maulana.cicilsubmission.R
import com.maulana.cicilsubmission.adapter.MovieAdapter
import com.maulana.cicilsubmission.model.MovieModel
import com.maulana.cicilsubmission.ui.detail.DetailMovieActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: MovieAdapter
    lateinit var movieActivityViewModel: MainActivityViewModel

    var movies = arrayListOf<MovieModel?>()
    var treshold: Int = 2
    lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.app_name)

        linearLayoutManager = LinearLayoutManager(this)

        movieActivityViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel::class.java)
        showRecyclerview()
        observableLifeData()
    }

    private fun observableLifeData() {
        movieActivityViewModel.observableData().observe(this, Observer {
            movies.clear()
            movies.addAll(it)
            adapter.addData(movies)
        })
        movieActivityViewModel.observableShowProgress().observe(this, Observer {
            if(it) {
                movie_fragment_progressbar.visibility = View.VISIBLE
            } else {
                movie_fragment_progressbar.visibility = View.GONE
            }
        })
        movieActivityViewModel.observableIsLoading().observe(this, Observer {
            if(it) {
                adapter.addNullData()
            } else {
                adapter.removeNullData()
            }
        })
        movieActivityViewModel.observableErroMessage().observe(this, Observer {
           Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        movieActivityViewModel.observableTitle().observe(this, Observer {
            val param = HashMap<String, String>()
            param["s"] = it
            movieActivityViewModel.getData(param)
        })
        movieActivityViewModel.observableShowRv().observe(this, Observer {
            if(it) {
                if(rv_movie.visibility == View.GONE) {
                    rv_movie.visibility = View.VISIBLE
                    indicatior_empty_movie.visibility = View.GONE
                }
            } else {
                if(rv_movie.visibility == View.VISIBLE) {
                    rv_movie.visibility = View.GONE
                    indicatior_empty_movie.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showRecyclerview() {
        rv_movie.layoutManager = linearLayoutManager
        adapter = MovieAdapter()
        rv_movie.adapter = adapter
        rv_movie.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy >0) {
                    val counItem = linearLayoutManager.itemCount
                    val lastItemVisible = linearLayoutManager.findLastVisibleItemPosition()
                    if(lastItemVisible + treshold >= counItem)
                        movieActivityViewModel.loadMoreData()
                }
            }
        })
        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback{
            override fun onItemClicked(data: MovieModel?) {
                val intent = Intent(this@MainActivity,  DetailMovieActivity::class.java)
                val bundle = Bundle()
                bundle.putString(DetailMovieActivity.DETAIL_IMDB, data?.imdbId)
                bundle.putString(DetailMovieActivity.DETAIL_NAME, data?.title)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.searchMenu)
        val searchView: SearchView = searchItem?.actionView as SearchView

        searchQuery(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    private fun searchQuery(searchView: SearchView) {
        val param = HashMap<String, String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.isNullOrEmpty() || query.length < 3) return false

                param["s"] = query.toString()
                movieActivityViewModel.postTitle(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }
}