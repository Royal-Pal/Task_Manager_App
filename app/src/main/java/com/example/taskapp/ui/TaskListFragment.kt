package com.example.taskapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.R
import com.example.taskapp.data.SortColumn
import com.example.taskapp.data.TaskAdapter
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : Fragment() {

    private lateinit var viewModel: TaskListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        inflating recyclerView
        with(task_list) {
            layoutManager = LinearLayoutManager(activity)
            adapter = TaskAdapter {
                findNavController().navigate(
                    TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(it)
                )
            }
        }

//        observing the live data
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            (task_list.adapter as TaskAdapter).submitList(it)
        })

//        handling click on fab button
        add_task.setOnClickListener {
            TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(0L)
        }
    }

//    handling menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    //    handling click on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_sort_priority -> {
                viewModel.changeSortOrder(SortColumn.Priority)
                item.isChecked = true
                true
            }
            R.id.menu_sort_title -> {
                viewModel.changeSortOrder(SortColumn.Title)
                item.isChecked = true
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}