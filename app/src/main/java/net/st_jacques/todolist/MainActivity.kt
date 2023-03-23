package net.st_jacques.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
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

        // Initialise customer taskList adapter
        val taskListAdapter = object : ArrayAdapter<Task>(this, R.layout.task_item, taskList) {
            // Overridden getView fun
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                // Sets view to the initialized task_item layout
                val view = convertView ?: LayoutInflater.from(context)
                    .inflate(R.layout.task_item, parent, false)
                val task = getItem(position)
                val checkedTextView = view.findViewById<CheckedTextView>(R.id.checkedTextView)
                // Gets the textViewTaskDescription from the current task_item layout
                val textViewTaskDescription =
                    view.findViewById<TextView>(R.id.textViewTaskDescription)
                textViewTaskDescription.text = task?.description
                // Set the checkedTextView check state to the state of the task, if it is null, set it to false.
                checkedTextView.isChecked = task?.isComplete ?: false
                // Sets the onClickListener of the checkedTextView
                checkedTextView.setOnClickListener {
                    task?.isComplete = !checkedTextView.isChecked
                    checkedTextView.isChecked = task?.isComplete ?: false
                }
                return view
            }
        }
        // Sets the taskListView adapter
        taskListView.adapter = taskListAdapter

        // Function to submit task to the list
        fun submitTask() {
            if (!editTextNewTask.text.toString().isNullOrBlank()) {
                // Create a new task with the editTextNewTask string, unchecked by default
                val newTask = Task(editTextNewTask.text.toString(), false)
                // Add the new task to the list
                taskList.add(newTask)
                // Clear the editText
                editTextNewTask.setText("")
                // Inform the adapter the data has been changed.
                taskListAdapter.notifyDataSetChanged()
            }
        }

        // Sets the submit button onClickListener to the submit button
        buttonSubmitTask.setOnClickListener { submitTask() }

        // Sets the editText onKeyListener to listen for ENTER keystrokes, and submit the task
        editTextNewTask.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            // If the pressed key is ENTER and the key is being released, submit the task
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                submitTask()
                // The true/false returns the event to the OnKeyListener and tells it if it
                // has handled the event, if true the event was handled, and it is ignored
                // if false, the event was not handled and allow the next listener to handle it.
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })
    }
}

// Data class used in the array adapter for the list view that represents each task.
data class Task(val description: String, var isComplete: Boolean)