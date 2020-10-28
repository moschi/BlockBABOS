package com.example.blockbabos.presentation.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.blockbabos.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.net.URL


private const val ARG_IMAGE_URL = "IMAGE_URL"
private const val ARG_TAGLINE = "TAGLINE"
private const val ARG_GENRES = "GENRES"
private const val ARG_RELEASE_DATE = "RELEASE_DATE"
private const val ARG_VOTE_AVERAGE = "VOTE_AVERAGE"
private const val ARG_TITLE = "TITLE"

class MovieInfoFragment : Fragment() {
    private var imageUrl: String? = null
    private var tagLine: String? = null
    private var genres: String? = null
    private var releaseDate: String? = null
    private var voteAverage: Float = 0F
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL)
            tagLine = it.getString(ARG_TAGLINE)
            genres = it.getString(ARG_GENRES)
            releaseDate = it.getString(ARG_RELEASE_DATE)
            voteAverage = it.getFloat(ARG_VOTE_AVERAGE)
            title = it.getString(ARG_TITLE)
        }
    }

    private fun setImage(image: Bitmap) {
        val activity = requireNotNull(activity)
        if(activity.findViewById<ImageView>(R.id.imageView_Movie_Poster) != null){
            val imageView =
                requireNotNull(activity.findViewById<ImageView>(R.id.imageView_Movie_Poster))
            imageView.setImageBitmap(image)
        }
    }

    private fun setValues() {
        val activity = requireNotNull(activity)
        val genreView = requireNotNull(activity.findViewById<TextView>(R.id.movie_genres))
        val tagLineView = requireNotNull(activity.findViewById<TextView>(R.id.movie_tagline))
        val releaseDateView =
            requireNotNull(activity.findViewById<TextView>(R.id.movie_release_date))
        val voteAverageView =
            requireNotNull(activity.findViewById<ProgressBar>(R.id.movie_vote_average))
        val voteAverageTextView =
            requireNotNull(activity.findViewById<TextView>(R.id.movie_vote_average_text))
        val titleTextView = requireNotNull(activity.findViewById<TextView>(R.id.movie_title))

        genreView.text = genres
        tagLineView.text = tagLine
        releaseDateView.text = releaseDate
        voteAverageView.progress = (voteAverage.times(10)).toInt()
        voteAverageTextView.text = voteAverage.toString()
        titleTextView.text = title
    }

    private fun downloadImage(imageUrl: String): Bitmap {
        // todo: the code in this method is rather smelly since it forces the download of the image
        // in the main thread - it should be optimized later!
        val downloadRoutine = GlobalScope.async {
            val url = URL(imageUrl)
            return@async BitmapFactory.decodeStream(url.openConnection().getInputStream())
        }
        var bmp: Bitmap? = null
        runBlocking {
            bmp = downloadRoutine.await()
        }

        return bmp!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setImage(downloadImage(imageUrl!!))
        setValues()
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(imageUrl: String) =
            MovieInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }
    }
}