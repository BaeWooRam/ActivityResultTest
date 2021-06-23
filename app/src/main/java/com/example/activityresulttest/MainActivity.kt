package com.example.activityresulttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private val debugTag: String = javaClass.simpleName
    private val requestActivity:ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        Log.d(debugTag, "resultCode = ${it.resultCode}, data = ${it.data?.data ?: "None"}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnGoToActivity).setOnClickListener {
            requestActivity.launch(Intent(this@MainActivity, RegisterForeResultActivity::class.java))
        }
    }

    companion object{
        const val RESULT_CODE = 999
    }
}