package live.mazwaz.merapi.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import live.mazwaz.merapi.MainActivity
import live.mazwaz.merapi.R
import live.mazwaz.merapi.model.RawLocation
import live.mazwaz.merapi.utils.Constants.Companion.DistanceTo
import live.mazwaz.merapi.utils.RxBus
import live.mazwaz.merapi.utils.RxEvent
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService


class GpsService : BaseService() {
    var scheduleTaskExecutor: ScheduledExecutorService = Executors.newScheduledThreadPool(5)
    var prevLocation: RawLocation = RawLocation(0.0, 0.0, 0.0)
    private val now = Calendar.getInstance()
    private val notificationMinutes = 30
    private var PrevMinuts: Long = 0
    private var TAG = "TEST"
    private var id = 1000
    private val df = DecimalFormat("#.##")
    lateinit var mLocationRequest: LocationRequest
    override fun onStartCommand() {
        mLocationRequest = LocationRequest()
//        getlocation()
        startForeground()
    }

    @SuppressLint("MissingPermission")
    private fun getlocation() {
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest.interval = 20000 //300000 //5 Menit
        mLocationRequest.fastestInterval = 10000 //60000 // 1 Menit
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.requestLocationUpdates(mLocationRequest, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val lastLocation = result.lastLocation
                val lastDistance = df.format(
                    DistanceTo(
                        -7.5411395,
                        110.4460518,
                        lastLocation.latitude,
                        lastLocation.longitude,
                        "K"
                    )
                ).toDouble()
                val krbDistance = 5
                val lastMinutes = System.currentTimeMillis()
                Log.d(TAG + "CHECK", "Last Minutes: $lastMinutes Prev minutes: $PrevMinuts")
                if (lastDistance < krbDistance && lastMinutes > PrevMinuts) {
                    PrevMinuts = lastMinutes+ (notificationMinutes*60*1000)
                    redZoneNotification(lastDistance.toString())
                }
                Log.d(TAG + "CHECK", "$lastDistance KM")
                if (!result.lastLocation.isFromMockProvider && prevLocation != RawLocation(
                        lastLocation.latitude,
                        lastLocation.longitude,
                        lastLocation.altitude
                    )
                ) {
                    prevLocation = RawLocation(
                        lastLocation.latitude,
                        lastLocation.longitude,
                        lastLocation.altitude
                    )
                    RxBus.publish(RxEvent.LocationChangeListener(prevLocation))
                }
            }
        }, null)
    }

    private fun redZoneNotification(range: String) {
        id++
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(applicationContext, "default")
        builder.setContentTitle("WARNING!!!")
        builder.setContentTitle("Anda Berada Di kawasan Zona Merah KRB")
        builder.setContentText("Anda Berada Di jarak $range KM Segera Menjahui Kawasan")
        builder.setChannelId("Chanel Redzone")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        val notification = builder.build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "Chanel Redzone",
                "Chanel Redzone",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(id, notification)
    }

    private fun startForeground() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            notificationIntent, 0
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel =
                NotificationChannel(
                    "Chanel Sticky",
                    "Chanel Sticky",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationChannel.description = "Chanel Sticky"
            notificationChannel.setSound(null, null)

            notificationManager.createNotificationChannel(notificationChannel)

            startForeground(
                1,
                NotificationCompat.Builder(this, "Chanel Sticky")
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("App-Sedang Berjalan")
                    .setContentText("Notification Untuk Memberitahu Posisi Anda")
                    .setContentIntent(pendingIntent)
                    .build()
            )
        }
        getlocation()
//        scheduleTaskExecutor.scheduleAtFixedRate({ nearbyDevice() }, 0, 30, TimeUnit.SECONDS)
    }
}