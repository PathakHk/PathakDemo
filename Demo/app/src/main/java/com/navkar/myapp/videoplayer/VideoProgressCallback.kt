package com.navkar.myapp.videoplayer

interface VideoProgressCallback {

    fun onProgressUpdate(position: Int, duration: Int)
}