package com.example.activityresulttest

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.activityresulttest.permission.Permission
import com.example.activityresulttest.permission.PermissionChecker

class PermissionTestActivity : AppCompatActivity() {
    private var listener: Permission.PermissionListener? = null
    private val permissionChecker = PermissionChecker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        listener = object : Permission.PermissionListener {
            override fun onGrantedPermission() {
                Toast.makeText(this@PermissionTestActivity, "퍼미션 허가 완료", Toast.LENGTH_SHORT).show()
            }

            override fun onDenyPermission(denyPermissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "퍼미션 허가 실패", Toast.LENGTH_SHORT).show()
            }
        }

        permissionChecker
            .initPermissionLauncher(this@PermissionTestActivity)
            .target(this@PermissionTestActivity)
            .setPermissionListener(listener!!)

        findViewById<Button>(R.id.btnRequestPermission).setOnClickListener {
            permissionChecker
                .request(arrayOf(Manifest.permission.CAMERA))
                .check()
        }

        findViewById<Button>(R.id.btnRequestMultiPermissions).setOnClickListener {
            permissionChecker
                .setMultiPermissionShowListener(object : Permission.ShowListener<Array<String>>{
                    override fun showRequestPermissionRationale(
                        permissions: Array<String>,
                        launcher: ActivityResultLauncher<Array<String>>
                    ) {
                        Toast.makeText(this@PermissionTestActivity, "(대충 권한 무조건 해야한다는 말)", Toast.LENGTH_SHORT).show()
                        launcher.launch(permissions)
                    }
                })
                .request(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                .check()
        }
    }
}