package com.izubcic.gymApp.ui.trainings.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.holder_training.view.*

class TrainingViewHolder(private val listener: OnItemClickListener, itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun setItem(training: Training) = with(itemView) {
        itemDelete.onClick { listener.onDeleteClick(training) }
        itemTitle.text = training.title
        itemDate.text = training.date
        itemWeight.text = String.format("User Weight: %s", training.userWeight)
        onClick { listener.onTrainingClick(training) }
    }
}