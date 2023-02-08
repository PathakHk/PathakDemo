package com.navkar.myapp.activities

import android.os.Bundle

interface DialogCallBacks {
    fun onDismiss(bundle: Bundle)
    fun dialogClick(position: Int, id: Int, value: String?)
}