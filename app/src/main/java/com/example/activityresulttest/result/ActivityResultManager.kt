package com.example.activityresulttest.result

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.HashMap

class ActivityResultManager<T : Enum<T>> : ForResult.Init<T>, ForResult.Launcher<T> {
    private val debugTag = javaClass.simpleName
    private var launcherHashMap: HashMap<String, ActivityResultLauncher<Intent>> = hashMapOf()

    override fun init(type: T, launcher: ActivityResultLauncher<Intent>): ForResult.Launcher<T> {
        launcherHashMap[type.name] = launcher
        return this@ActivityResultManager
    }

    override fun launch(type: T, intent: Intent) {
        val launcher = launcherHashMap[type.name]

        if (launcher == null) {
            Log.e(debugTag, "launcher is null", NullPointerException())
            return
        }

        launcher.launch(intent)
    }

    companion object {

        /**
         * Activity Result 관련 Launcher 생성
         */
        fun getActivityResultLauncher(
            target: AppCompatActivity,
            callback: ActivityResultCallback<ActivityResult>
        ): ActivityResultLauncher<Intent> {
            return target.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                callback
            )
        }
    }

}