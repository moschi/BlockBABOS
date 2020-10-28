package com.example.blockbabos.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.R
import com.example.blockbabos.databinding.FragmentBaboMovieBinding
import com.example.blockbabos.domain.listeners.impl.BaboMovieRecyclerViewAdapterSwipeListener
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.moviedbapi.ApiController
import com.omertron.themoviedbapi.model.movie.MovieInfo
import org.w3c.dom.Text
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class BaboMovieRecyclerViewAdapter(
    val listViewModel: ListViewModel,
    private val movies: List<BaboMovie>,
) : RecyclerView.Adapter<BaboMovieRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: FragmentBaboMovieBinding = FragmentBaboMovieBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        itemBinding.root.setOnTouchListener(BaboMovieRecyclerViewAdapterSwipeListener())
        itemBinding.root.setOnClickListener {
            if (!isInformationVisible(it)) {

                val executor: Executor = Executors.newSingleThreadExecutor()
                val fetchMovieInfo = CountDownLatch(1)
                val apiController = ApiController()
                var movieInfo: MovieInfo

                //poster https://image.tmdb.org/t/p/original/h8Rb9gBr48ODIwYUttZNYeMWeUU.jpg

                executor.execute {
                    checkNotNull(itemBinding.baboMovie)
                    println((itemBinding.baboMovie as BaboMovie).movieDbApiId)
                    movieInfo =
                        apiController.getMovieById((itemBinding.baboMovie as BaboMovie).movieDbApiId)
                    it.findViewById<TextView>(R.id.movie_genres).text = movieInfo.genres.toString()
                    it.findViewById<TextView>(R.id.movie_tagline).text = movieInfo.toString()
                    it.findViewById<TextView>(R.id.movie_tagline).text = movieInfo.tagline
                    it.findViewById<TextView>(R.id.movie_release_date).text = movieInfo.releaseDate
                    it.findViewById<ProgressBar>(R.id.movie_vote_average).progress =
                        (movieInfo.voteAverage * 10).toInt()
                    it.findViewById<TextView>(R.id.movie_vote_average_text).text =
                        movieInfo.voteAverage.toString()
                    println(movieInfo.voteAverage)
                    println(movieInfo)
                    fetchMovieInfo.countDown()
                }

                fetchMovieInfo.await()


            }
            toggleInformation(it)


        }
        return ViewHolder(itemBinding)
    }

    private fun isInformationVisible(view: View): Boolean {
        return view.findViewById<LinearLayout>(R.id.movie_information).visibility == View.VISIBLE
    }

    private fun toggleInformation(view: View) {
        if (isInformationVisible(view)) {
            view.findViewById<LinearLayout>(R.id.movie_information).visibility = View.GONE
        } else {
            view.findViewById<LinearLayout>(R.id.movie_information).visibility = View.VISIBLE
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie: BaboMovie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(
        private val binding: FragmentBaboMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: BaboMovie) {
            binding.baboMovie = movie
            binding.listViewModel = listViewModel
            binding.executePendingBindings()
        }

    }
}