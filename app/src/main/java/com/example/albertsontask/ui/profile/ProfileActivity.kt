package com.example.albertsontask.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.albertsontask.common.setLightStatusBar
import com.example.albertsontask.data.model.profile.Profile
import com.example.albertsontask.data.model.user.Result
import com.example.albertsontask.databinding.ActivityProfileBinding
import com.example.albertsontask.ui.profile.composablescreen.ProfileScreen

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setLightStatusBar(false)
        initIntent()
        initListener()
    }

    private fun initIntent() {
        val bundle = intent.getBundleExtra("BUNDLE")
        bundle?.let {
            binding.content.composeView.setContent {
                val user: Profile? = bundle.getParcelable("user")
                ProfileScreen(user)
            }
        }
    }

    private fun initListener() {
        binding.toolbar.setNavigationOnClickListener {
            super.onBackPressed()
        }
    }
}