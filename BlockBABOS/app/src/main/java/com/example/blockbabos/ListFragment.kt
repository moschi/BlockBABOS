package com.example.blockbabos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blockbabos.dao.BaboMovieDao
import com.example.blockbabos.dummy.DummyContent
import com.example.blockbabos.model.BaboMovie
import com.example.blockbabos.persistence.BaboMovieRoomDatabase

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
        // setupDb(); TODO throws a IllegalStateException
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                // TODO does not work yet
                // val entries: List<BaboMovie> = baboMovieDao!!.getEntries()
                adapter = MyBaboMovieRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    private fun setupDb() {
        db = BaboMovieRoomDatabase.getDatabase(requireContext())
        baboMovieDao = db!!.baboMovieDao()
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