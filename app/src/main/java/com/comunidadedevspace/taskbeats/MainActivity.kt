package com.comunidadedevspace.taskbeats

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Kotlin
        val taskList = listOf<Task>(
            Task("Contato 1", "99999-9999"),
            Task("Contato 2", "8888-8888"),
            Task("Title 2", "Desc 2"),
            Task("Title 3", "Desc 3"),
            Task("Title 4", "Desc 4"),
            Task("Title 5", "Desc 5"),
            Task("Title 6", "Desc 6"),
            Task("Title 7", "Desc 7"),
            Task("Title 8", "Desc 8"),
            Task("Title 9", "Desc 9"),
            Task("Title 10", "Desc 10"),
            Task("Title 11", "Desc 11"),
            Task("Title 12", "Desc 12"),
            Task("Title 13", "Desc 13"),
            Task("Title 14", "Desc 14"),
            Task("Title 15", "Desc 15"),
        )

        //Adapter
        val adapter: TaskListAdapter = TaskListAdapter(taskList, ::openTaskDetailView)

        // recuperando a RecyclerView do activity_main.xml
        // nome da variavel é rvTasks, do tipo RecyclerView
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter


    }
    //task: Task é um parâmetro da função
    private fun openTaskDetailView(task: Task): Unit{
        val intent = Intent(this, TaskDetailActivity::class.java)
            . apply {
                putExtra(TaskDetailActivity.TASK_TITLE_EXTRA, task.title)
            }
        startActivity(intent)
    }
}