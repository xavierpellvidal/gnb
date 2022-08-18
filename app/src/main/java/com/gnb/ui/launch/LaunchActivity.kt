package com.gnb.ui.launch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.gnb.R
import com.gnb.databinding.ActivityLaunchBinding
import com.gnb.domain.entity.ui.ProductUI
import com.gnb.ui.main.MainActivity
import com.gnb.util.Constants
import java.util.stream.Collectors

class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initApp()
    }

    /**
     * Waits SPLASH_DELAY and starts MainActivity.
     */
    private fun initApp() {
        Handler(Looper.getMainLooper()).postDelayed({
            //Navigate to main activity
            startActivity(Intent(this@LaunchActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.slide_enter_right_left, R.anim.slide_exit_right_left)
            finish()
        }, Constants.SPLASH_DELAY.toLong())
    }

}