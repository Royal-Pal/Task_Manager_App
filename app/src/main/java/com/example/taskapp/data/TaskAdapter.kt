package com.example.taskapp.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_task_detail.view.*

class TaskAdapter(private val listener: (Long) -> Unit):
    ListAdapter<Task, TaskAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition).id)
            }
        }

        fun bind(task: Task) {
            with(task) {
                when(priority) {
                    PriorityLevel.High.ordinal -> {
                        itemView.task_priority_view.setBackgroundColor(ContextCompat.getColor(
                            containerView.context, R.color.highPriorityColor))
                    }
                    PriorityLevel.Medium.ordinal -> {
                        itemView.task_priority_view.setBackgroundColor(ContextCompat.getColor(
                            containerView.context, R.color.mediumPriorityColor))
                    }
                    else -> {
                        itemView.task_priority_view.setBackgroundColor(ContextCompat.getColor(
                            containerView.context, R.color.lowPriorityColor))
                    }
                }

                itemView.task_title.setText(task.title)
                itemView.task_detail.setText(task.detail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}