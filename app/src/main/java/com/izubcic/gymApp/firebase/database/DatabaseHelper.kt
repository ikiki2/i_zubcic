package com.izubcic.gymApp.firebase.database

import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.data.model.User
import com.izubcic.gymApp.data.request.UserUpdateProfile

interface DatabaseHelper {

    fun saveUser(user: User)

    fun getUser(id: String, returningUser: (User) -> Unit)

    fun onItemDeleted(userId: String, training: Training, lambda: () -> Unit)

    fun listenToTrainingList(userId: String, noTrainingsReceived: (List<Training>) -> Unit)

    fun addTraining(userId: String, training: Training, returningUser: () -> Unit)

    fun updateTraining(userId: String, training: Training, returningTraining: () -> Unit)

    fun saveUserDetails(user: UserUpdateProfile, lambda: (UserUpdateProfile) -> Unit)

    fun saveUserWeight(user: UserUpdateProfile, lambda: () -> Unit)
}