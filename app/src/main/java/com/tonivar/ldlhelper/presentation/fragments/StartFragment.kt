package com.tonivar.ldlhelper.presentation.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.tonivar.ldlhelper.R
import java.lang.RuntimeException
import java.util.ArrayList

class StartFragment : Fragment() {

    private lateinit var menuButton: Button
    private lateinit var testButton: Button
    private lateinit var searchButton: Button
    private lateinit var fragmentListener: FragmentListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            fragmentListener = context
        } else {
            throw RuntimeException("Unknown element: $context")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuButton = view.findViewById(R.id.menu_button)
        testButton = view.findViewById(R.id.test_button)
        searchButton = view.findViewById(R.id.search_button)
        menuButton.setOnClickListener {
            fragmentListener.setAction(FragmentListener.Companion.FragmentName.MENU, null)
        }
    }

    companion object {
        const val DATA_KEY = "data_key"
        fun newInstance(arg: List<String>? = null) = StartFragment().apply {
            arguments = Bundle().apply {
                putStringArrayList(DATA_KEY, arg as ArrayList<String>)
            }
        }
    }
}