package com.example.imagerecog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import java.sql.RowId
import java.util.regex.Pattern


class MainActivity2 : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val wasteType=intent.getStringExtra("WasteType")
        Log.i("WASTE",wasteType.toString())
        if(wasteType.toString() == "Anorganik"){
            initializePlayer(getYoutubeVideoIdFromUrl("https://www.youtube.com/embed/KQWYvpssvyY")!!)
        }else{
            initializePlayer(getYoutubeVideoIdFromUrl("https://www.youtube.com/embed/8jC8krdIjus")!!)
        }

        


    }



    private fun initializePlayer(videoId: String) {
        val yt: YouTubePlayerView= findViewById(R.id.youtubeplayer)
        yt.initialize(getString(R.string.api_key),object :YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1!!.loadVideo(videoId)
                p1.play()

            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext,"error occured",Toast.LENGTH_LONG).show()
            }

        })



    }

    //to get a video id from this method
    fun getYoutubeVideoIdFromUrl(inUrl: String): String? {
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1)
        }
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(inUrl)
        return if (matcher.find()) {
            matcher.group()
        } else null
    }
}
