package com.izubcic.gymApp.ui.trainings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.izubcic.gymApp.R
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.ui.training_add.AddTraningActivity
import com.izubcic.gymApp.ui.trainings.pager.CustomPagerAdapter
import kotlinx.android.synthetic.main.activity_gym.*

class GymsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gym)
        initAdapter()
        initListeners()
    }

    private fun initAdapter() {
        val navigationPagerAdapter = CustomPagerAdapter(supportFragmentManager)
        navigationPagerAdapter.addFragment(TrainingsFragment())
        navigationPagerAdapter.addFragment(ProfileFragment())

        viewPager.adapter = navigationPagerAdapter
        viewPager.offscreenPageLimit = 2
    }

    private fun initListeners() {
        addNewTraining.onClick {
            val intent = Intent(this, AddTraningActivity::class.java)
            startActivity(intent)
        }

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_training_list -> viewPager.currentItem = 0
                R.id.action_profile -> viewPager.currentItem = 1
            }
            true
        }
    }
}