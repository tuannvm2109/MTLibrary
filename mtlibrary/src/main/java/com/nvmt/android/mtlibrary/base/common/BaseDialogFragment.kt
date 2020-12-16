package com.nvmt.android.mtlibrary.base.common

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.nvmt.android.mtlibrary.base.Constant
import com.nvmt.android.mtlibrary.extension.showDialogAlert
import com.nvmt.android.mtlibrary.R
import retrofit2.HttpException

abstract class BaseDialogFragment<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    DialogFragment() {

    lateinit var binding: ViewBinding

    abstract val viewModel: ViewModel

    @get:LayoutRes
    abstract val layoutId: Int

    private var alreadyNavigate: Boolean = false

    private var loadingDialog: Dialog? = null

    private lateinit var mActivity: BaseActivity<*, *>

    private var mLastClickTime = longArrayOf(0, 0, 0, 0, 0)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            val activity: BaseActivity<*, *> = context
            mActivity = activity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0));
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoadingDialog()
        binding.apply {
            setVariable(BR.viewModel, viewModel)
            root.isClickable = true
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
        observeException()
    }

    private fun observeException() {
        viewModel.apply {
            networkNotAvailable.observe(requireActivity(), Observer {
                if (context == null) return@Observer
                it.getContentIfNotHandled()?.let { isOffline ->
                    if (isOffline)
                        showAlert(getString(R.string.message_network_not_available))
                }
            })
            unknownError.observe(requireActivity(), Observer {
                if (context == null) return@Observer
                it.getContentIfNotHandled()?.let {
                    if (Constant.DEBUG_API) {
                        showAlert(it)
                        return@let
                    }

                    if (it.isBlank()) showAlert(getString(R.string.message_unknown_error))
                    else showAlert(it)
                }
            })
            internalServerException.observe(requireActivity(), Observer {
                if (context == null) return@Observer
                it.getContentIfNotHandled()?.let {
                    showAlert(getString(R.string.message_internal_server_erorr))
//                    showAlert(getString(R.string.message_internal_server_erorr))
                }
            })
            socketTimeoutException.observe(requireActivity(), Observer {
                if (context == null) return@Observer
                it.getContentIfNotHandled()?.let {
                    showAlert(getString(R.string.message_socket_timeout_erorr))
                }
            })
            httpException.observe(requireActivity(), Observer { event ->
                if (context == null) return@Observer
                event.getContentIfNotHandled()?.let {
                    handleHttpException(it)
                }
            })
            expireToken.observe(requireActivity(), Observer {
                if (context == null) return@Observer
                it.getContentIfNotHandled()?.let { isExpire ->
                    if (isExpire) showExpireTokenAlert()
                }
            })
            ioException.observe(requireActivity(), Observer {
                if (context == null) return@Observer
                it.getContentIfNotHandled()?.let {
                    showAlert(getString(R.string.message_io_exception_erorr))
//                    showAlert(getString(R.string.message_io_exception_erorr))
                }
            })
            tooManyRequestException.observe(requireActivity(), Observer {
                if (context == null) return@Observer
                it.getContentIfNotHandled()?.let { it ->
                    if (it) showAlert(getString(R.string.message_too_many_request))
                }
            })
        }
    }

    protected fun showAlert(msg: String?, positiveListener: (() -> Unit)? = null) {
        if (activity == null || requireActivity().isFinishing) return
        (requireActivity() as BaseActivity<*, *>).showDialogAlert(msg, positiveListener)
    }

    protected open fun handleHttpException(httpException: HttpException) {
        if (!this::mActivity.isInitialized) return
        mActivity.handleHttpException(httpException)
    }

    private fun showExpireTokenAlert() {
        if (!this::mActivity.isInitialized) return
        mActivity.showExpireTokenAlert()
    }

    open fun openActivityOnTokenExpire() {
        if (!this::mActivity.isInitialized) return
        mActivity.handleExpireToken()
    }

    open fun handleShowLoading(isLoading: Boolean) {
        if (isLoading) showLoading() else hideLoading()
    }

    fun showLoading() {
        if (context == null || loadingDialog?.isShowing == true) return
        loadingDialog?.show()
    }

    fun hideLoading() {
        if (context == null) return
        loadingDialog?.dismiss()
    }

    private fun setLoadingDialog() {
        if (context == null || loadingDialog != null) return
        loadingDialog = Dialog(requireContext(), android.R.style.Theme_Panel)
        val progressBar = ProgressBar(requireContext(), null, android.R.attr.progressBarStyleLarge)
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        val rl = RelativeLayout(requireContext())
        rl.gravity = Gravity.CENTER
        rl.addView(progressBar)

        if (progressBar.parent != null) {
            (progressBar.parent as ViewGroup).removeView(progressBar)
        }
        loadingDialog!!.addContentView(progressBar, params)
//        loadingDialog!!.setCancelable(false)


        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            val isLoading = it.getContentIfNotHandled()
            if (isLoading != null) {
                if (isLoading) showLoading()
                else hideLoading()
            }
        })
    }

    protected fun checkMultiClick(index: Int): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime[index] < 1000) {
            return true
        }
        mLastClickTime[index] = SystemClock.elapsedRealtime()
        return false
    }

    override fun onPause() {
        loadingDialog?.dismiss()
        super.onPause()
    }
}