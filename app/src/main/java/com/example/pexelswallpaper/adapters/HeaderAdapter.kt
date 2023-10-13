package com.example.pexelswallpaper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pexelswallpaper.R
import com.example.pexelswallpaper.databinding.ListItemHeaderBinding
import com.example.pexelswallpaper.models.HeaderModel

class HeaderAdapter(private val context: Context) :
    RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    private var arrayList: List<HeaderModel> = ArrayList()
    private var listener: OnClickListener? = null
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    private var firstPosition = 0
    private var is2ndClicked = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val binding =
            ListItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeaderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(arrayList[position])
        if (!is2ndClicked) {
            if (firstPosition == position) {
                holder.tvSearch.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.cdMain.setBackgroundResource(R.drawable.ripple_effect_selected)
            } else {
                holder.cdMain.setBackgroundResource(R.drawable.ripple_effect)
                holder.tvSearch.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
            }
        }
        holder.cdMain.setOnClickListener {
            selectTaskListItem(position)
            if (position != RecyclerView.NO_POSITION) {
                onClickItem(position)
            }
            listener!!.onItemClick(arrayList[position])
        }
    }

    private fun isSelected(position: Int): Boolean {
        return position == selectedItemPosition
    }

    inner class HeaderViewHolder(private val binding: ListItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cdMain = binding.cdMain
        val tvSearch = binding.tvSearch

        fun bind(item: HeaderModel) {
            binding.tvSearch.text = item.name
            updateBackground(
                isSelected(adapterPosition),
                binding.cdMain,
                binding.tvSearch,
                binding.root.context
            )
        }
    }

    fun submitList(arrayList: List<HeaderModel>) {
        this.arrayList = arrayList
        notifyDataSetChanged()
    }

    interface OnClickListener {
        fun onItemClick(item: HeaderModel)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    private fun onClickItem(position: Int) {
        val previousSelectedItemPosition = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(previousSelectedItemPosition)
        notifyItemChanged(selectedItemPosition)
    }

    private fun updateBackground(
        isSelected: Boolean,
        cdMain: LinearLayout,
        tvSearch: TextView,
        context: Context
    ) {
        if (isSelected) {
            is2ndClicked = true
            tvSearch.setTextColor(ContextCompat.getColor(context, R.color.white))
            cdMain.setBackgroundResource(R.drawable.ripple_effect_selected)
        } else {
            tvSearch.setTextColor(ContextCompat.getColor(context, R.color.black))
            cdMain.setBackgroundResource(R.drawable.ripple_effect)
        }
    }

    fun selectTaskListItem(pos: Int) {
        val previousItem: Int = firstPosition
        firstPosition = pos
        notifyItemChanged(previousItem)
        notifyItemChanged(pos)
    }
}