package com.shubham.dishapp.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shubham.dishapp.databinding.ItemCustomListLayoutBinding
import com.shubham.dishapp.view.activities.AddUpdateDishActivity


class CustomListItemAdapter(
        private val activity: Activity,
        private val listItems: List<String>,
        private val selection: String
) :
        RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListLayoutBinding =
                ItemCustomListLayoutBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = listItems[position]

        holder.tvText.text = item

        holder.itemView.setOnClickListener {

            if (activity is AddUpdateDishActivity) {
                activity.selectedListItem(item, selection)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    class ViewHolder(view: ItemCustomListLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvText
    }
}