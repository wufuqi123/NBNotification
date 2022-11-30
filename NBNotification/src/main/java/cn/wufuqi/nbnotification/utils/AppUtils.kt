package cn.wufuqi.nbnotification.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager

object AppUtils {

    fun getAppIcon(): Int {
        var icon = -1
        try {
            val packageManager = ActivityUtils.getContext()?.packageManager
            val packageInfo = packageManager?.getPackageInfo(
                ActivityUtils.getContext()?.packageName ?: "", 0
            )
            icon = packageInfo?.applicationInfo?.icon ?: -1
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return icon
    }


    /**
     * 获取app的名称
     * @param context
     * @return
     */
    fun getAppName(): String {
        var appName = ""
        try {
            val packageManager = ActivityUtils.getContext()?.packageManager
            val packageInfo = packageManager?.getPackageInfo(
                ActivityUtils.getContext()?.packageName ?: "", 0
            )
            val labelRes = packageInfo?.applicationInfo?.labelRes ?: -1
            appName = ActivityUtils.getContext()?.resources?.getString(labelRes) ?: ""
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return appName
    }

    /**
     * 获取app的名称
     * @param context
     * @return
     */
    fun getPackageName(): String {
        return ActivityUtils.getContext()?.packageName ?: ""
    }

    /**
     * 用于判断那个应用是处于前台的
     */
    private fun getForegroundApp(): String? {

        val activityManager = ActivityUtils.getContext()!!
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        if (runningAppProcesses.isNullOrEmpty()) {
            return null
        }
        runningAppProcesses.forEach {
            if (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                return it.processName
            }
        }

        return null
    }

    fun isRunningForeground(): Boolean {
        return getForegroundApp() == getPackageName()
    }
}