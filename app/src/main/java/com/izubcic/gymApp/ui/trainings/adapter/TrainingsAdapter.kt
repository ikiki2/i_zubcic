package com.izubcic.gymApp.ui.trainings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.izubcic.gymApp.R
import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.ui.listeners.OnItemClickListener
import com.izubcic.gymApp.ui.trainings.holder.TrainingViewHolder

class TrainingsAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TrainingViewHolder>() {
    private val trainings: MutableList<Training> = mutableListOf()

    fun setItems(list: List<Training>) {
        trainings.clear()
        trainings.addAll(list)
        trainings.sortByDescending { it.date }
        notifyDataSetChanged()
    }

    fun addItems(list: List<Training>) {
        this.trainings.addAll(list)
        trainings.sortByDescending { it.date }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.holder_training, parent, false)
        return TrainingViewHolder(listener, view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = trainings[position]
        holder.run {
            setItem(training)
           listener.let { this }
        }
    }

    override fun getItemCount(): Int = trainings.size

}