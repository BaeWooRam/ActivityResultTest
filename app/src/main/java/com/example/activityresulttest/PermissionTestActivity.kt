package com.example.activityresulttest

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PermissionTestActivity : AppCompatActivity() {
    private var listener:Permission.PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        val permissionChecker = PermissionChecker().initPermissionLauncher(this@PermissionTestActivity)

        listener = object : Permission.PermissionListener {
            override fun onGrantedPermission() {
                Toast.makeText(this@PermissionTestActivity, "퍼미션 허가 완료", Toast.LENGTH_SHORT).show()
            }

            override fun onDenyPermission(denyPermissions: Array<String>) {
                Toast.makeText(this@PermissionTestActivity, "퍼미션 허가 실패", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnRequestPermission).setOnClickListener {
            permissionChecker
                .target(this@PermissionTestActivity)
                .request(arrayOf(Manifest.permission.CAMERA))
                .setPermissionListener(listener!!)
                .check()
        }

        findViewById<Button>(R.id.btnRequestMultiPermissions).setOnClickListener {
            permissionChecker
                .target(this@PermissionTestActivity)
                .request(arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ))
                .setPermissionListener(listener!!)
                .check()
        }
    }
}