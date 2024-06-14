package com.comunidadedevspace.taskbeats

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var ctnContent: LinearLayout

    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter(::onListItemClicked)
    }

    private val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "taskbeats-database"
        ).build()
    }

    private val dao by lazy {
        dataBase.taskDao()
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction
            val task: Task = taskAction.task


            when (taskAction.actionType) {

                ActionType.DELETE.name -> deleteById(task.id)

                ActionType.CREATE.name -> insertIntoDatabase(task)

                ActionType.UPDATE.name -> updateIntoDatabase(task)

            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        setSupportActionBar(
            findViewById(R.id.toolbar)
        )

        listFromDatabase()

        ctnContent = findViewById(R.id.ctn_content)

        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val btnFloating = findViewById<FloatingActionButton>(R.id.btn_floating)
        btnFloating.setOnClickListener {
            openTaskListDetail(null)
        }

    }

    private fun insertIntoDatabase(task: Task) {
        CoroutineScope(IO).launch {
            dao.insert(task)
            listFromDatabase()
        }
    }

    private fun updateIntoDatabase(task: Task) {
        CoroutineScope(IO).launch {
            dao.update(task)
            listFromDatabase()
        }
    }

    private fun deleteAll() {
        CoroutineScope(IO).launch {
            dao.deleteAll()
            listFromDatabase()
        }
    }

    private fun deleteById(id: Int) {
        CoroutineScope(IO).launch {
            dao.deleteById(id)
            listFromDatabase()
        }
    }

    private fun listFromDatabase() {
        CoroutineScope(IO).launch {
            val myDatabaseList: List<Task> = dao.getAll()
            adapter.submitList(myDatabaseList)
        }
    }


    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()


    }

    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)

    }

    private fun openTaskListDetail(task: Task?) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_task -> {
                deleteAll()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}

enum class ActionType {

    DELETE, UPDATE, CREATE
}

data class TaskAction(
    val task: Task,
    //Esta String por causa do enum
    val actionType: String
) : Serializable

const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"

