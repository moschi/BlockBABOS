package com.example.blockbabos.dropdown

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast

class DropdownOnItemSelectedListener : OnItemSelectedListener {
    override fun onItemSelected(
        parent: AdapterView<*>,
        view: View?,
        pos: Int,
        id: Long
    ) {
        Toast.makeText(
            parent.context,
            "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onNothingSelected(arg0: AdapterView<*>?) {
        // TODO Auto-generated method stub
    }
}