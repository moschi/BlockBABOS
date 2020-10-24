package com.example.blockbabos.presentation

import android.media.Image
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.R
import com.example.blockbabos.domain.model.BaboMovie

class MyBaboMovieRecyclerViewAdapter(
    private val values: List<BaboMovie>,
) : RecyclerView.Adapter<MyBaboMovieRecyclerViewAdapter.ViewHolder>() {


    private var undoSwipe: () -> Boolean = fun (): Boolean {
        return false
    }
    private var undoSwipeIsSet: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_babo_movie, parent, false)
        view.setOnTouchListener { v, event ->
            Toast.makeText(view.getContext(), event.action.toString(), Toast.LENGTH_LONG).show()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.performClick()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (this.undoSwipeIsSet) {
                        this.undoSwipe()
                    }
                    val itemDeleteContainer = view.findViewById<ImageButton>(R.id.item_delete_button)
                    val grow: Animation = ScaleAnimation(
                        0f, 1f,  // Start and end values for the X axis scaling
                        1f, 1f,  // Start and end values for the Y axis scaling
                    )
                    grow.fillAfter = true // Needed to keep the result of the animation
                    grow.duration = 500
                    itemDeleteContainer.startAnimation(grow)
                    itemDeleteContainer.visibility = View.VISIBLE
                    //itemDeleteContainer.clearAnimation()
                    this.undoSwipe = fun(): Boolean {
                        itemDeleteContainer.visibility = View.GONE
                        itemDeleteContainer.clearAnimation()
                        return true
                    }
                    this.undoSwipeIsSet = true
                }
            }
            true
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.movieDbApiId.toString()
        holder.contentView.text = item.title
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val idView: TextView = view.findViewById(R.id.item_number)
        val contentView: TextView = view.findViewById(R.id.content)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }

    }
}