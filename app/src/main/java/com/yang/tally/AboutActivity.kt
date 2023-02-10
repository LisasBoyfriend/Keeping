package com.yang.tally

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yang.tally.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    fun onClick(view: View?) {
        finish()
    }
}