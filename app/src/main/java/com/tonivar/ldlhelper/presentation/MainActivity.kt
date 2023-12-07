package com.tonivar.ldlhelper.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.tonivar.ldlhelper.R
import com.tonivar.ldlhelper.presentation.fragments.FragmentListener
import com.tonivar.ldlhelper.presentation.fragments.LDLSearchFragment
import com.tonivar.ldlhelper.presentation.fragments.LDLTestFragment
import com.tonivar.ldlhelper.presentation.fragments.MenuFragment
import com.tonivar.ldlhelper.presentation.fragments.StartFragment

class MainActivity : AppCompatActivity(), FragmentListener {
    private val dataKeys = mutableListOf<String>()
    private lateinit var uri: ActivityResultLauncher<String>
    lateinit var activityResultLauncher: (Uri?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFragment(StartFragment(), null)
        uri = this.registerForActivityResult(ActivityResultContracts.GetContent()){
            activityResultLauncher.invoke(it)
        }
    }

    override fun setAction(
        fragmentName: FragmentListener.Companion.FragmentName,
        args: List<String>?
    ) {
        args?.let {
            dataKeys.clear()
            for (item in it) {
                dataKeys.add(item)
            }
        }
        when(fragmentName) {
            FragmentListener.Companion.FragmentName.START -> openFragment(StartFragment(), null)
            FragmentListener.Companion.FragmentName.MENU -> openFragment(MenuFragment(), null)
            FragmentListener.Companion.FragmentName.TEST -> openFragment(LDLTestFragment(), dataKeys.toList())
            FragmentListener.Companion.FragmentName.SEARCH -> openFragment(LDLSearchFragment(), dataKeys.toList())
        }
    }

    private fun openFragment(fragment: Fragment, args: List<String>?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }


}