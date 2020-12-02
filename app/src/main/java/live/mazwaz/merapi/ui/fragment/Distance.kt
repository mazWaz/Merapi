package live.mazwaz.merapi.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.kml.KmlLayer
import live.mazwaz.merapi.R
import live.mazwaz.merapi.utils.Constants
import java.io.ByteArrayInputStream
import java.io.IOException
import java.text.DecimalFormat


class Distance : Fragment() {
    private val df = DecimalFormat("#.##")

    @SuppressLint("MissingPermission")

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val merapi = LatLng(-7.5411395, 110.4460518)
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        googleMap.addCircle(
            CircleOptions()
                .center(merapi)
                .radius(5000.0)
                .strokeColor(Color.RED)
                .fillColor(Color.RED)
        )

        mFusedLocation.requestLocationUpdates(LocationRequest(), object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {
                val lastLocation = result.lastLocation
                val myPosition = LatLng(lastLocation.latitude, lastLocation.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 0f))
                googleMap.addPolyline(
                    PolylineOptions()
                        .add(merapi)
                        .add(myPosition)
                        .width(6f)
                        .color(Color.RED)
                )
                googleMap.addMarker(
                    MarkerOptions()
                        .position(myPosition)
                        .title("Jarak Dengan Merapi")
                        .snippet(
                            " ${
                                df.format(
                                    Constants.DistanceTo(
                                        -7.5411395,
                                        110.4460518,
                                        lastLocation.latitude,
                                        lastLocation.longitude,
                                        "K"
                                    )
                                )
                            } KM"
                        )
                ).showInfoWindow()
                googleMap.addMarker(MarkerOptions().position(merapi).title("Gunung Merapi"))
                googleMap.isMyLocationEnabled = true
                val cameraPosition = CameraPosition.Builder()
                    .target(myPosition) // Sets the center of the map to Mountain View
                    .zoom(10f)            // Sets the zoom
                    .target(
                        LatLng(myPosition.latitude,myPosition.longitude)
                    )// Sets the tilt of the camera to 30 degrees
                    .build()              // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }, null)
        try {
            val inputStream = requireActivity().assets.open("overlay-merapi_krbonly.xml")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            val layer = KmlLayer(googleMap, ByteArrayInputStream(buffer), requireContext())
            layer.addLayerToMap()
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_distance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    fun midPoint(lat1: Double, long1: Double, lat2: Double, long2: Double): LatLng {
        return LatLng((lat1 + lat2) / 2, (long1 + long2) / 2)
    }


}