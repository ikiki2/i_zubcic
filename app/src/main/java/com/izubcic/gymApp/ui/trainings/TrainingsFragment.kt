package com.izubcic.gymApp.ui.trainings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.izubcic.gymApp.App
import com.izubcic.gymApp.R
import com.izubcic.gymApp.commons.constants.TRAINING_KEY
import com.izubcic.gymApp.commons.extensions.hide
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.commons.extensions.show
import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.firebase.authentication.AuthenticationHelperImpl
import com.izubcic.gymApp.firebase.database.DatabaseHelperImpl
import com.izubcic.gymApp.preferences.PreferencesHelperImpl
import com.izubcic.gymApp.ui.listeners.OnItemClickListener
import com.izubcic.gymApp.ui.signIn.SignInActivity
import com.izubcic.gymApp.ui.training_details.TrainingDetailsActivity
import com.izubcic.gymApp.ui.trainings.adapter.TrainingsAdapter
import com.izubcic.gymApp.ui.trainings.views.TrainingListView
import kotlinx.android.synthetic.main.fragment_trainings.*

class TrainingsFragment : Fragment(), OnItemClickListener, TrainingListView {


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

    private val adapter by lazy { TrainingsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trainings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initRecyclerView()
        getTrainings()
    }

    private fun initListeners() {
        signOutTrainings.onClick {
            auth.logTheUserOut()
            val intent = Intent(activity, SignInActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun initRecyclerView() {
        recyclerViewTopRated.adapter = adapter
        val lm = LinearLayoutManager(activity)
        recyclerViewTopRated.layoutManager = lm
    }


    // logic functions
    override fun onTrainingClick(training: Training) {
        val bundle = Bundle()
        bundle.putSerializable(TRAINING_KEY, training)
        val intent = Intent(activity, TrainingDetailsActivity::class.java).putExtras(bundle)
        startActivity(intent)
    }

    override fun onDeleteClick(training: Training) {
        this.onDeleteTapped(training)
    }

    private fun getTrainings(): Unit = run {
        auth.getCurrentUserId()?.run {
            database.listenToTrainingList(this) { onItemsReceived(it) }
        }
    }

    private fun onItemsReceived(list: List<Training>) {
        if (list.isEmpty()) this.showMessageEmptyList() else this.hideMessageEmptyList()
        this.setTrainings(list)
    }

    private fun onDeleteTapped(training: Training) {
        auth.getCurrentUser()?.uid?.run {
            database.onItemDeleted(auth.getCurrentUser()?.uid.toString(), training) {}
        }
    }

    // view functions
    override fun addTrainings(trainings: List<Training>) = adapter.addItems(trainings)

    override fun setTrainings(trainings: List<Training>) = adapter.setItems(trainings)

    override fun showMessageEmptyList() = noTrainings.show()

    override fun hideMessageEmptyList() = noTrainings.hide()
}

