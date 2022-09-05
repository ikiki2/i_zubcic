package com.izubcic.gymApp.ui.listeners

import com.izubcic.gymApp.data.model.Training

interface OnItemClickListener {

    fun onTrainingClick(training: Training)

    fun onDeleteClick(training: Training)
}