package com.plcoding.backgroundlocationtracking

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GeofenceService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var polygon: MutableList<LatLng>

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if(intent.hasExtra("VERTICES_LIST")){
            polygon = intent.getSerializableExtra("VERTICES_LIST") as MutableList<LatLng>

            Log.d("GeofenceService", intent.hasExtra("VERTICES_LIST").toString())
            Log.d("GeofenceService", polygon.toString())

        }

        when(intent.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: fetching location...")
            .setPriority(2)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->

//                val lat = location.latitude.toString().takeLast(3)
                val lat = location.latitude
                val long = location.longitude
                val currentLocation = LatLng(lat, long)

                if(isInsideGeofence(currentLocation)){
                    val updatedNotification = notification
                        .setContentText(
                        "You are inside geofence"
                    )
                    notificationManager.notify(1, updatedNotification.build())
                }else{
                    val updatedNotification = notification
                        .setContentText(
                            "You are outside geofence"
                        )
                    notificationManager.notify(1, updatedNotification.build())
                }
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    private fun isInsideGeofence(currentLocation: LatLng): Boolean {
        val polygon2 =  addCoOrdsToList()
        // Outside Points
        // val pointOutside = LatLng(13.01988270399168, 77.7249941249847)
        // Inside Points
        // val pointInside = LatLng(13.01938164570265, 77.72744209234357)
        // val res = PolyUtil.containsLocation(pointOutside, polygon, false)
        // val res2 = PolyUtil.containsLocation(pointInside, polygon, false)
        val result: Boolean = if(polygon.size == 0){
            PolyUtil.containsLocation(currentLocation, polygon2, false)
        }else{
            PolyUtil.containsLocation(currentLocation, polygon, false)
        }

        Log.d(TAG, "Res : $result")

        return result
    }

    private fun addCoOrdsToList(): ArrayList<LatLng> {
        val coOrdsOfSquare = ArrayList<LatLng>()
        // Square Vertices
        coOrdsOfSquare.add(LatLng(13.018952968666893, 77.72852987114437))
        coOrdsOfSquare.add(LatLng(13.018765507507823, 77.72970456514402))
        coOrdsOfSquare.add(LatLng(13.017907130915496, 77.72917797817867))
        coOrdsOfSquare.add(LatLng(13.018124191943844, 77.72839822440304))

        return coOrdsOfSquare
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val TAG = "GeofenceService"
    }
}