package com.example.activityresulttest.result

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity

interface ForResult {
    interface Init<T: Enum<T>>{
        fun init(type:T, launcher: ActivityResultLauncher<Intent>):Launcher<T>
    }

    interface Launcher<T: Enum<T>> {
        fun launch(type: T, intent: Intent)
    }
}