package com.izubcic.gymApp.ui.trainings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.izubcic.gymApp.App
import com.izubcic.gymApp.R
import com.izubcic.gymApp.commons.constants.TRAINING_KEY
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.commons.extensions.toast
import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.data.model.User
import com.izubcic.gymApp.data.request.UserUpdateProfile
import com.izubcic.gymApp.firebase.authentication.AuthenticationHelperImpl
import com.izubcic.gymApp.firebase.database.DatabaseHelperImpl
import com.izubcic.gymApp.preferences.PreferencesHelperImpl
import com.izubcic.gymApp.ui.listeners.OnItemClickListener
import com.izubcic.gymApp.ui.signIn.SignInActivity
import com.izubcic.gymApp.ui.training_details.TrainingDetailsActivity
import com.izubcic.gymApp.ui.trainings.views.ProfileView
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), OnItemClickListener, ProfileView {

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getUser()
    }

    private fun initListeners() {
        profileSave.onClick {
            auth.getCurrentUser()?.run {
                val user = UserUpdateProfile(
                    id = this.uid,
                    userDisplayName = profileNameValue.text.toString(),
                    age = profileAgeValue.text.toString().toInt(),
                    weight = profileWeightValue.text.toString(),
                    height = profileHeightValue.text.toString()
                )
                database.saveUserDetails(user) { onItemsReceived() }
            }
        }

        signOutProfile.onClick {
            auth.logTheUserOut()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun getUser() {
        auth.getCurrentUser()?.run {
            database.getUser(this.uid) { onUserFetched(it) }
        }
    }

    // logic functions

    private fun onItemsReceived() {
        this.activity?.applicationContext.toast("Done!")
    }

    private fun onUserFetched(user: User) {
        profileAgeValue.setText(user.age.toString())
        profileNameValue.setText(user.userDisplayName)
        profileHeightValue.setText(user.height)
        profileWeightValue.setText(user.weight)
    }

    private fun onItemDeleted(training: Training) {
        auth.getCurrentUser()?.uid?.run {
            database.onItemDeleted(this, training) {}
        }
    }

    // view functions
    override fun onTrainingClick(training: Training) {
        val bundle = Bundle()
        bundle.putSerializable(TRAINING_KEY, training)
        val intent = Intent(activity, TrainingDetailsActivity::class.java).putExtras(bundle)
        startActivity(intent)
    }

    override fun onDeleteClick(training: Training) {
        onItemDeleted(training)
    }
}