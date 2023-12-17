package com.tonivar.ldlhelper.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.tonivar.ldlhelper.R
import com.tonivar.ldlhelper.presentation.fragments.FragmentListener
import com.tonivar.ldlhelper.presentation.fragments.LDLSearchFragment
import com.tonivar.ldlhelper.presentation.fragments.LDLTestFragment
import com.tonivar.ldlhelper.presentation.fragments.MenuFragment
import com.tonivar.ldlhelper.presentation.fragments.StartFragment

class MainActivity : AppCompatActivity(), FragmentListener, ActivityResultLauncherListener {
    private val dataKeys = mutableListOf<String>()
    private lateinit var launcher: ActivityResultLauncher<String>
    private var uriCallback: ((Uri) -> Unit)? = null
    private var permissionLauncher: ActivityResultLauncher<String>? = null
    private var fileAccessResultLauncher: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            registerFileAccessResultLauncher()
        } else {
            registerPermissionLauncher()
        }
        launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                uriCallback?.invoke(it)
            }
        }
        openFragment(StartFragment(), null)
    }

    private fun registerPermissionLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun registerFileAccessResultLauncher() {
        fileAccessResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT)
                        .show()
                }
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

    override fun openFile(fileType: String, callback: ((Uri)->Unit)?) {
        launcher.launch(fileType)
        uriCallback = {
            callback?.invoke(it)
        }
    }

    override fun <T> requestPermissions(request: T) {
        when (request) {
            is Intent -> fileAccessResultLauncher?.launch(request)
            is String -> permissionLauncher?.launch(request)
        }
    }
}
