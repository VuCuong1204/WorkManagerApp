package vn.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.io.DataOutput
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.random.Random

class BlurWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private var listener: BlurCompleteListener? = null

    fun setBlurCompleteListener(listener: BlurCompleteListener) {
        this.listener = listener
    }

    override fun doWork(): Result {

        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.demo);
        return try {
            showNotification()
            Result.success()
        } catch (e: Exception) {
            listener?.onBlurredImageSaveError("Failed to save blurred image")
            Result.failure()
        }
    }

    private fun showNotification() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "default_channel_id"
        val channelName = "Default Channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(listOf<Int>(1,2,3,4,5,6,7,8,9).random(), notificationBuilder.build())
    }

    private fun blurBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val blurBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(blurBitmap)
        val paint = Paint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            maskFilter = BlurMaskFilter(25f, BlurMaskFilter.Blur.NORMAL)
        }

        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return blurBitmap
    }

    private fun saveBitmapToFile(bitmap: Bitmap): String? {
        val filename = "blurred_image_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BlurredImages")
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(uri)
            outputStream?.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            return uri.toString()
        }

        return null
    }
}

interface BlurCompleteListener {
    fun onBlurredImageSaved(outputUri: String)
    fun onBlurredImageSaveError(errorMessage: String)
}
