package com.example.blockbabos.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.R
import com.example.blockbabos.domain.listeners.impl.BaboMovieRecyclerViewAdapterSwipeListener
import com.example.blockbabos.domain.model.BaboMovie

class MyBaboMovieRecyclerViewAdapter(
    private val values: List<BaboMovie>,
) : RecyclerView.Adapter<MyBaboMovieRecyclerViewAdapter.ViewHolder>() {

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.fragment_babo_movie, parent, false)
    view.setOnTouchListener(BaboMovieRecyclerViewAdapterSwipeListener())
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