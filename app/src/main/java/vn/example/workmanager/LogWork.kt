package vn.example.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class LogWork(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {
    override fun doWork(): Result {

        Log.d("LogWork", "doWork: ")

        return Result.success()
    }
}
