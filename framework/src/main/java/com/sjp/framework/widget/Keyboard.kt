package com.sjp.framework.widget

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object Keyboard {
    fun isShowKeyboard(context: Context): Boolean {
        return (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).isAcceptingText
    }

    fun showKeyboard(view: View?) {
        if (view != null) {
            view.requestFocus()
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
            if (view is EditText) {
                view.isCursorVisible = true
            }
        }
    }

    fun hideKeyboard(view: View?) {
        if (view != null) {
            view.clearFocus()
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            if (view is EditText) {
                view.isCursorVisible = false
            }
        }
    }

    fun showSafeKeyboard(view: View?) {
        if (view != null) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.postDelayed(Runnable { showKeyboard(view) }, 300)
        }
    }
}
