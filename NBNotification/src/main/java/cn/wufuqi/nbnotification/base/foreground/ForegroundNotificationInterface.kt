package cn.wufuqi.nbnotification.base.foreground

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.annotation.UiThread
import com.wukonganimation.action.chained.SequenceActionBuild


/**
 * 前台通知
 */
interface ForegroundNotificationInterface {



    /**
     * 是否可使用
     */
    fun isEnable():Boolean

    /**
     * 获取 Activity 对象
     */
    fun getActivity(): Activity?

    /**
     * 前台通知被创建
     */
    @MainThread
    fun onCreate(context: Context)


    /**
     * 前台通知 view 被创建
     */
    @UiThread
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup
    ): View


    /**
     * 前台通知  view 被销毁
     */
    @UiThread
    fun onDestroyView(v: View)


    /**
     * 前台通知被销毁
     */
    @MainThread
    fun onDestroy()


    /**
     * 获取 底部移动时偏移的 Y值
     */
    fun getBottomOffsetMoveY():Float


    /**
     * 指定activity 显示前台通知
     */
    @UiThread
    fun show(activity: Activity)

    /**
     * 创建前台通知显示动画
     */
    @UiThread
    fun buildAnimationShow(time: Long, bottomY: Float): SequenceActionBuild

    /**
     * 移动时的时间
     */
    fun getMoveTime(): Long

    /**
     * 等待时间，
     * 负数为  不会自动隐藏
     */
    fun getWaitTime(): Long

    /**
     * 在顶部activity  显示前台通知
     */
    @UiThread
    fun show()

    /**
     * 隐藏前台通知
     */
    @UiThread
    fun hide()

    /**
     * 创建隐藏动画
     */
    @UiThread
    fun buildAnimationHide(time: Long, topY: Float): SequenceActionBuild

    /**
     * 移动时调用
     */
    @UiThread
    fun onMove(offsetY: Float)

    /**
     * 点击时调用
     */
    @UiThread
    fun onClick()

    /**
     * Touch  事件
     */
    @UiThread
    fun onTouch(event: MotionEvent)


    /**
     * 开始等待
     */
    fun startWait()

    /**
     * 开始等待
     */
    fun startWait(waitTime:Long)

    /**
     * 开始等待
     */
    fun startWait(isWait:Boolean,waitTime:Long)

    /**
     * 结束等待
     */
    fun stopWait()

}