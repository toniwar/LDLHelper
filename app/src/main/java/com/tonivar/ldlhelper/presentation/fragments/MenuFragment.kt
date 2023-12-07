package com.tonivar.ldlhelper.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.tonivar.ldlhelper.R
import com.tonivar.ldlhelper.domain.models.Question
import java.util.ArrayList

class MenuFragment : Fragment() {
    private lateinit var vm: TestViewModel
    private lateinit var loadTestButton: Button
    private lateinit var testTV: TextView
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

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
        registerPermissionLauncher()
        loadTestButton.setOnClickListener {
            loadTest()
        }
        vm.data.observe(requireActivity() as LifecycleOwner) {
            if (it is Question) {
                testTV.text = it.question 
            }
        }
    }

    private fun registerPermissionLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it) {
                Toast.makeText(
                    requireActivity(),
                    "Permission granted!",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Permission deny!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadTest() {
        if (Build.VERSION.SDK_INT < 33) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                vm.loadTest(requireActivity())
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            vm.loadTest(requireActivity())
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
