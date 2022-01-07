package com.example.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import androidx.lifecycle.Observer
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TaskFormViewModel
    private var mTaskId: Int = 0
    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    private val mPriorityListIds: MutableList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        setListeners()
        setObservers()
        loadPriorities()

        loadTask()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_cancel -> {
                finish()
            }
            R.id.button_save -> {
                save()
            }
            R.id.button_date -> {
                showDatePicker()
            }
        }
    }

    private fun setListeners() {
        button_cancel.setOnClickListener(this)
        button_save.setOnClickListener(this)
        button_date.setOnClickListener(this)
    }

    private fun setObservers() {
        mViewModel.priorityList.observe(this, Observer {
            val list: MutableList<String> = arrayListOf()
            for (item in it) {
                list.add(item.description)
                mPriorityListIds.add(item.id)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            spinner_priority.adapter = adapter
        })

        mViewModel.validation.observe(this, Observer {
            if (it.success()) {
                Toast.makeText(this, R.string.tarefa_salva_com_sucesso, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, it.message(), Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.loadTask.observe(this, Observer {
            edit_description.setText(it.description)
            check_complete.isChecked = it.complete

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(it.dueDate)
            button_date.text = mDateFormat.format(date)
            spinner_priority.setSelection(getPriorityIndex(it.priorityId))
        })
    }

    private fun getPriorityIndex(priorityId: Int): Int {
        return mPriorityListIds.indexOf(priorityId)
    }

    private fun loadPriorities() {
        mViewModel.loadPriorities()
    }

    private fun loadTask() {
        val bundle = intent.extras

        if (bundle != null) {
            mTaskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            mViewModel.load(mTaskId)
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, dayOfMonth)

        button_date.text = mDateFormat.format(c.time)
    }

    fun save() {
        if (validateTask()) {
            val description = edit_description.text.toString()
            val priorityId = mPriorityListIds[spinner_priority.selectedItemPosition]
            val dueDate = button_date.text.toString()
            val complete = check_complete.isChecked

            mViewModel.save(mTaskId, description, priorityId, dueDate, complete)
        } else {
            Toast.makeText(this, R.string.preencha_todos_campos, Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateTask(): Boolean {
        return edit_description.text.toString() != "" && button_date.text.toString() != ""
    }
}
