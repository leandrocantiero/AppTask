package com.example.tasks.view.viewholder

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.service.listener.TaskListener
import com.example.tasks.service.model.TaskModel
import com.example.tasks.service.repository.PriorityRepository
import java.text.SimpleDateFormat
import java.util.*

class TaskViewHolder(itemView: View, val listener: TaskListener) :
    RecyclerView.ViewHolder(itemView) {

    private val mPriorityRepository = PriorityRepository(itemView.context)
    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    private var mTextDescription: TextView = itemView.findViewById(R.id.text_description)
    private var mTextPriority: TextView = itemView.findViewById(R.id.text_priority)
    private var mTextDueDate: TextView = itemView.findViewById(R.id.text_due_date)
    private var mImageTask: ImageView = itemView.findViewById(R.id.image_task)

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    fun bindData(taskModel: TaskModel) {

        this.mTextDescription.text = taskModel.description
        this.mTextPriority.text = mPriorityRepository.getById(taskModel.priorityId).description
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(taskModel.dueDate)
        this.mTextDueDate.text = mDateFormat.format(date)
        mImageTask.setImageResource(if (taskModel.complete) R.drawable.ic_done else R.drawable.ic_todo)

        mTextDescription.setOnClickListener { listener.onListClick(taskModel.id) }
        mTextPriority.setOnClickListener { listener.onListClick(taskModel.id) }

        mImageTask.setOnClickListener {
            if (taskModel.complete)
                listener.onUndoClick(taskModel.id)
            else {
                listener.onCompleteClick(taskModel.id)
            }
        }

        mTextDescription.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.remocao_de_tarefa)
                .setMessage(R.string.remover_tarefa)
                .setPositiveButton(R.string.sim) { dialog, which ->
                    listener.onDeleteClick(taskModel.id)
                }
                .setNeutralButton(R.string.cancelar, null)
                .show()
            true
        }

    }

}