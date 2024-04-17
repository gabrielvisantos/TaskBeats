package com.comunidadedevspace.taskbeats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TaskListAdapter(

    //a linha 13 foi adicionada na criação de um click, pedindo um novo construtor
    //--> Unit quer dizer que a função não retorno nenhum, então preciso especificar isso
    private val openTaskDetailView: (task: Task) -> Unit
) : ListAdapter<Task, TaskListViewHolder>(TaskListAdapter){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_task, parent, false)

        return TaskListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task, openTaskDetailView)
    }

    companion object : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem

        }
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.title == newItem.title&&
                    oldItem.description == newItem.description
        }

    }
}

//O TaskViewHolder esta ligado com o item_task.xml
class TaskListViewHolder(
    private val view: View
) : RecyclerView.ViewHolder(view) {

    val tvTaskTitle = view.findViewById<TextView>(R.id.tv_task_title)
    val tvTaskDescription = view.findViewById<TextView>(R.id.tv_task_description)

    fun bind(
        task: Task,
        openTaskDetailView: (task: Task) -> Unit
    ) {
        tvTaskTitle.text = task.title
        tvTaskDescription.text = task.description

        view.setOnClickListener {
            openTaskDetailView.invoke(task)
        }
    }
}