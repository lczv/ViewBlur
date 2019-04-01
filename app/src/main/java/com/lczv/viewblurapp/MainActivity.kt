package com.lczv.viewblurapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lczv.viewblur.setBlurredBackground
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.setBlurredBackground(25f)
    }
}
