package com.example.blockbabos.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.presentation.MyBaboMovieRecyclerViewAdapter
import com.example.blockbabos.R
import com.example.blockbabos.databinding.FragmentBaboMovieListBinding
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import com.example.blockbabos.presentation.ListViewModel
import com.example.blockbabos.presentation.ListViewModelFactory
import com.google.android.material.appbar.MaterialToolbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment() {
    private lateinit var binding: FragmentBaboMovieListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val toolbar = activity?.findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar?.title = getString(R.string.watchlist)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_babo_movie_list,
            container,
            false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = BaboMovieRoomDatabase.getDatabase(application).baboMovieDao()
        val viewModelFactory = ListViewModelFactory(dataSource, application)
        val listViewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)
        binding.listViewModel = listViewModel
        binding.list.adapter = MyBaboMovieRecyclerViewAdapter(listViewModel, emptyList())

        listViewModel.superLiked.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.list.adapter = MyBaboMovieRecyclerViewAdapter(listViewModel, it)
            }
        })
        return binding.root
    }

    companion object {
        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() = ListFragment()
    }
}