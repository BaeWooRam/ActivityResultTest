package com.example.activityresulttest

import androidx.appcompat.app.AppCompatActivity

interface Permission {
    interface Target{
        fun target(target:AppCompatActivity):Request
    }

    interface Request{
        fun request(permissions:Array<String>):Listener
    }

    interface Listener{
        fun setPermissionListener(permissionListener: PermissionListener):Checker
    }

    interface Checker{
        fun check()
    }

    interface PermissionListener{
        fun onGrantedPermission()
        fun onDenyPermission(denyPermissions: Array<String>)
    }
}