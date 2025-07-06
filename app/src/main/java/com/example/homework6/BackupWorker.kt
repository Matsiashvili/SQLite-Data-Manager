package com.example.homework6

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class BackupWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val backupPath = inputData.getString("BACKUP_PATH") ?: return Result.failure()

        Log.d("BackupWorker", "Starting backup to $backupPath")

        Thread.sleep(3000)

        Log.d("BackupWorker", "Backup completed to $backupPath")

        val outputData = Data.Builder()
            .putString("RESULT", "Backup completed to $backupPath")
            .build()

        return Result.success(outputData)
    }
}
