package com.example.pexelswallpaper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pexelswallpaper.databinding.ListItemsWallpaperBinding
import com.example.pexelswallpaper.models.Photo

class WallpaperAdapter: RecyclerView.Adapter<WallpaperAdapter.HeaderViewHolder>() {

    private var arrayList: List<Photo> = ArrayList()
    private var listener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding = ListItemsWallpaperBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeaderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(arrayList[position])
        holder.cdMain.setOnClickListener{ listener?.onItemClick(arrayList[position]) }
    }

    class HeaderViewHolder(private val binding: ListItemsWallpaperBinding) : RecyclerView.ViewHolder(binding.root) {
        val cdMain = binding.cdMain
        fun bind(item: Photo) {
            Glide.with(binding.root.context).load(item.src.small).into(binding.wallpaper)
        }
    }

    fun submitList(arrayList: List<Photo>)
    {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onItemClick(item: Photo)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }
}