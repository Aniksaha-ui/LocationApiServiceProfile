package com.example.locationapiservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.util.Log.d
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.locationapiservices.AppDb.appDb
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class locationservice : Service() {
    var currentlatitude : Double = 0.0
    var currentlongitude : Double =0.0
    var prelatitude : Double = 0.0
    var prelongitude : Double = 0.0
    var result = FloatArray(1)
    var i : Int = 1;
    var distance : Float = 0.0F

    var newdistance : Float = 0.0F

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult != null && locationResult.lastLocation != null) {
                currentlatitude = locationResult.lastLocation.latitude
                currentlongitude = locationResult.lastLocation.longitude
                Log.d("current", "latitude is $currentlatitude and longtitude is $currentlongitude")
//                if(currentlatitude == )

//                Toast.makeText(applicationContext, currentlatitude.toString() + " " + currentlongitude.toString(), Toast.LENGTH_LONG).show()
//                Toast.makeText(applicationContext, prelatitude.toString() + " " + prelongitude.toString(), Toast.LENGTH_LONG).show()

                if(i==1){
                    prelatitude = currentlatitude
                    prelongitude = currentlongitude
                }
            }

            Thread{
                var db = Room.databaseBuilder(applicationContext, appDb::class.java,"Usersettings").build()

                db.UserSetting_dao().selectall().forEach {
                    Log.d("data",it.usermood.toString())
                    Location.distanceBetween(
                        it.latitude!!,
                        it.longitude!!,
                        currentlatitude,
                        currentlongitude,
                        result
                    )
                    distance = result[0]
                    d("distance",distance.toString())
                    var am = getSystemService(Context.AUDIO_SERVICE) as AudioManager

                    if(distance <= it.Distance!!.toDouble()){
                        if(it.MediaVolume==0 && it.Vibrate==1){
                            am.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                            am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                it.MediaVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                                it.callVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_ALARM,
                                it.AlarmVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)

                        }

                        else if(it.MediaVolume==0 && it.Vibrate==2){
                            am.ringerMode = AudioManager.RINGER_MODE_SILENT
                            am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                it.MediaVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                                it.callVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_ALARM,
                                it.AlarmVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)

                        }




                        else if(it.MediaVolume!! >=1 && it.Vibrate==1 ){
                            am.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                            am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                it.MediaVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                                it.callVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_ALARM,
                                it.AlarmVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)

                        }


                        else if(it.MediaVolume!! >=1 && it.Vibrate==2 ){
                            am.ringerMode = AudioManager.RINGER_MODE_NORMAL
                            am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                it.MediaVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                                it.callVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)
                            am.setStreamVolume(AudioManager.STREAM_ALARM,
                                it.AlarmVolume!!.toInt(),AudioManager.FLAG_PLAY_SOUND)

                        }

                    }
                }
            }.start()
        }
    }



    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("not yet implemented")
    }

    private fun StartLocationService() {
        val channelId = "location_notification_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder =
            NotificationCompat.Builder(applicationContext, channelId)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("location service")
        builder.setDefaults(NotificationCompat.DEFAULT_ALL)
        builder.setContentText("Running")
        builder.setContentIntent(pendingIntent)
        builder.setAutoCancel(true)
        builder.priority = NotificationCompat.PRIORITY_MAX
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    "location_service",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.description = "This channel is used by location service"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        val locationRequest = LocationRequest()
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 2000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        startForeground(Constant.LOCATION_SERVICE_ID, builder.build())
    }

    private fun stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
            .removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                if (action == Constant.ACTION_START_LOCATION_SERVICE) {
                    StartLocationService()
                } else if (action == Constant.ACTION_STOP_LOCATION_SERVICE) {
                    stopLocationService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


}