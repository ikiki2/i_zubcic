package com.izubcic.gymApp.ui.training_details

import com.izubcic.gymApp.data.model.Training

interface DetailsView {

    fun showData(training: Training)
}