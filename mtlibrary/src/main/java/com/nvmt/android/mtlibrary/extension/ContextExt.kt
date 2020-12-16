package com.nvmt.android.mtlibrary.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.nvmt.android.mtlibrary.R

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
}

fun Context.toastError(msg: String) {
    val toast = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        val view = toast.view
        view!!.setBackgroundResource(R.drawable.custom_error_background)
        val text = view!!.findViewById(android.R.id.message) as TextView
        text.setTextColor(Color.WHITE)
    }
//    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}

fun Context.logTest(message: String) {
    Log.d("zxcvasdf", message)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.openWebsite(url: String?) {
    try {
        val uris = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uris)
        val b = Bundle()
        b.putBoolean("new_window", true)
        intent.putExtras(b)
        this.startActivity(intent)
    } catch (ex: Exception) {
        ex.message?.let { toast(it) }
    }
}

interface IDialogConfirm {
    fun onPositiveClick()
    fun onNegativeClick()
}
