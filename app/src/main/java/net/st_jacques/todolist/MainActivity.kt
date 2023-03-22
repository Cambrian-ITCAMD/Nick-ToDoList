package net.st_jacques.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var editTextNewTask: EditText
    private lateinit var buttonSubmitTask: Button
    private lateinit var taskListView: ListView
    private var taskList: ArrayList<Task> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNewTask = findViewById(R.id.editTextNewTask)
        buttonSubmitTask = findViewById(R.id.buttonSubmitTask)
        taskListView = findViewById(R.id.listViewTasks)

//        Testing array
//        taskList = arrayListOf(
//            Task("Hello", false),
//            Task("Cruel", false),
//            Task("World", false)
//        )

        // Initialise customer taskList adapter
        val taskListAdapter = object: ArrayAdapter<Task>(this,R.layout.task_item, taskList) {
            // Overridden getView
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
                val task = getItem(position)
                val checkedTextView = view.findViewById<CheckedTextView>(R.id.checkedTextView)
                val textViewTaskDescription = view.findViewById<TextView>(R.id.textViewTaskDescription)
                // Set the taskDescriptionTextView text to task description
                textViewTaskDescription.text = task?.description
                // Set the checkedTextView check state to the state of the task, if it is null, set it to false.
                checkedTextView.isChecked = task?.isComplete ?: false
                // Sets the onClickListener of the checkedTextView
                checkedTextView.setOnClickListener {
                    // Toggle the Task object boolean
                    task?.isComplete = !checkedTextView.isChecked
                    // Sets the checkTextView checkmark to the state of the task object
                    checkedTextView.isChecked = task?.isComplete ?: false
                }
                return view
            }
        }
        // Sets the taskListView adapter
        taskListView.adapter = taskListAdapter

        buttonSubmitTask.setOnClickListener{
            val newTask = Task(editTextNewTask.text.toString(), false)
            editTextNewTask.setText("")
            taskList.add(newTask)
            taskListAdapter.notifyDataSetChanged()
        }
    }
}

// Data class used in the array adapter for the list view that represents each task.
data class Task(val description: String, var isComplete: Boolean)