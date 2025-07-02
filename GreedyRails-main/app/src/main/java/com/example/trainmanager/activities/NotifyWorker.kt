package com.example.trainmanager.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.trainmanager.R


class NotifyWorker(val context: Context, val params: WorkerParameters) :
    Worker(context, params) {
    override fun doWork(): Result {
        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                //                                        grantResults: IntArray)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return@with
            }
            notify(1, NotificationCompat.Builder(context, "ItinerariNotificationChannel").setContentTitle("AVVISO").setContentText("Preparati al tuo viaggio! Hai una partenza tra un'ora").setSmallIcon(
                R.drawable.rail).build())
        }
        return Result.success()
    }
}