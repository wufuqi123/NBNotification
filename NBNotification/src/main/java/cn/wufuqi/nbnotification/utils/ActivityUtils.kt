package cn.wufuqi.nbnotification.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.util.Log
import java.lang.reflect.InvocationTargetException
import java.util.*


@SuppressLint("BlockedPrivateApi", "StaticFieldLeak")
object ActivityUtils {
    private var application: Application? = null
    private var context: Context? = null

    /** 反射获取Application  */
    @SuppressLint("BlockedPrivateApi", "StaticFieldLeak", "PrivateApi", "DiscouragedPrivateApi")
    fun getApplication(): Application? {
        if (application != null) {
            return application
        }
        try {
            @SuppressLint("PrivateApi") val activityThread =
                Class.forName("android.app.ActivityThread")
            val at = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(at)
                ?: throw NullPointerException("u should init first")
            application = app as Application
            context = application
            return application
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        throw NullPointerException("u should init first")
    }

    /** 获取Context  */
    @SuppressLint("StaticFieldLeak")
    fun getContext(): Context? {
        return context ?: getApplication()
    }

    /** 获取targetSdkVersion  */
    @SuppressLint("StaticFieldLeak")
    fun getTargetSdkVersion(): Int {
        val localPackageManager = getContext()!!.packageManager
        val localApplicationInfo: ApplicationInfo? = try {
            localPackageManager.getApplicationInfo(getContext()!!.packageName, 0)
        } catch (localNameNotFoundException: NameNotFoundException) {
            localNameNotFoundException.printStackTrace()
            return 0
        }
        return localApplicationInfo?.targetSdkVersion ?: 0
    }


    /**
     * 获取前台activity的包名
     * @param context
     * @return
     */
    fun getTopActivityClassName(): String? {
        val mActivityManager =
            getContext()!!.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (mActivityManager.getRunningTasks(1) == null) {
            return null
        }
        val mRunningTask = mActivityManager.getRunningTasks(1)[0] ?: return null
        //String activityName =  mRunningTask.topActivity.getClassName();
        return mRunningTask.topActivity!!.className
    }


    /**
     * 获取前台activity的包名
     * @param context
     * @return
     */
    fun getTopActivity(): Activity {
        val activityList = getActivitiesByReflect()

        val topName = getTopActivityClassName()
        activityList.forEach {
            if (it::class.java.name == topName) {
                return it
            }
        }
        return activityList[activityList.size - 1]
    }


    /**
     * 通过反射获取所有 activity
     */
    fun getActivitiesByReflect(): List<Activity> {
        val list: LinkedList<Activity> = LinkedList()
        var topActivity: Activity? = null
        try {
            val activityThread: Any = this.getActivityThread() ?: return list
            val mActivitiesField = activityThread.javaClass.getDeclaredField("mActivities")
            mActivitiesField.isAccessible = true
            val mActivities = mActivitiesField[activityThread] as? Map<*, *>
                ?: return list
            val var7: Iterator<*> = mActivities.values.iterator()
            while (var7.hasNext()) {
                val activityRecord = var7.next()!!
                val activityClientRecordClass: Class<*> = activityRecord.javaClass
                val activityField = activityClientRecordClass.getDeclaredField("activity")
                activityField.isAccessible = true
                val activity = activityField[activityRecord] as Activity
                if (topActivity == null) {
                    val pausedField = activityClientRecordClass.getDeclaredField("paused")
                    pausedField.isAccessible = true
                    if (!pausedField.getBoolean(activityRecord)) {
                        topActivity = activity
                    } else {
                        list.addFirst(activity)
                    }
                } else {
                    list.addFirst(activity)
                }
            }
        } catch (var13: Exception) {
            Log.e("UtilsActivityLifecycle", "getActivitiesByReflect: " + var13.message)
        }
        if (topActivity != null) {
            list.addFirst(topActivity)
        }
        return list
    }

    private fun getActivityThread(): Any? {
        val activityThread: Any? = this.getActivityThreadInActivityThreadStaticField()
        return activityThread ?: this.getActivityThreadInActivityThreadStaticMethod()
    }


    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    private fun getActivityThreadInActivityThreadStaticField(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val sCurrentActivityThreadField =
                activityThreadClass.getDeclaredField("sCurrentActivityThread")
            sCurrentActivityThreadField.isAccessible = true
            sCurrentActivityThreadField[null as Any?]
        } catch (var3: Exception) {
            Log.e(
                "UtilsActivityLifecycle",
                "getActivityThreadInActivityThreadStaticField: " + var3.message
            )
            null
        }
    }

    @SuppressLint("PrivateApi")
    private fun getActivityThreadInActivityThreadStaticMethod(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            activityThreadClass.getMethod("currentActivityThread").invoke(null as Any?)
        } catch (var2: Exception) {
            Log.e(
                "UtilsActivityLifecycle",
                "getActivityThreadInActivityThreadStaticMethod: " + var2.message
            )
            null
        }
    }

}