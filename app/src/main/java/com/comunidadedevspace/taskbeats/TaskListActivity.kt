package com.comunidadedevspace.taskbeats

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    //lista kotlin, apenas os dados
    private var taskList = arrayListOf(
        Task(0, "Contato 1", "99999-9999"),
        Task(0, "Contato 2", "7676767"),
        Task(0, "Contato 3", "64564646")
    )

    private lateinit var ctnContent: LinearLayout

    //Adapter
    private val adapter: TaskListAdapter = TaskListAdapter(::onListItemClicked)

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //pegando resultado
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction
            val task: Task = taskAction.task

            //Tratando o DELETE
            if (taskAction.actionType == ActionType.DELETE.name) {
                val newList = arrayListOf<Task>()
                    .apply {
                        addAll(taskList)
                    }

                //removendo item a lista kotlin
                newList.remove(task)
                showMessage(ctnContent, "O item ${task.title} foi deletado da lista")

                if (newList.size == 0) {
                    ctnContent.visibility = View.VISIBLE
                }

                //atualizar o adapter
                adapter.submitList(newList)
                taskList = newList


                //tratando o CREATE
            } else if (taskAction.actionType == ActionType.CREATE.name) {
                val newList = arrayListOf<Task>()
                    .apply {
                        addAll(taskList)
                    }

                newList.add(task)
                showMessage(ctnContent, "Item ${task.title} criado")

                if (newList.size == 0) {
                    ctnContent.visibility = View.VISIBLE
                }

                adapter.submitList(newList)
                taskList = newList

                //Tratando o UPDATE

            }else if (taskAction.actionType == ActionType.UPDATE.name) {

                val tempEmptyList = arrayListOf<Task>()
                taskList.forEach {
                    if(it.id == task.id){
                        val newItem = Task (it.id, task.title, task.description)
                        tempEmptyList.add(newItem)
                    }else {
                        tempEmptyList.add(it)
                    }
                }

                showMessage(ctnContent, "Item ${task.title} atualizado")
                adapter.submitList(tempEmptyList)
                taskList = tempEmptyList

            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)


        ctnContent = findViewById(R.id.ctn_content)
        //Adapter
        adapter.submitList(taskList)

        // recuperando a RecyclerView do activity_main.xml
        // nome da variavel é rvTasks, do tipo RecyclerView
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val btnFloating = findViewById<FloatingActionButton>(R.id.btn_floating)
        btnFloating.setOnClickListener {
            openTaskListDetail(null)


        }

    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()


    }

    //task: Task é um parâmetro da função
    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)

    }

    private fun openTaskListDetail(task: Task?) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }


}

//enum é um enumerador, irá comparar o name que é uma String
//CRUD - CREATE, READ, UPDATE, DELETE
enum class ActionType {

    DELETE,
    UPDATE,
    CREATE
}

data class TaskAction(
    val task: Task,
    //Esta String por causa do enum
    val actionType: String
) : Serializable

const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"

