package cn.wufuqi.nbnotification.foreground

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.contains
import androidx.core.view.forEach
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import cn.wufuqi.nbnotification.R
import cn.wufuqi.nbnotification.utils.ActivityUtils
import com.wukonganimation.action.ActionManager
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.stopAction
import com.wukonganimation.tween.Easing
import kotlin.math.abs
import kotlin.math.min

open class ForegroundNotification : ForegroundNotificationInterface {
    private var mActivity: Activity? = null
    private var mRootView: ViewGroup? = null
    private var mContentViewParent: ViewGroup? = null
    private var mStatusBarColorView: View? = null
    private var mActivityStatusBarColor: Int = -1

    var mContentView: View? = null

    var currStatusBarHeight = 0

    var isShow: Boolean = false
        private set

    var isAnimationRunning = false
        private set

    var time = 500L

    var offsetMoveY = dp2px(20f)

    var easing = Easing.outExpo()

    private var lastDownY = 0f
    private var isDown = false

    private var currMoveOffset = 0f
    private var maxMoveOffset = 5f

    private lateinit var mLayoutInflater: LayoutInflater


    fun getActivity(): Activity? {
        return mActivity
    }

    @SuppressLint("InflateParams")
    @CallSuper
    override fun onCreate(context: Context) {
        mLayoutInflater = LayoutInflater.from(context)
        mRootView = mLayoutInflater.inflate(
            R.layout.nbnotification_notification_container,
            null
        ) as ViewGroup
        mContentViewParent = mRootView?.findViewById(R.id.cv_notification)
        mStatusBarColorView = mRootView?.findViewById(R.id.status_bar_color)
        val outRect = Rect()
        (mActivity!!.window.decorView as ViewGroup).getChildAt(0)
            .getWindowVisibleDisplayFrame(outRect)
        currStatusBarHeight = outRect.top
        val lp = mStatusBarColorView!!.layoutParams
        lp.height = currStatusBarHeight
        mStatusBarColorView!!.layoutParams = lp
        mStatusBarColorView!!.setBackgroundColor(mActivityStatusBarColor)

        if (currStatusBarHeight == 0) {
            currStatusBarHeight = dp2px(44f).toInt()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return View(container.context)
    }

    @CallSuper
    override fun onDestroyView(v: View) {

    }

    @CallSuper
    override fun onDestroy() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @CallSuper
    override fun show(activity: Activity) {
        if (isShow) {
            Log.w(
                this::class.java.name.toString(),
                "当前已经有 ForegroundNotification 正在显示，无需调用show方法，已忽略！"
            )
            return
        }
        isShow = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActivityStatusBarColor = activity.window.statusBarColor
        }
        mActivity = activity
        activity.window.decorView.post {
            onCreate(activity)
            mContentView = onCreateView(mLayoutInflater, mContentViewParent!!)
            getGlobalVisibleRect(mGlobalVisibleRect)
            mContentViewParent!!.visibility = View.INVISIBLE
            (activity.window.decorView as ViewGroup).addView(mRootView)
            mContentView!!.post {
                mContentView!!.y = -mContentView!!.measuredHeight.toFloat()
                mContentViewParent!!.visibility = View.VISIBLE
                animationShow()
                mContentView!!.post {
                    mContentView!!.setOnTouchListener { _, event ->
                        onTouch(event)
                        return@setOnTouchListener true
                    }
                }

            }

        }

    }

    @CallSuper
    override fun show() {
        show(ActivityUtils.getTopActivity())
    }


    @CallSuper
    override fun hide() {
        if (!isShow) {
            return
        }
        animationHide {
            onDestroyView(mContentView!!)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mActivity!!.window.statusBarColor = mActivityStatusBarColor
            }
            (mActivity!!.window.decorView as ViewGroup).removeView(mRootView)
            onDestroy()
            isShow = false
        }
    }


    private fun getBottomY(): Float = currStatusBarHeight + offsetMoveY


    private fun animationShow(callback: (() -> Unit)? = null) {
        ActionManager.init(mActivity!!.application)
        isAnimationRunning = true
        mContentView!!.createAction()
            .moveTo(time, Float.NaN, getBottomY(), easing)
            .callFunc {
                isAnimationRunning = false
                callback?.invoke()
            }.start()
    }

    private fun animationHide(callback: (() -> Unit)? = null) {
        isAnimationRunning = true
        val moveTime =
            (mContentView!!.measuredHeight.toFloat() + mContentView!!.y) / (mContentView!!.measuredHeight.toFloat() + getBottomY()) * time
        mContentView!!.createAction()
            .moveTo(moveTime.toLong(), Float.NaN, -mContentView!!.measuredHeight.toFloat(), easing)
            .callFunc {
                isAnimationRunning = false
                callback?.invoke()
            }
            .start()
    }


    fun onTouch(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mContentView!!.stopAction()
                isAnimationRunning = false
                lastDownY = event.rawY
                getGlobalVisibleRect(mGlobalVisibleRect)
                isDown = event.x >= mGlobalVisibleRect.left && event.x <= mGlobalVisibleRect.right
                currMoveOffset = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDown) {
                    return
                }
                val offsetY = event.rawY - lastDownY
                lastDownY = event.rawY
                currMoveOffset += abs(offsetY)
                if (currMoveOffset >= maxMoveOffset) {
                    onMove(offsetY)
                }

            }
            MotionEvent.ACTION_UP -> {
                if (!isDown) {
                    return
                }

                if (currMoveOffset >= maxMoveOffset) {
                    hide()
                } else {
                    onClick()
                }

                isDown = false
            }
        }
    }

    fun onMove(offsetY: Float) {
        mContentView!!.y = min(getBottomY(), mContentView!!.y + offsetY)
    }

    fun onClick() {
        hide()
    }


    private var mGlobalVisibleRect = Rect()


    private fun getGlobalVisibleRect(gvr: Rect) {
        var r = Rect()
        r.left = mContentView!!.left
        r.right = mContentView!!.right
        r.top = mContentView!!.top
        r.bottom = mContentView!!.bottom

//        Log.e("wufuqi---", "r... ${r}")
        if (mContentView is ViewGroup) {
            var maxR: Rect? = null
            (mContentView as ViewGroup)!!.forEach {
                if (maxR == null) {
                    maxR = Rect()
                    maxR!!.left = it.left
                    maxR!!.right = it.right
                } else {
                    if (it.left < maxR!!.left && it.left >= 0) {
                        maxR!!.left = it.left
                    }
                    if (it.right > maxR!!.right && it.right <= r.right) {
                        maxR!!.right = it.right
                    }
                }
            }
            if (maxR != null) {
                r = maxR!!
            }
        }
//        Log.e("wufuqi---", "r ${r}")
        gvr.left = r.left
        gvr.right = r.right
        gvr.top = r.top
        gvr.bottom = r.bottom
    }


    private fun dp2px(dpValue: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            Resources.getSystem().displayMetrics
        )
    }
}