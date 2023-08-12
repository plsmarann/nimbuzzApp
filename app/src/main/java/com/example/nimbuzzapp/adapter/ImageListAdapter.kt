package com.example.nimbuzzapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nimbuzzapp.databinding.ListImageBinding

class ImageListAdapter : ListAdapter<Uri, RecyclerView.ViewHolder>(
    ImageDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TagViewHolder(
            ListImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as TagViewHolder).binding.index = (position+1).toString()
        (holder as TagViewHolder).binding.imageView1.setImageURI(item)
    }

    class TagViewHolder(
        val binding:    ListImageBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

private class ImageDiffCallback : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem:  Uri): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem:  Uri, newItem:  Uri): Boolean {
        return oldItem == newItem
    }
}