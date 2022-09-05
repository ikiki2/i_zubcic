package com.izubcic.gymApp.ui.trainings.views

import com.izubcic.gymApp.data.model.Training

interface TrainingListView {

    fun setTrainings(trainings: List<Training>)

    fun addTrainings(trainings: List<Training>)

    fun showMessageEmptyList()

    fun hideMessageEmptyList()

}