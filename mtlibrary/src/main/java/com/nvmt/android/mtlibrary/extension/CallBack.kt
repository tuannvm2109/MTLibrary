package com.nvmt.android.mtlibrary.extension

//
//fun<T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
//    val callBackKt = CallBackKt<T>()
//    callback.invoke(callBackKt)
//    this.enqueue(callBackKt)
//}
//
//class CallBackKt<T> : Callback<T> {
//    var onResponse: ((Response<T>) -> Unit)? = null
//    var onFailure: ((t: Throwable) -> Unit)? = null
////
////    var success: ((T) -> Unit)? = null
////    var fail: ((t: String?) -> Unit)? = null
//
//    override fun onFailure(call: Call<T>, t: Throwable) {
//        onFailure?.invoke(t)
////        fail?.invoke(t.message)
//    }
//
//    override fun onResponse(call: Call<T>, response: Response<T>) {
//        onResponse?.invoke(response)
//
////        if (response.body() == null) {
////            fail?.invoke("")
////            return
////        }
////        this.success?.invoke(response.body()!!)
//    }
//
//}

//
//fun<T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
//    val callBackKt = CallBackKt<T>()
//    callback.invoke(callBackKt)
//    this.enqueue(callBackKt)
//}
//
//class CallBackKt<T>: Callback<T> {
//
//    var onResponse: ((Response<T>) -> Unit)? = null
//    var onFailure: ((t: Throwable) -> Unit)? = null
//
//    override fun onFailure(call: Call<T>, t: Throwable) {
//        onFailure?.invoke(t)
//    }
//
//    override fun onResponse(call: Call<T>, response: Response<T>) {
//        onResponse?.invoke(response)
//    }
//
//}