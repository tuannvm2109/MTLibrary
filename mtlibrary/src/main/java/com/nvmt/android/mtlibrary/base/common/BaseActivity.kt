package com.nvmt.android.mtlibrary.base.common

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.MotionEvent
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import com.nvmt.android.mtlibrary.extension.hideKeyboard
import com.nvmt.android.mtlibrary.extension.showDialogAlert
import com.nvmt.android.mtlibrary.extension.toast
import com.karumi.dexter.listener.*
import com.nvmt.android.mtlibrary.R
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException

abstract class BaseActivity<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    AppCompatActivity() {

    lateinit var viewBinding: ViewBinding
    abstract val viewModel: ViewModel

    @get:LayoutRes
    abstract val layoutId: Int


    private var mLastClickTime = longArrayOf(0, 0, 0, 0, 0)

    private var isEnableDoubleTabBackPress = false
    private var doubleBackToExitPressedOnce = false

    var loadingDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = DataBindingUtil.setContentView(this, layoutId)
        viewBinding.setVariable(BR.viewModel, viewModel)

        viewBinding.lifecycleOwner = this
        observeException()
    }


    override fun onResume() {
        super.onResume()
        hideKeyboard()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val v = currentFocus
        if (v != null && (ev?.action == MotionEvent.ACTION_UP || ev?.action == MotionEvent.ACTION_MOVE) &&
            !v.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX.plus(v.left).minus(scrcoords[0])
            val y = ev.rawY.plus(v.top).minus(scrcoords[1])
            if (x < v.left || x > v.right || y < v.top || y > v.bottom)
                hideKeyboard()
        }

        return super.dispatchTouchEvent(ev)
    }

    fun showLoading() {
        if (loadingDialog == null) {
//            loadingDialog = createLoadingDialog()
        }
        loadingDialog?.show()
    }

    fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
        }
    }

    // Handle http Exception =======================================================================
    private fun observeException() {
        viewModel.apply {
            networkNotAvailable.observe(this@BaseActivity, Observer {
                it.getContentIfNotHandled()?.let { isOffline ->
                    if (isOffline)
                        showDialogAlert(getString(R.string.message_network_not_available))
                }
            })
            unknownError.observe(this@BaseActivity, Observer {
                it.getContentIfNotHandled()?.let {
                    showDialogAlert(getString(R.string.message_unknown_error))
                }
            })
            internalServerException.observe(this@BaseActivity, Observer {
                it.getContentIfNotHandled()?.let {
                    showDialogAlert(getString(R.string.message_internal_server_erorr))
                }
            })
            socketTimeoutException.observe(this@BaseActivity, Observer {
                it.getContentIfNotHandled()?.let {
                    showDialogAlert(getString(R.string.message_socket_timeout_erorr))
                }
            })
            httpException.observe(this@BaseActivity, Observer { event ->
                event.getContentIfNotHandled()?.let {
                    handleHttpException(it)
                }
            })
            expireToken.observe(this@BaseActivity, Observer {
                it.getContentIfNotHandled()?.let { isExpire ->
                    if (isExpire) showExpireTokenAlert()
                }
            })
        }
    }

    open fun handleHttpException(httpException: HttpException) {
        httpException.response()?.errorBody()?.let {
            showDialogAlert(getErrorMessage(it))
        }
    }

    protected fun getErrorMessage(responseBody: ResponseBody): String? {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("msg")
        } catch (e: Exception) {
            e.message
        }
    }


    // Handle expireToken ==========================================================================
    open fun showExpireTokenAlert() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.message_expire_token))
            .setPositiveButton(getString(R.string.txt_login)) { dialog, _ ->
                dialog.dismiss()
                handleExpireToken()
            }
            .setCancelable(false)
            .show()
    }
    abstract fun handleExpireToken()

    // Handle backPress and Double tap to Exit =====================================================
    override fun onBackPressed() {
        hideKeyboard()
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {

            if (isEnableDoubleTabBackPress)
                doubleTapToExit()
            else super.onBackPressed()
        }
    }

    private fun doubleTapToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true

        toast(getString(R.string.message_press_again_to_exit))
        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    protected fun enableDoubleTabToExit(enable: Boolean) {
        this.isEnableDoubleTabBackPress = enable
    }

    // checkMultiClick =============================================================================
    protected fun checkMultiClick(index: Int): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime[index] < 1000) {
            return true
        }
        mLastClickTime[index] = SystemClock.elapsedRealtime()
        return false
    }
}