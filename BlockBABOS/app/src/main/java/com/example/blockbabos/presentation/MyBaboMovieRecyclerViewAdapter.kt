package com.example.blockbabos.presentation

import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.R
import com.example.blockbabos.domain.dummy.DummyContent.DummyItem
import com.example.blockbabos.domain.model.BaboMovie


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyBaboMovieRecyclerViewAdapter(
    private val values: List<BaboMovie>,
) : RecyclerView.Adapter<MyBaboMovieRecyclerViewAdapter.ViewHolder>() {


    private var undoSwipe: () -> Boolean = fun (): Boolean {
        return false
    }
    private var undoSwipeIsSet: Boolean = false;

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
                        this.undoSwipe();
                    }
                    val itemDeleteContainer = view.findViewById<LinearLayout>(R.id.item_delete_container)
                    itemDeleteContainer.visibility = View.VISIBLE
                    val grow: Animation = ScaleAnimation(
                        1f, 1f,  // Start and end values for the X axis scaling
                        0f, 30f,  // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 30f,  // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 30f
                    )
                    grow.fillBefore = false // Needed to keep the result of the animation
                    grow.duration = 1000
                    grow.repeatCount = 0
                    itemDeleteContainer.startAnimation(grow)
                    itemDeleteContainer.clearAnimation()
                    this.undoSwipe = fun(): Boolean {
                        itemDeleteContainer.visibility = View.GONE
                        return true
                    }
                    this.undoSwipeIsSet = true;
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