package com.exemple.scrabble

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Title: FullscreenFragment
 *
 * Author: Gabriel Caetano Araújo
 *
 * Brief:
 * Displays a welcome screen when starting the application.
 *
 * Description:
 * When starting the application, it displays a welcome screen that remains for
 * 1 second before starting the main activity of the application. This screen
 * hides the notification bar to simulate a full screen.
 */
class FullscreenFragment : AppCompatActivity() {
    // 1 second timeout for displaying the welcome screen.
    private val timeOut: Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // hides the notification bar while the welcome screen is running
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_fullscreen)

        // starts the main activity after the waiting time expires
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))

            // closes the welcome screen activity
            finish()
        }, timeOut)
    }
}