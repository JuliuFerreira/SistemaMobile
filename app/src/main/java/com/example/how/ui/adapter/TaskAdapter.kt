package com.example.how.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.how.R
import com.example.how.databinding.ItemAdpterBinding
import com.example.how.model.Task

class TaskAdapter(
    private val context: Context,
    val taskSelected: (Task, Int) -> Unit

) : ListAdapter<Task, TaskAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val SELECT_BACK: Int = 1
        val SELECT_REMOVE: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_DETAILS: Int = 4
        val SELECT_NEXT: Int = 5

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>(){
            override fun areItemsTheSame(
                oldItem: Task,
                newItem: Task
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(
                oldItem: Task,
                newItem: Task
            ): Boolean {
                return oldItem == newItem && oldItem.description == newItem.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.MyViewHolder {
        return MyViewHolder(
            ItemAdpterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding.textDescription.text = task.description

        holder.binding.btnDelete.setOnClickListener { taskSelected(task, SELECT_REMOVE) }
        holder.binding.btnEdit.setOnClickListener { taskSelected(task, SELECT_EDIT) }
        holder.binding.btnDetails.setOnClickListener { taskSelected(task, SELECT_DETAILS) }

        when (task.status) {
            0 ->{
                holder.binding.idBack.isVisible = false

                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_doing)
                )

                holder.binding.ibNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }
            1 ->{

                holder.binding.idBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_todo)
                )

                holder.binding.ibNext.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_done)
                )
                holder.binding.idBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
                holder.binding.ibNext.setOnClickListener { taskSelected(task, SELECT_NEXT) }
            }
            else -> {
                holder.binding.ibNext.isVisible = false

                holder.binding.idBack.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_doing)
                )
                holder.binding.idBack.setOnClickListener { taskSelected(task, SELECT_BACK) }
            }


        }
    }

    inner class MyViewHolder(val binding: ItemAdpterBinding) :
            RecyclerView.ViewHolder(binding.root)
}
