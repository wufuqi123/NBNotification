package cn.wufuqi.nbnotification.foreground

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.UiThread


/**
 * 前台通知
 */
interface ForegroundNotificationInterface {

    @MainThread
    fun onCreate(context: Context)


    @UiThread
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View


    @UiThread
    fun onDestroyView(v: View)

    @MainThread
    fun onDestroy()

    @UiThread
    fun show(activity: Activity)

    @UiThread
    fun hide()

}