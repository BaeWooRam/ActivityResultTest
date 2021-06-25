package com.example.activityresulttest.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.activityresulttest.R
import com.example.activityresulttest.RegisterForeResultActivity

class MainActivity : AppCompatActivity() {
    private val debugTag: String = javaClass.simpleName
    private val pageMoveManager: ActivityResultManager<MainPage> = ActivityResultManager<MainPage>().apply {
        init(MainPage.RegisterForResult, ActivityResultManager.getActivityResultLauncher(this@MainActivity) {
            Log.d(debugTag, "resultCode = ${it.resultCode}, data = ${it.data}")
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnGoToActivity).setOnClickListener {
            pageMoveManager.launch(MainPage.RegisterForResult, Intent(this@MainActivity, RegisterForeResultActivity::class.java))
        }
    }

    companion object{
        const val RESULT_CODE = 999
    }
}