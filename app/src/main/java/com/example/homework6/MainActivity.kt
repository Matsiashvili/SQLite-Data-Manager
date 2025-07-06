package com.example.homework6

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var backupResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backupResultTextView = findViewById(R.id.backupResultTextView)

        val backupButton: Button = findViewById(R.id.backupButton)
        backupButton.setOnClickListener {
            startOneTimeBackup()
        }

        setupPeriodicBackup()
    }

    private fun startOneTimeBackup() {
        val backupPath = "/storage/emulated/0/Backup/"
        val inputData = Data.Builder()
            .putString("BACKUP_PATH", backupPath)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<BackupWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo ->
                if (workInfo != null && workInfo.state.isFinished) {
                    val result = workInfo.outputData.getString("RESULT") ?: "No result"
                    backupResultTextView.text = result
                }
            }
    }

    private fun setupPeriodicBackup() {
        val periodicWorkRequest = PeriodicWorkRequestBuilder<BackupWorker>(
            24, TimeUnit.HOURS
        )
            .setInputData(
                Data.Builder()
                    .putString("BACKUP_PATH", "/storage/emulated/0/Backup/")
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "PeriodicBackup",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

        Log.d("MainActivity", "Periodic backup scheduled")
    }
}