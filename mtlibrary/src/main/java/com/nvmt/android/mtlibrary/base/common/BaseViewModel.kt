package com.nvmt.android.mtlibrary.base.common

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvmt.android.mtlibrary.di.scheduler.AndroidSchedulerProvider
import com.esmac.android.appcas.di.scheduler.SchedulerProvider
import com.nvmt.android.mtlibrary.base.MTConstant
import com.nvmt.android.mtlibrary.base.EventOnce
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    val schedulerProvider: SchedulerProvider = AndroidSchedulerProvider()
    val isLoading = MutableLiveData<EventOnce<Boolean>>()
    val expireToken = MutableLiveData(EventOnce(false))
    val httpException = MutableLiveData<EventOnce<HttpException>>()
    val networkNotAvailable = MutableLiveData<EventOnce<Boolean>>()
    val internalServerException = MutableLiveData<EventOnce<String>>()
    val socketTimeoutException = MutableLiveData<EventOnce<String>>()
    val ioException = MutableLiveData<EventOnce<String>>()
    val unknownError = MutableLiveData<EventOnce<String>>()
    val tooManyRequestException = MutableLiveData<EventOnce<Boolean>>()
    val debugError = MutableLiveData<EventOnce<String>>()

    open fun showLoading() {
        isLoading.value = EventOnce(true)
    }

    open fun hideLoading() {
        isLoading.value = EventOnce(false)
    }

    fun showLoadingDelay() {
        //Delay cho trường hợp shareViewModel, lỗi frament 1 sẽ nhận isloading thay vì fragment2
        Handler().postDelayed(
            {
                showLoading()
            },
            100 // value in milliseconds
        )
    }

    fun hideLoadingDelay() {
        Handler().postDelayed(
            {
                hideLoading()
            },
            100 // value in milliseconds
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    open suspend fun onLoadFail(throwable: Throwable) {
        onError(throwable)
    }

    abstract fun isDebug(): Boolean

    open fun onError(throwable: Throwable?) {
        hideLoading()
        Log.d("asdfErrorOkHttp", "${throwable?.message}")
        if (isDebug()) {
            try {
                if (throwable is HttpException) {
                    onUnknownError(
                        JSONObject(
                            (throwable).response()?.errorBody()?.string()
                        ).toString()
                    )
                } else debugError.value = EventOnce(JSONObject(throwable?.message ?: "").toString())
            } catch (e: Exception) {
                onUnknownError("")
            }
            return
        }

        when (throwable) {
            is UnknownHostException -> {
                networkNotAvailable()
            }
            is HttpException -> {
                when (throwable.code()) {
                    401, 403 -> onExpireToken()
                    422 -> onHttpException(throwable)
                    429 -> onTooManyRequestExeption()
                    500 -> {
                        throwable.response()?.errorBody()
                            ?.let { internalServerException(getErrorMessage(it)) }
                    }
                    else -> onHttpException(throwable) /*400, 404, 502*/
                }
            }
            is SocketTimeoutException -> {
                onSocketTimeout()
            }
            is IOException -> {
                onIOException()
            }
            else -> {
                try {
                    val jsonObject = JSONObject(throwable?.message)
                    val error = jsonObject.getString("msg")
                    val code = jsonObject.getString("status")
                    when (code) {
                        "401", "403" -> onExpireToken()
                        "429" -> onTooManyRequestExeption()
                        "500" -> internalServerException(error)
                        else -> onUnknownError(error)
                    }
                } catch (e: Exception) {
                    onUnknownError("")
                }
            }
        }
    }

    private fun onHttpException(throwable: HttpException) {
        httpException.value = EventOnce(throwable)
    }

    private fun onTooManyRequestExeption() {
        tooManyRequestException.value = EventOnce(true)
    }

    fun onExpireToken() {
        expireToken.value = EventOnce(true)
    }

    private fun networkNotAvailable() {
        networkNotAvailable.value = EventOnce(true)
    }

    private fun internalServerException(error: String) {
        internalServerException.value = EventOnce(error)
    }

    private fun onUnknownError(error: String?) {
        error?.let {
            unknownError.value = EventOnce(error)
        }
    }

    private fun onSocketTimeout() {
        socketTimeoutException.value =
            EventOnce("Socket Timeout Exception")
    }

    private fun onIOException() {
        ioException.value = EventOnce("IO Exception")
    }

    fun getErrorMessage(responseBody: ResponseBody): String {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            jsonObject.getString("msg")
        } catch (e: Exception) {
            e.message ?: "Server Error"
        }
    }
}