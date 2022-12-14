package com.izubcic.gymApp.ui.training_details

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.izubcic.gymApp.App
import com.izubcic.gymApp.R
import com.izubcic.gymApp.commons.constants.TRAINING_KEY
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.commons.extensions.toast
import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.firebase.authentication.AuthenticationHelperImpl
import com.izubcic.gymApp.firebase.database.DatabaseHelperImpl
import com.izubcic.gymApp.preferences.PreferencesHelperImpl
import kotlinx.android.synthetic.main.activity_details.*
import java.io.Serializable

class TrainingDetailsActivity : AppCompatActivity(), DetailsView {

    private var trainingId: String = ""
    private var userId: String = ""

    private val auth: AuthenticationHelperImpl by lazy {
        AuthenticationHelperImpl(
            App.auth,
            DatabaseHelperImpl(App.database),
            PreferencesHelperImpl(App.prefs)
        )
    }

    private val database: DatabaseHelperImpl by lazy {
        DatabaseHelperImpl(App.database)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        getDetails()
        initListeners()
    }

    private fun initListeners() {
        detailsSave.onClick { updateTraining() }
        backDetails.onClick { finish() }
    }

    // logic functions

    private fun getDetails() {
        val intent = this.intent
        val bundle: Bundle? = intent.extras
        val training: Serializable? = bundle?.getSerializable(TRAINING_KEY)
        trainingId = (training as Training).id
        showData(training)
    }

    // view functions
    override fun showData(training: Training) {
        deleteDetails.onClick {
            auth.getCurrentUser()?.run {
                userId = this.uid
                deleteDetails.onClick {
                    database.onItemDeleted(this.uid, training) {
                        onSuccess()
                    }
                }
            }
        }

        titleTrainingDetails.setText(training.title)
        userWeightTrainingDetails.text = String.format("User Weight: %s kg", training.description)
        seriesTrainingDetails.text = String.format("%s series", training.series)
        weightTrainingDetails.text = String.format("Weight %s kg", training.series)
        dateTrainingDetails.text = training.date
        descriptionTrainingDetails.setText(training.description)
    }

    private fun onSuccess() {
        finish()
    }


    private fun updateTraining() {
        auth.getCurrentUser()?.run {

            val training = Training(
                id = trainingId,
                title = titleTrainingDetails.text.toString(),
                description = descriptionTrainingDetails.text.toString()
            )
            this@TrainingDetailsActivity.database.updateTraining(this.uid, training) { trainingIsUpdated() }

        }

    }

    private fun trainingIsUpdated() {
        this.toast("Training is updated")
    }
}