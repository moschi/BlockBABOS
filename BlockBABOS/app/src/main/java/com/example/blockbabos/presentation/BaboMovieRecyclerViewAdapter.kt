package com.example.blockbabos.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.R
import com.example.blockbabos.databinding.FragmentBaboMovieBinding
import com.example.blockbabos.domain.listeners.impl.BaboMovieRecyclerViewAdapterSwipeListener
import com.example.blockbabos.domain.model.BaboMovie
import com.example.blockbabos.domain.moviedbapi.ApiController
import com.example.blockbabos.presentation.fragments.MovieInfoFragment
import com.omertron.themoviedbapi.model.movie.MovieInfo
import java.lang.StringBuilder
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
            toggleInformation(it, itemBinding)
        }
        return ViewHolder(itemBinding)
    }

    private fun isInformationVisible(view: View): Boolean {
        return view.findViewById<LinearLayout>(R.id.movie_information) != null && view.findViewById<LinearLayout>(
            R.id.movie_information
        ).visibility == View.VISIBLE
    }

    private fun toggleInformation(view: View, itemBinding: FragmentBaboMovieBinding) {
        if (isInformationVisible(view)) {
            if (view.findViewById<LinearLayout>(R.id.movie_information) != null) {
                view.findViewById<LinearLayout>(R.id.movie_information).visibility = View.GONE
            }        } else {
            showMovieInfoFragment(view, itemBinding)
            if (view.findViewById<LinearLayout>(R.id.movie_information) != null) {
                view.findViewById<LinearLayout>(R.id.movie_information).visibility = View.VISIBLE
            }

        }
    }

    private fun showMovieInfoFragment(view: View, itemBinding: FragmentBaboMovieBinding) {
        if (view.findViewById<FrameLayout>(R.id.fragment_movie_info) != null) {
            val fragmentManager = (view.context as AppCompatActivity).supportFragmentManager
            val ft: FragmentTransaction = fragmentManager.beginTransaction()
            ft.replace(R.id.fragment_movie_info, renderInfoFragment(itemBinding))
            ft.commit()
        }
    }

    private fun renderInfoFragment(
        itemBinding: FragmentBaboMovieBinding
    ): MovieInfoFragment {
        val executor: Executor = Executors.newSingleThreadExecutor()
        val fetchMovieInfo = CountDownLatch(1)
        val apiController = ApiController()
        lateinit var movieInfo: MovieInfo

        executor.execute {
            checkNotNull(itemBinding.baboMovie)
            movieInfo =
                apiController.getMovieById((itemBinding.baboMovie as BaboMovie).movieDbApiId)
            fetchMovieInfo.countDown()
        }
        fetchMovieInfo.await()

        val tagLine = movieInfo.tagline
        val genresBuilder = StringBuilder("Genres: ")

        movieInfo.genres.forEach {
                el -> genresBuilder.append(el.name + " ")
        }
        val releaseDate = movieInfo.releaseDate
        val voteAverage = movieInfo.voteAverage
        val title = movieInfo.title


        val baseUrl = "https://image.tmdb.org/t/p/original"

        val args = Bundle()
        args.putString(
            "IMAGE_URL",
            baseUrl + movieInfo.posterPath.toString()
        )
        args.putString("TAGLINE", tagLine)
        args.putString("GENRES", genresBuilder.toString())
        args.putString("RELEASE_DATE", releaseDate)
        args.putFloat("VOTE_AVERAGE", voteAverage)
        args.putString("TITLE", title)
        val moviePosterFragment = MovieInfoFragment()
        moviePosterFragment.arguments = args

        return moviePosterFragment
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