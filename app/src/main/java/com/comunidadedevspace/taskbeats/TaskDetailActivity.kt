package com.comunidadedevspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class TaskDetailActivity : AppCompatActivity() {

    companion object {
        val TASK_TITLE_EXTRA = "task.title.extra.detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //Recuperar String da tela anterior
        val title: String = intent.getStringExtra(TASK_TITLE_EXTRA) ?: ""
        //Recuperar campo do xml
        val tvTitle = findViewById<TextView>(R.id.tv_task_title_detail)

        //setar um novo texto na tela
        tvTitle.text = title

    }
}