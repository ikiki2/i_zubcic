package com.izubcic.gymApp.ui.training_add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.izubcic.gymApp.App
import com.izubcic.gymApp.R
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.commons.extensions.toast
import com.izubcic.gymApp.commons.utils.getRandomString
import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.data.request.UserUpdateProfile
import com.izubcic.gymApp.firebase.authentication.AuthenticationHelperImpl
import com.izubcic.gymApp.firebase.database.DatabaseHelperImpl
import com.izubcic.gymApp.preferences.PreferencesHelperImpl
import kotlinx.android.synthetic.main.activity_add_training.*
import java.text.SimpleDateFormat
import java.util.*

class AddTraningActivity : AppCompatActivity(), AddTrainingView {

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
        setContentView(R.layout.activity_add_training)
        initListeners()
    }

    private fun initListeners() {
        backAdd.onClick { finish() }
        addTraining.onClick { addData() }
    }

    // logic functions

    // view functions
    override fun addData() {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateTimeInstance()
        val formatedDate = formatter.format(date)


        val training = Training(
            id = getRandomString(28),
            weight = weightTrainingAdded.text.toString().toLong(),
            userWeight = userWeightTrainingAdd.text.toString().toLong(),
            title = titleTrainingAdd.text.toString(),
            description = weightTrainingAdded.text.toString(),
            series = seriesTrainingAdd.text.toString().toLong(),
            date = formatedDate
        )

        auth.getCurrentUser()?.run {
            database.addTraining(this.uid, training) {}
        }

        auth.getCurrentUser()?.run {
            val userId = this.uid
            training.userWeight.run {
                database.saveUserWeight(
                    UserUpdateProfile(
                        id = userId,
                        weight = training.userWeight.toString()
                    )
                ) {
                    onSuccess()
                }
            }
        }
    }

    private fun onSuccess() {
        this.toast("Updated Your Weight")
        finish()
    }
}