package com.example.nimbuzzapp

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nimbuzzapp.helper.Event

class MainViewModel : ViewModel() {

    private val _selectedImages = MutableLiveData<List<Uri>>()
    val selectedImages: LiveData<List<Uri>> = _selectedImages

    private val _generatedImageList = MutableLiveData<List<Uri>>()
    val generatedImageList: LiveData<List<Uri>> = _generatedImageList

    val listSize = MutableLiveData<String>()

    private val _selectImagesEvent = MutableLiveData<Event<Unit>>()
    val selectImagesEvent: LiveData<Event<Unit>> = _selectImagesEvent

    fun onImagesSelected(uris: List<Uri>) {
        _selectedImages.value = uris
    }

    fun onGenerateListClicked() {
        val size = listSize.value?.toIntOrNull()
        val selectedImages = selectedImages.value

        if (size != null && selectedImages != null && selectedImages.size >= 2) {
            val generatedList = generateImageList(selectedImages, size)
            _generatedImageList.value = generatedList
        }else{
            _generatedImageList.value = arrayListOf()
        }
    }

    init {
        listSize.value = "50"
    }
    fun onSelectImagesClicked() {
        _selectImagesEvent.value = Event(Unit)
    }

    private fun generateImageList(selectedImages: List<Uri>, listSize: Int): List<Uri> {
        if (selectedImages.size < 2 || listSize <= 0) {
            return emptyList()
        }

        val selectedImage1 = selectedImages.getOrElse(0) { Uri.EMPTY }
        val selectedImage2 = selectedImages.getOrElse(1) { Uri.EMPTY }

        val repeatedImages = (0 until listSize).mapIndexed { index, _ ->
            if (isTriangularNumber(index + 1)) {
                selectedImage1
            } else {
                selectedImage2
            }
        }

        return repeatedImages
    }

    private fun isTriangularNumber(num: Int): Boolean {
        var sum = 0
        var i = 1
        while (sum < num) {
            sum += i
            i++
        }
        return sum == num
    }
}
