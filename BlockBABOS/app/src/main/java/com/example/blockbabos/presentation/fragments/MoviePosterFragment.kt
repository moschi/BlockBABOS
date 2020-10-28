package com.example.blockbabos.presentation.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.blockbabos.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.net.URL


private const val ARG_IMAGE_URL = "IMAGE_URL"

class MoviePosterFragment : Fragment() {
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(ARG_IMAGE_URL)
        }
    }

    private fun setImage(image: Bitmap) {
        val activity = requireNotNull(activity)
        val imageView =
            requireNotNull(activity.findViewById<ImageView>(R.id.imageView_Movie_Poster))
        imageView.setImageBitmap(image)
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
        return inflater.inflate(R.layout.fragment_movie_poster, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setImage(downloadImage(imageUrl!!))
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(imageUrl: String) =
            MoviePosterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }
    }
}