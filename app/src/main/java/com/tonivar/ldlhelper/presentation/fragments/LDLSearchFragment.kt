package com.tonivar.ldlhelper.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tonivar.ldlhelper.R
import java.util.ArrayList

class LDLSearchFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_l_d_l_search, container, false)
    }

    companion object {
        const val DATA_KEY = "data_key"
        fun newInstance(arg: List<String>? = null) = LDLSearchFragment().apply {
            arguments = Bundle().apply {
                putStringArrayList(DATA_KEY, arg as ArrayList<String>)
            }
        }
    }
}