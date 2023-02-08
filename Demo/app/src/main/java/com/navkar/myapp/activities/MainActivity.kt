package com.navkar.myapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener
import com.navkar.myapp.R
import com.navkar.myapp.databinding.ActivityMainBinding
import io.paperdb.Paper

class MainActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    lateinit var mBinder: ActivityMainBinding
    private var mContext: Context? = null
    var APIKEY: String = "AIzaSyDvjaldwGJ5R5qxUiBWoZ7feFtnkIm6aRU"
    var stopWatch: StopWatch = StopWatch()
    lateinit var ytaaa: YouTubePlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        initUI()
    }

    private fun initUI() {
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinder.youTubePlayerView.initialize(APIKEY, this)
        Paper.init(applicationContext)

        mBinder.button.setOnClickListener {
            val intent = Intent(mContext, MediaActivity::class.java)
            startActivity(intent)
        }

        mBinder.buttonPDF.setOnClickListener {
            val intent = Intent(mContext, PDFActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, yt: YouTubePlayer?, flag: Boolean) {
        if (!flag) {
            yt!!.cueVideo("5xVh-7ywKpE")
            ytaaa = yt!!

            /*  yt!!.setPlayerStateChangeListener(object : PlayerStateChangeListener {
                  override fun onLoading() {
                      Log.d("yt", "loading")
                  }

                  override fun onLoaded(p0: String?) {
                      Log.d("yt", "loaded")
                  }

                  override fun onAdStarted() {
                      Log.d("yt", "ads")
                  }

                  override fun onVideoStarted() {
                      Log.d("yt", "started")
                  }

                  override fun onVideoEnded() {
                      Log.d("yt", "ended")
                  }

                  override fun onError(p0: YouTubePlayer.ErrorReason?) {
                      Log.d("yt", "error " + p0.toString())
                  }
              })*/

            yt!!.setPlaybackEventListener(playbackEventListener)
        }
    }

    private val playbackEventListener: PlaybackEventListener = object : PlaybackEventListener {
        override fun onBuffering(arg0: Boolean) {
            stopWatch.pause();
            mBinder.tvTxt.text = "Buffering "

        }

        override fun onPaused() {
            stopWatch.pause();

            mBinder.tvTxt.text = "Paused " + " Total Sec : " + stopWatch.elapsedTimeSecs

            val sec: Int = ytaaa!!.getCurrentTimeMillis()
            var a = sec / 1000
            //  Toast.makeText(this@MainActivity, " " + a + " Sec", Toast.LENGTH_SHORT).show()
        }

        override fun onPlaying() {
            stopWatch.resume();
            mBinder.tvTxt.text = "Playing "
        }

        override fun onSeekTo(arg0: Int) {
            mBinder.tvTxt.text = "Seek to " + arg0
        }

        override fun onStopped() {
            stopWatch.pause();
            mBinder.tvTxt.text = "Stoped " + " Total Sec : " + stopWatch.elapsedTimeSecs
            val sec: Int = ytaaa!!.getCurrentTimeMillis()
            var a = sec / 1000
            //  Toast.makeText(this@MainActivity, " " + a + " Sec", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult?) {
        if (errorReason!!.isUserRecoverableError()) {
            errorReason!!.getErrorDialog(this, 1).show()
        } else {
            val error: String = errorReason!!.toString()
            Log.d("yt", error)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            getYouTubePlayerProvider().initialize(APIKEY, this)
        }
    }

    protected fun getYouTubePlayerProvider(): YouTubePlayer.Provider {
        return mBinder.youTubePlayerView
    }

    inner class StopWatch {
        private var startTime: Long = 0
        private var stopTime: Long = 0
        private var elapsedTime: Long = 0
        private var running = false
        fun resume() {
            if (!running) {
                startTime = System.currentTimeMillis()
                running = true
            }
        }

        fun pause() {
            if (running) {
                stopTime = System.currentTimeMillis()
                running = false
                elapsedTime += stopTime - startTime
            }
        }

        fun getElapsedTime(): Long {
            return if (running) {
                elapsedTime + System.currentTimeMillis() - startTime
            } else elapsedTime
        }

        val elapsedTimeSecs: Long
            get() = if (running) {
                (System.currentTimeMillis() - startTime + elapsedTime) / 1000
            } else elapsedTime / 1000
    }
}