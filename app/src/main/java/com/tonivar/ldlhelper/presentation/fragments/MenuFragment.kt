package com.tonivar.ldlhelper.presentation.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.tonivar.ldlhelper.R
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.domain.models.Question
import com.tonivar.ldlhelper.presentation.ActivityResultLauncherListener
import java.lang.RuntimeException
import java.util.ArrayList

class MenuFragment : Fragment() {
    private lateinit var vm: TestViewModel
    private lateinit var loadTestButton: Button
    private lateinit var testTV: TextView
    private lateinit var testOut: TextView
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var listener: ActivityResultLauncherListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityResultLauncherListener) {
            listener = context
        } else {
            throw RuntimeException("Unknown activity")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this)[TestViewModel::class.java]
        loadTestButton = view.findViewById(R.id.search_test_button)
        testTV = view.findViewById(R.id.test_file_tv)
        testOut = view.findViewById(R.id.test_output_text)
        registerPermissionLauncher()
        loadTestButton.setOnClickListener {
            loadTest()
        }
        vm.data.observe(requireActivity() as LifecycleOwner) { list ->
            var str = ""
            list.forEach {
                if (it is Question) {
                    str += it.question + "\n"
                    for (item in it.answers) {
                        str += item.answer + "\n"
                    }
                }
                if (it is Chapter) {
                    str += "#" + (it.id + 1) + ": " + it.title + "\n"
                }
            }
            testOut.text = str
            testOut.invalidate()
        }
    }

    private fun registerPermissionLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(requireContext(), "Permission granted!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun requestPermissions() : Boolean {
        if (Build.VERSION.SDK_INT > 32) {
            return true
        }
        val isHandleIntent = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        val permission = if (isHandleIntent)
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
            else Manifest.permission.READ_EXTERNAL_STORAGE
        if (checkPermission(permission)) {
            return true
        }
        if (!isHandleIntent) {
            permissionLauncher.launch(permission)
        } else {
            val intent = Intent()
            try {
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: Exception) {
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startActivity(intent)
            }
        }
        return checkPermission(permission)
    }

    private fun checkPermission(permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun loadTest() {
        if (!requestPermissions()) {
            return
        }

        listener.launch("*/*") {
            val file = it.toFile()
            vm.loadTest(file)
            testTV.text = file.path
        }
    }

    companion object {
        const val DATA_KEY = "data_key"
        fun newInstance(arg: List<String>? = null) = MenuFragment().apply {
            arguments = Bundle().apply {
                putStringArrayList(DATA_KEY, arg as ArrayList<String>)
            }
        }
    }

}
