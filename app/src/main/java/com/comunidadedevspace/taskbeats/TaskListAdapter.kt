package com.comunidadedevspace.taskbeats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskListAdapter(
    private val listTask: List<Task>,
    //a linha 13 foi adicionada na criação de um click, pedindo um novo construtor
    //--> Unit quer dizer que a função não retorno nenhum, então preciso especificar isso
    private val openTaskDetailView: (task: Task) -> Unit
) :
    RecyclerView.Adapter<TaskListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
        val view: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_task, parent, false)

        return TaskListViewHolder(view)

    }

    //Tamanho da minha lista
    override fun getItemCount(): Int {
        return listTask.size
    }


    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
        val task = listTask[position]
        holder.bind(task, openTaskDetailView)
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