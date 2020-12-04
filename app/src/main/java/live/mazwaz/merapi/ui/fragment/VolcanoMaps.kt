package live.mazwaz.merapi.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.kml.KmlLayer
import kotlinx.android.synthetic.main.fragment_distance.*
import live.mazwaz.merapi.R
import live.mazwaz.merapi.utils.Constants
import java.io.ByteArrayInputStream
import java.io.IOException
import java.text.DecimalFormat

class VolcanoMaps : Fragment() {
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
                .fillColor(resources.getColor(R.color.colorBahaya))
        )

        mFusedLocation.requestLocationUpdates(LocationRequest(), object : LocationCallback() {

            override fun onLocationResult(result: LocationResult) {
                val lastLocation = result.lastLocation
                val myPosition = LatLng(lastLocation.latitude, lastLocation.longitude)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 0f))
                googleMap.addMarker(MarkerOptions().position(merapi).title("Gunung Merapi")).showInfoWindow()
                googleMap.isMyLocationEnabled = true
                val cameraPosition = CameraPosition.Builder()
                    .target(myPosition)
                    .zoom(10f)
                    .target(merapi)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }, null)
        try {
            val inputStream = requireActivity().assets.open("no-hospital.kmz")
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
        cvBack.setOnClickListener {
            findNavController().popBackStack()
        }
        clBack.setOnClickListener {
            findNavController().popBackStack()
        }
        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        mapFragment?.getMapAsync(callback)
    }

}