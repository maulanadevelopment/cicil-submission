package com.maulana.cicilsubmission.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.maulana.cicilsubmission.R
import com.maulana.cicilsubmission.databinding.ActivityDetailMovieBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.activity_main.*

class DetailMovieActivity : AppCompatActivity() {

    lateinit var title: String
    lateinit var movieViewModel: DetailMovieViewModel
    lateinit var binding: ActivityDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_movie)
        sv_detail_movie.visibility = View.GONE

        movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailMovieViewModel::class.java)

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val bundle = intent.extras
        if(bundle != null) {
            title = bundle.getString(DETAIL_NAME).toString()
            val imdbId = bundle.getString(DETAIL_IMDB).toString()
            supportActionBar?.title = title
            movieViewModel.getData(imdbId)
        }
        observableLivedata()
    }

    private fun observableLivedata() {
        movieViewModel.observableShowProgress().observe(this, Observer {
            if(it) {
                movie_detail_progressbar.visibility = View.VISIBLE
            } else {
                movie_detail_progressbar.visibility = View.GONE
            }
        })

        movieViewModel.observableData().observe(this, Observer {
            binding.model = it
            sv_detail_movie.visibility = View.VISIBLE
            Picasso.with(this).load(it.poster).fit().into(detail_image)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DETAIL_NAME = "detail_movie"
        const val DETAIL_IMDB = "detail_imdb"
    }
}
