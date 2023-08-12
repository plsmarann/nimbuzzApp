package com.example.nimbuzzapp

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.nimbuzzapp.adapter.ImageListAdapter
import com.example.nimbuzzapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private val getContent = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris: List<Uri>? ->
        uris?.let { viewModel.onImagesSelected(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.toolbar.setNavigationOnClickListener { view ->
            binding.showList = false
        }
        viewModel.selectImagesEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                checkPermissionsAndSelectImages()
            }
        }
        viewModel.generatedImageList.observe(this) { images ->
            // Display the generated image list in ImageView widgets
            if(images.isNotEmpty()) {
                val adapter = ImageListAdapter()
                binding.list.adapter = adapter
                adapter.submitList(images)
                binding.showList = true

            }else{
               var size =  viewModel.listSize.value?.toIntOrNull()
                var text: CharSequence = "Please select two images"
                if(size!= null) {
                    text = "Please select two images"
                }else{
                   text = "Please enter a list size"
                }
                    val duration = Toast.LENGTH_SHORT
                    binding.showList = false

                    val toast = Toast.makeText(this, text, duration)
                    toast.show()

            }
        }

    }

    private fun checkPermissionsAndSelectImages() {
        val permission = Manifest.permission.READ_MEDIA_IMAGES
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            selectImages()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_READ_IMAGES)
        }
    }

    private fun selectImages() {
        getContent.launch("image/*")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_IMAGES) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.onSelectImagesClicked()
            }
        }
    }

    companion object {
        private const val REQUEST_READ_IMAGES = 1
    }
}
