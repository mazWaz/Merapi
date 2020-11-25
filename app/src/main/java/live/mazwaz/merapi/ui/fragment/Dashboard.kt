package live.mazwaz.merapi.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.activityViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.disposables.Disposable
import live.mazwaz.merapi.*
import live.mazwaz.merapi.databinding.FragmentBaseBinding
import live.mazwaz.merapi.model.RawLocation
import live.mazwaz.merapi.services.GpsService
import live.mazwaz.merapi.ui.base.BaseEpoxyFragment
import live.mazwaz.merapi.ui.base.MvRxEpoxyController
import live.mazwaz.merapi.ui.base.buildController
import live.mazwaz.merapi.ui.item.volcanoStatusInfo
import live.mazwaz.merapi.ui.viewModel.DashboardViewModel
import live.mazwaz.merapi.utils.Constants.Companion.DistanceTo
import live.mazwaz.merapi.utils.Constants.Companion.URL_BLG
import live.mazwaz.merapi.utils.RxBus
import live.mazwaz.merapi.utils.RxEvent
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.DecimalFormat
import kotlin.math.round

class Dashboard : BaseEpoxyFragment<FragmentBaseBinding>() {
    private val viewModel: DashboardViewModel by activityViewModel()
    override var fragmentLayout: Int = R.layout.fragment_base
    private lateinit var disposable: Disposable
    var PrevLocation: RawLocation = RawLocation(0.0, 0.0, 0.0)
    private val df = DecimalFormat("#.##")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(Intent(requireActivity(), GpsService::class.java))
        } else {
            requireActivity().startService(Intent(requireActivity(), GpsService::class.java))
        }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.layoutManager = GridLayoutManager(context, 1)
        }

        disposable = RxBus.listen(RxEvent.LocationChangeListener::class.java).subscribe {
            postLocation(it.data.latitude, it.data.longitude, it.data.altitude)
        }
        getStatus()
    }

    override fun epoxyController(): MvRxEpoxyController = buildController(viewModel) { state ->
        topLabel {
            id("TopLabel")
        }

        volcanoStatusInfo {
            id("Volcano Status")
            data("AA")
        }

        location {
            id("location")
            location(state.geoLocation)
        }
        distanceAltitude {
            id("DistanceAltitude")
            distance(state.distance)
            distanceInfo("Anda Berada Dikawasan Bencana Segra Menjauh")
            altitude("${state.altitude} M")
        }
        titleSparator {
            id("Sparator")
            title("Information")
        }

        informationDashboard {
            id("informationDashboard")
            info1("10")
            info2("20")
            info3("30")
        }
    }

    private fun postLocation(lat: Double, long: Double, alti: Double) {
        val geoCoder = Geocoder(requireContext())
        val addresses = geoCoder.getFromLocation(
            lat,
            long,
            1
        )
        viewModel.apply {
            if (addresses.isNotEmpty()) {
                addresses.first()?.let {
                    setGeoLocation(it.subLocality + ", " + it.locality + ", " + it.subAdminArea + ", " + it.adminArea)
                }
            }
            setAltitude("${df.format(alti)}")
            setDistance((df.format(DistanceTo(-7.5411395, 110.4460518, lat, long, "K"))).toString())
        }
    }
    @SuppressLint("MissingPermission")
    private fun getlocation() {
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocation.requestLocationUpdates(LocationRequest(), object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val lastLocation = result.lastLocation

                if (PrevLocation != RawLocation(
                        lastLocation.latitude,
                        lastLocation.longitude,
                        lastLocation.altitude
                    )
                ) {
                    PrevLocation = RawLocation(
                        lastLocation.latitude,
                        lastLocation.longitude,
                        lastLocation.altitude
                    )
                }

                if (!result.lastLocation.isFromMockProvider) {
                    postLocation(
                        lastLocation.latitude,
                        lastLocation.latitude,
                        lastLocation.altitude
                    )
                    try {

                    } catch (e: IOException) {
                        //
                    }
                }
            }
        }, null)
    }
    fun getStatus(){
        Thread(Runnable {
            try {
                val doc = Jsoup.connect(URL_BLG).get()
                val h1 = doc.select("h1")
                Log.d("PPPPP", h1.toString())
            }catch (e: Exception){
                Log.d("PPPPPE", e.toString())
            }
        }).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}