package com.tonivar.ldlhelper.presentation.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.tonivar.ldlhelper.FILE_DOWNLOADED
import com.tonivar.ldlhelper.FILE_NOT_FOUND
import com.tonivar.ldlhelper.HIDE_PROGRESS
import com.tonivar.ldlhelper.SET_FILE_NAME
import com.tonivar.ldlhelper.SHOW_PROGRESS
import com.tonivar.ldlhelper.databinding.FragmentMenuBinding
import com.tonivar.ldlhelper.domain.models.AppResponse
import com.tonivar.ldlhelper.domain.models.Chapter
import com.tonivar.ldlhelper.presentation.ActivityResultLauncherListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.RuntimeException
import java.util.ArrayList

class MenuFragment : Fragment() {
    private val scope1 = CoroutineScope(Dispatchers.Main)
    private val scope2 = CoroutineScope(Dispatchers.Main)
    private var fileName = ""
    private lateinit var binding: FragmentMenuBinding
    private lateinit var vm: TestViewModel
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
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProvider(this)[TestViewModel::class.java]
        subscribeUserDataState()
        subscribeResponseState()
        binding.loadTestButton.setOnClickListener {
            loadTest()
        }
        vm.loadChapters(true)
    }

    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                val uri = Uri.fromParts("package",
                    requireActivity().packageName,
                    null)
                data = uri
            }
            listener.requestPermissions(intent)
        } else {
            listener.requestPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun loadTest() {
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            listener.openFile("*/*") {
                val pathFromUri = it.path.toString()
                val split = pathFromUri.split(":")
                fileName = split[1]
                val storages = ContextCompat.getExternalFilesDirs(requireContext(), null)
                val source = ArrayList<String>()
                storages.forEach { file ->
                    val path = file.path
                        .replaceAfter("Android", "")
                        .replace("Android", "")
                    source.add(path + split[1])
                }
                vm.loadTest(source)
            }
        }
    }

    private fun setFileInfo(name: String) {
        binding.infoTv.text = name
    }

    private fun subscribeResponseState() {
        scope1.launch {
            vm.responseFlow.collect{response ->
                response?.let {
                    if (it is AppResponse){
                        when(it.code) {
                            FILE_DOWNLOADED -> {
                                vm.setFileName(fileName)
                                vm.loadChapters()
                                showToast(it.message)
                            }
                            FILE_NOT_FOUND -> {
                                binding.progressBar.isVisible = false
                                showToast(it.message)
                            }
                            SHOW_PROGRESS -> {
                                binding.progressBar.isVisible = true;
                            }
                            HIDE_PROGRESS -> {
                                binding.progressBar.isVisible = false
                            }
                            SET_FILE_NAME -> {
                                setFileInfo(it.message)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun subscribeUserDataState() {
        scope2.launch {
            vm.userDataFlow.collect{list ->
                val items = ArrayList<String>()
                list?.let {
                    for (item in it) {
                        if (item is Chapter) {
                            items.add(item.title)
                        }
                    }
                }
                val adapter = ArrayAdapter(requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    items)
                binding.chaptersSelector.apply {
                    setAdapter(adapter)
                    invalidate()
                }

            }
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope1.cancel()
        scope2.cancel()
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
