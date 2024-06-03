package vn.example.workmanager.recorder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import vn.example.workmanager.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class RecorderActivity : AppCompatActivity() {
    private lateinit var StartRecording: Button
    private lateinit var StopRecording: Button
    private lateinit var StartPlaying: Button
    private lateinit var StopPlaying: Button
    private lateinit var download: Button
    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var AudioSavaPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recorder_activity)
        StartRecording = findViewById(R.id.startRecord)
        StopRecording = findViewById(R.id.stopRecord)
        StartPlaying = findViewById(R.id.startPlaying)
        StopPlaying = findViewById(R.id.stopPlaying)
        download = findViewById(R.id.download)

        // Yêu cầu quyền nếu chưa được cấp
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        }

        StartRecording.setOnClickListener {
            if (checkPermissions()) {
                AudioSavaPath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/recorder.mp3"
                mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    MediaRecorder(this)
                } else {
                    MediaRecorder()
                }
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder.setOutputFile(AudioSavaPath)
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                try {
                    mediaRecorder.prepare()
                    mediaRecorder.start()
                    Toast.makeText(this@RecorderActivity, "Recording started", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                ActivityCompat.requestPermissions(this@RecorderActivity, arrayOf<String>(
                    Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE
                ), 1)
            }
        }

        StopRecording.setOnClickListener {
            mediaRecorder.stop()
            mediaRecorder.reset()
            mediaRecorder.release()
            Toast.makeText(this@RecorderActivity, "Recording stopped", Toast.LENGTH_SHORT).show()
        }

        StartPlaying.setOnClickListener {
            mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(AudioSavaPath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                Toast.makeText(this@RecorderActivity, "Start playing", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        StopPlaying.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.release()
            Toast.makeText(this@RecorderActivity, "Stopped playing", Toast.LENGTH_SHORT).show()
        }

        download.setOnClickListener {
            downloadAudioFile()
        }
    }

    fun downloadAudioFile() {
        val srcFile = File(AudioSavaPath)
        val destFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "audiorecordtest.mp3")

        if (!srcFile.exists()) {
            // Handle file not found
            return
        }

        try {
            FileInputStream(srcFile).use { input ->
                FileOutputStream(destFile).use { output ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (input.read(buffer).also { length = it } > 0) {
                        output.write(buffer, 0, length)
                    }
                }
                Toast.makeText(this@RecorderActivity, "success", Toast.LENGTH_SHORT).show()
            }
            // Notify user the file has been saved
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the error
            Toast.makeText(this@RecorderActivity, "${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermissions(): Boolean {
        val first = ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.RECORD_AUDIO)
        val second = ActivityCompat.checkSelfPermission(applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return first == PackageManager.PERMISSION_GRANTED &&
                second == PackageManager.PERMISSION_GRANTED
    }

}
