package com.example.blockbabos.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.databinding.FragmentBaboMovieBinding
import com.example.blockbabos.domain.listeners.impl.BaboMovieRecyclerViewAdapterSwipeListener
import com.example.blockbabos.domain.model.BaboMovie


class MyBaboMovieRecyclerViewAdapter(
    val listViewModel: ListViewModel,
    private val movies: List<BaboMovie>,
) : RecyclerView.Adapter<MyBaboMovieRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: FragmentBaboMovieBinding = FragmentBaboMovieBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        itemBinding.root.setOnTouchListener(BaboMovieRecyclerViewAdapterSwipeListener())
        return ViewHolder(itemBinding)
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