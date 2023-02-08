package com.navkar.myapp.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.navkar.myapp.R
import com.navkar.myapp.databinding.ActivityMediaBinding
import com.navkar.myapp.videoplayer.BetterVideoPlayer
import com.navkar.myapp.videoplayer.VideoCallback
import io.paperdb.Paper


class MediaActivity : AppCompatActivity() {
    lateinit var mBinder: ActivityMediaBinding
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_media)
        mContext = this

        /*  var mediaController=MediaController(this@MediaActivity)
          mediaController.setAnchorView(mBinder.videoView)
          mBinder.videoView.setMediaController(mediaController)

          val uri: Uri = Uri.parse("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4")
          mBinder.videoView.setVideoURI(uri)
          mBinder.videoView.requestFocus()
          mBinder.videoView.start()*/

        /* mBinder.exoPlayerVideoView.setResizeMode(EnumResizeMode.FIT)
         mBinder.exoPlayerVideoView.setAndExoPlayerListener(this)
         mBinder.exoPlayerVideoView.setSource("http://techslides.com/demos/sample-videos/small.mp4")


         val uri: Uri = Uri.parse("http://techslides.com/demos/sample-videos/small.mp4")
         mBinder.videoView.setVideoURI(uri)

         val mediaController = MediaController(this)
         mediaController.setAnchorView(mBinder.videoView)
         mediaController.setMediaPlayer(mBinder.videoView)
         mBinder.videoView.setMediaController(mediaController)
         mBinder.videoView.start()*/


       // mBinder.bvp.setSource(Uri.parse("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4"))
        mBinder.bvp.setSource(Uri.parse("http://techslides.com/demos/sample-videos/small.mp4"))
        var a = Paper.book().read("pos", 0)
        mBinder.bvp.setInitialPosition(a)
        mBinder.bvp.setAutoPlay(true)

        mBinder.bvp.setCallback(object : VideoCallback {
            override fun onStarted(player: BetterVideoPlayer) {
                Log.i("AAAA", "Started")
            }

            override fun onPaused(player: BetterVideoPlayer) {
                Log.i("AAAA", "Paused")
            }

            override fun onPreparing(player: BetterVideoPlayer) {
                Log.i("AAAA", "Preparing")
            }

            override fun onPrepared(player: BetterVideoPlayer) {
                Log.i("AAAA", "Prepared")
            }

            override fun onBuffering(percent: Int) {
                Log.i("AAAA", "Buffering $percent")
            }

            override fun onError(player: BetterVideoPlayer, e: Exception) {
                Log.i("AAAA", "Error " + e.message)
            }

            override fun onCompletion(player: BetterVideoPlayer) {
                Log.i("AAAA", "Completed")
            }

            override fun onToggleControls(player: BetterVideoPlayer, isShowing: Boolean) {

            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        Paper.book().write("pos", mBinder.bvp.getCurrentPosition())
    }
}