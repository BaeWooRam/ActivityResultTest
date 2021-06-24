package com.example.activityresulttest

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.lang.NullPointerException

class PermissionChecker : Permission.Target, Permission.Listener, Permission.Request {
    private val debugTag: String = javaClass.simpleName
    private var target: AppCompatActivity? = null

    //Event
    private var permissionListener: Permission.PermissionListener? = null
    private var singlePermissionShowListener: Permission.ShowListener<String>? = null
    private var multiPermissionShowListener: Permission.ShowListener<Array<String>>? = null

    private var permissions: Array<String>? = null

    //Launch
    private var launchPermission: ActivityResultLauncher<String>? = null
    private var launchMultiplePermissions: ActivityResultLauncher<Array<String>>? = null

    /**
     * 초기화
     */
    fun initPermissionLauncher(target: AppCompatActivity):PermissionChecker{
        //init single permission launcher
        launchPermission = target!!.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Log.d(debugTag, "lunchPermission() result = $it")
            if (it)
                permissionListener!!.onGrantedPermission()
            else
                permissionListener!!.onDenyPermission(permissions!!)
        }


        //init multi permission launcher
        launchMultiplePermissions = target!!.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            var denyPermissions: ArrayList<String> = ArrayList()

            for (entry in map.entries) {
                Log.d(
                    debugTag,
                    "lunchMultiplePermissions() result = ${entry.key} = ${entry.value}"
                )
                if (!entry.value)
                    denyPermissions.add(entry.key)
            }

            if (denyPermissions.isEmpty())
                permissionListener!!.onGrantedPermission()
            else
                permissionListener!!.onDenyPermission(denyPermissions.toTypedArray())
        }

        return this@PermissionChecker
    }

    override fun target(target: AppCompatActivity): Permission.Listener {
        this@PermissionChecker.target = target
        return this@PermissionChecker
    }

    override fun setPermissionListener(permissionListener: Permission.PermissionListener): Permission.Request {
        this@PermissionChecker.permissionListener = permissionListener
        return this@PermissionChecker
    }

    override fun setSinglePermissionShowListener(showListener: Permission.ShowListener<String>): Permission.Request {
        this@PermissionChecker.singlePermissionShowListener = showListener
        return this@PermissionChecker
    }

    override fun setMultiPermissionShowListener(showListener: Permission.ShowListener<Array<String>>): Permission.Request {
        this@PermissionChecker.multiPermissionShowListener = showListener
        return this@PermissionChecker
    }

    override fun request(permissions: Array<String>): Permission.Checker {
        this@PermissionChecker.permissions = permissions
        return Checker()
    }

    inner class Checker : Permission.Checker {
        override fun check() {
            //검증 과정
            if(launchMultiplePermissions == null && launchPermission == null){
                Log.e(debugTag, "Launch Not Init", NullPointerException())
                return
            }

            if (target == null) {
                Log.e(debugTag, "Target is Null", NullPointerException())
                return
            }

            if (permissions == null) {
                Log.e(debugTag, "Require Permission", NullPointerException())
                return
            }

            if(permissions!!.isEmpty()){
                Log.e(debugTag, "Permission is Empty", NullPointerException())
                return
            }

            if (permissionListener == null) {
                Log.e(debugTag, "Require PermissionListener", NullPointerException())
                return
            }


            when (permissions!!.size) {
                1 -> singleExecute()
                else -> multiExecute()
            }
        }

        private fun singleExecute() {
            val requestPermissions = permissions!![0]

            when {
                //퍼미션 승락
                ContextCompat.checkSelfPermission(
                    target!!,
                    requestPermissions
                ) == PackageManager.PERMISSION_GRANTED -> {
                    Log.d(debugTag, "singleExecute() requestPermission = $requestPermissions")

                    permissionListener!!.onGrantedPermission()
                }

                //퍼미션 거절 시 UI 보여주기
                target!!.shouldShowRequestPermissionRationale(requestPermissions) -> {
                    if(singlePermissionShowListener != null)
                        singlePermissionShowListener?.showRequestPermissionRationale(requestPermissions, launchPermission!!)
                    else
                        launchPermission?.launch(requestPermissions)
                }

                else -> {
                    launchPermission?.launch(requestPermissions)
                }
            }
        }

        private fun multiExecute() {
            val requestPermission = ArrayList<String>()

            //퍼미션 허가 체크
            for (permission in permissions!!) {
                if (ContextCompat.checkSelfPermission(
                        target!!,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission.add(permission)
                }
            }

            Log.d(debugTag, "multiExecute() requestPermission = $requestPermission")

            when {
                //퍼미션 허가
                requestPermission.isEmpty() -> permissionListener!!.onGrantedPermission()

                //퍼미션 거절 시 UI 보여주기
                target!!.shouldShowRequestPermissionRationale(requestPermission[0]) -> {
                    if(multiPermissionShowListener != null)
                        multiPermissionShowListener?.showRequestPermissionRationale(permissions!!, launchMultiplePermissions!!)
                    else
                        launchMultiplePermissions?.launch(permissions)
                }

                //퍼미션 비허가
                else -> {
                    launchMultiplePermissions?.launch(permissions)
                }
            }
        }
    }
}