package com.example.blockbabos.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.presentation.MyBaboMovieRecyclerViewAdapter
import com.example.blockbabos.R
import com.example.blockbabos.domain.dao.BaboMovieDao
import com.example.blockbabos.persistence.BaboMovieRoomDatabase
import com.example.blockbabos.presentation.ListViewModel
import com.example.blockbabos.presentation.ListViewModelFactory

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
    private var columnCount = 1
    private var baboMovieDao: BaboMovieDao? = null
    private var db: BaboMovieRoomDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_babo_movie_list, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = BaboMovieRoomDatabase.getDatabase(application).baboMovieDao()
        val viewModelFactory = ListViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val listViewModel =
            ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        //        // give the binding object a reference to it.
        //        // binding.sleepTrackerViewModel = sleepTrackerViewModel

        listViewModel.superLiked.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (view is RecyclerView) {
                    with(view) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = MyBaboMovieRecyclerViewAdapter(it)
                    }
                }
            }
        })
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}