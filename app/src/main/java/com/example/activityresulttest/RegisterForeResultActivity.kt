package com.example.activityresulttest

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.activityresulttest.result.MainActivity

class RegisterForeResultActivity : AppCompatActivity() {
    private val debugTag: String = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setResult(MainActivity.RESULT_CODE, Intent().apply { data = Uri.parse("Hello!") })
        finish()
    }
}