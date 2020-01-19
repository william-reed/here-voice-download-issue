package dev.williamreed.voice_prompt_fail

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.here.android.mpa.common.ApplicationContext
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.common.MapSettings
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.guidance.VoiceCatalog
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeHere()
    }

    private fun initializeHere() {
        // needs to be before MapEngine init
        val dir = getExternalFilesDir(null)
        if (dir == null) {
            log("Could not get external files directory.")
            return
        }
        val success = MapSettings.setIsolatedDiskCacheRootPath(
            dir.absolutePath + File.separator + ".here-maps", "map_service_cache_intent"
        )
        if (!success) {
            log("File not usable for disk cache. See MapSettings#setIsolatedDiskCacheRootPath docs.")
            return
        }

        MapEngine.getInstance().init(ApplicationContext(this)) { error ->
            if (error == OnEngineInitListener.Error.NONE) {
                setupVoice()
            } else {
                log("Error initializing map engine ${error.name}: ${error.details}")
            }
        }
    }

    private fun setupVoice() {
        val voiceCatalog = VoiceCatalog.getInstance()
        val navigationManager = NavigationManager.getInstance()

        fun attemptDownload() =
            voiceCatalog.downloadVoice(VOICE_SKIN_ID) { error ->
            when (error) {
                VoiceCatalog.Error.NONE -> {
                    navigationManager.voiceGuidanceOptions.voiceSkin = voiceCatalog.getLocalVoiceSkin(VOICE_SKIN_ID)
                }
                VoiceCatalog.Error.NOT_ENOUGH_DISK_SPACE -> {
                    log("Not enough disk space for voice skin")
                }
                else -> {
                    log("Error while downloading voice package $error")
                }
            }
        }

        var downloadStarted = attemptDownload()
        if (!downloadStarted) {
            log("Voice download not started initially")
        } else {
            log("voice download firs time")
        }

        // spawn an async task to sleep for a few seconds and try again
        object: AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit) {
                Thread.sleep(5000L)
                downloadStarted = attemptDownload()

                if (!downloadStarted) {
                    log("Voice download not started after 5 seconds")
                } else {
                    log("voice downloaded second time")
                }
            }

        }.execute()
    }

    private fun log(msg: String) {
        Log.e("MainActivity", msg)
    }

    companion object {
        // english voice skin
        private const val VOICE_SKIN_ID = 206L
    }
}
