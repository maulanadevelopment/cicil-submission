package com.maulana.cicilsubmission.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maulana.cicilsubmission.R
import com.maulana.cicilsubmission.model.MovieModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    var data = ArrayList<MovieModel?>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MovieModel?)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            (holder as ListViewHolder).bind(model)
        }
    }

    companion object {
        val VIEW_TYPE_ITEM: Int = 0
        val VIEW_TYPE_LOADING: Int = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when(viewType) {
            VIEW_TYPE_ITEM -> ListViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false))
            else -> LoadingHolder(layoutInflater.inflate(R.layout.item_loading, parent, false))
        }
    }

    open class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!)

    inner class LoadingHolder(itemView: View?) : ViewHolder(itemView!!)

    inner class ListViewHolder(itemView: View): ViewHolder(itemView) {
        fun bind(movies: MovieModel?) {
            itemView.findViewById<TextView>(R.id.item_title).text = movies?.title
            itemView.findViewById<TextView>(R.id.item_genre).text = movies?.type
            itemView.findViewById<TextView>(R.id.item_year).text = movies?.year

            if(movies?.poster.equals("N/A")) {
                Picasso.with(itemView.context).load(R.drawable.ic_sentiment_dissatisfied_black_24dp).into(itemView.item_image)
            } else {
                Picasso.with(itemView.context).load(movies?.poster).into(itemView.item_image)
            }


            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movies) }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun getItemViewType(position: Int): Int {
        return if(data[position] != null) {
            VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_LOADING
        }
    }

    fun addNullData() {
        data.add(null)
        notifyItemInserted(data.size -1)
    }

    fun addData(movieList: ArrayList<MovieModel?>) {
        Log.d("Jumlah Data", data.size.toString())
        data.clear()
        data.addAll(movieList)
        notifyDataSetChanged()
    }
    fun removeNullData() {
        data.removeAt(data.size - 1)
        notifyItemRemoved(data.size)
    }
}