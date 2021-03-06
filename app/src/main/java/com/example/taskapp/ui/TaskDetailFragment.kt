package com.example.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.data.PriorityLevel
import com.example.taskapp.R
import com.example.taskapp.data.Task
import com.example.taskapp.data.TaskStatus
import kotlinx.android.synthetic.main.fragment_task_detail.*

class TaskDetailFragment : Fragment() {

    private lateinit var viewModel: TaskDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setting up the spinner for priorities
        val priorities = mutableListOf<String>()
        PriorityLevel.values().forEach {
            priorities.add(it.name)
        }
        task_priority.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_item, priorities)

//        handling item selection of Spinner
        task_priority?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateTaskPriorityView(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

//        setting up task inside of ViewModel
        val id = TaskDetailFragmentArgs.fromBundle(requireArguments()).taskId
        viewModel.setTaskId(id)

        viewModel.task.observe(viewLifecycleOwner, Observer {
            it?.let { setData(it) }
        })

        save_task.setOnClickListener {
            saveTask()
        }

        delete_task.setOnClickListener {
            deleteTask()
        }
    }

    private fun setData(task: Task) {
        updateTaskPriorityView(task.priority)
        task_title.setText(task.title)
        task_detail.setText(task.detail)
        if(task.status == TaskStatus.Open.ordinal) {
            status_open.isChecked = true
        } else {
            status_closed.isChecked = true
        }
        task_priority.setSelection(task.priority)
    }

    private fun saveTask() {
        val title: String = task_title.text.toString()
        val detail: String = task_detail.text.toString()
        val priority: Int = task_priority.selectedItemPosition

        val selectedStatusButton: RadioButton = status_group.findViewById(status_group.checkedRadioButtonId)
        var status: Int = TaskStatus.Open.ordinal
        if(selectedStatusButton.text == TaskStatus.Closed.name) {
            status = TaskStatus.Closed.ordinal
        }
        val task = Task(viewModel.taskId.value!!, title, detail, priority, status)

        viewModel.saveTask(task)

        requireActivity().onBackPressed()
    }

    private fun deleteTask() {
        viewModel.deleteTask()

        requireActivity().onBackPressed()
    }

    //    function to update the color of priority view as per our selection
    private fun updateTaskPriorityView(priority: Int) {
        when(priority) {
            PriorityLevel.Low.ordinal -> {
                task_priority_view.setBackgroundColor(ContextCompat.getColor(requireActivity(),
                    R.color.lowPriorityColor
                ))
            }
            PriorityLevel.Medium.ordinal -> {
                task_priority_view.setBackgroundColor(ContextCompat.getColor(requireActivity(),
                    R.color.mediumPriorityColor
                ))
            }
            PriorityLevel.High.ordinal -> {
                task_priority_view.setBackgroundColor(ContextCompat.getColor(requireActivity(),
                    R.color.highPriorityColor
                ))
            }
        }
    }
}