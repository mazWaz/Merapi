package live.mazwaz.merapi.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.orhanobut.hawk.Hawk
import io.reactivex.disposables.Disposable
import live.mazwaz.merapi.*
import live.mazwaz.merapi.R
import live.mazwaz.merapi.databinding.FragmentBaseBinding
import live.mazwaz.merapi.model.RawLocation
import live.mazwaz.merapi.services.GpsService
import live.mazwaz.merapi.ui.base.BaseEpoxyFragment
import live.mazwaz.merapi.ui.base.MvRxEpoxyController
import live.mazwaz.merapi.ui.base.buildController
import live.mazwaz.merapi.ui.item.distanceAltitude
import live.mazwaz.merapi.ui.item.volcanoStatusInfo
import live.mazwaz.merapi.ui.state.DashboardState
import live.mazwaz.merapi.ui.viewModel.DashboardViewModel
import live.mazwaz.merapi.utils.Constants
import live.mazwaz.merapi.utils.Constants.Companion.DistanceTo
import live.mazwaz.merapi.utils.RxBus
import live.mazwaz.merapi.utils.RxEvent
import java.text.DecimalFormat

class Dashboard : BaseEpoxyFragment<FragmentBaseBinding>() {
    private val viewModel: DashboardViewModel by activityViewModel()
    override var fragmentLayout: Int = R.layout.fragment_base

    private lateinit var disposable: Disposable
    var PrevLocation: RawLocation = RawLocation(0.0, 0.0, 0.0)
    private val df = DecimalFormat("#.##")
    private var previousStatus = ""
    private val krb = 3
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(
                Intent(
                    requireActivity(),
                    GpsService::class.java
                )
            )
        } else {
            requireActivity().startService(Intent(requireActivity(), GpsService::class.java))
        }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.layoutManager = GridLayoutManager(context, 1)
        }
        previousStatus = if (!Hawk.contains(Constants.PREVIOUS_STATUS)) {
            "NORMAL"
        } else {
            Hawk.get(Constants.PREVIOUS_STATUS)
        }
        viewModel.getVolcanos()
        getMerapiInfo()
        getlocation()
    }

    override fun epoxyController(): MvRxEpoxyController = buildController(viewModel) { state ->
        topLabel {
            id("TopLabel")
        }
        when (val response = state.volcanoResponse) {

            is Success -> {
                val data = response.invoke().laporan[0]
                Log.d("PPPPP", data.cu_status)
                volcanoStatusInfo {
                    id("Volcano Status")
                    data(data.cu_status)
                }
            }
            else -> {
                volcanoStatusInfo {
                    id("Volcano Status")
                    data(previousStatus)
                }
            }
        }
        location {
            id("location")
            location(state.geoLocation)
        }
        if (state.distance != null && state.altitude != null) {
            distanceAltitude {
                id("DistanceAltitude")
                distance(state.distance)
                altitude(state.altitude)
                krb(krb.toString())
                onClicked {
                    findNavController().navigate(R.id.action_dashboard_to_distance)
                }
            }
        } else {
            distanceAltitude {
                id("DistanceAltitude")
                distance("0")
                altitude("0")
                krb(krb.toString())
                onClicked {
                    findNavController().navigate(R.id.action_dashboard_to_distance)
                }
            }
        }
        when (val response = state.volcanoResponse) {
            is Success -> {
                val data = response.invoke().laporan[0]
                data
                titleSparator {
                    id("Sparator")
                    title("Informasi Merapi")
                    titleRight("Detail >")
                    onDetailClick { _ ->
                        findNavController().navigate(R.id.action_dashboard_to_informationVolcano)
                    }
                }
            }

        }


        when (val response = state.volcanoResponse) {
            is Success -> {
                val data = response.invoke().laporan[0]
                informationDashboard {
                    id("informationDashboard")
                    suhu("${data.var_suhumin} - ${data.var_suhumax} °C")
                    kelembapan("${data.var_kelembabanmin} - ${data.var_kelembabanmax}%")
                    tekanan("${data.var_tekananmin} - ${data.var_tekananmax}")
                }
            }

        }

        when (val response = state.volcanoResponse) {
            is Success -> {
                val data = response.invoke().laporan[0]
                var visual = ""
                if(data.var_asap !="Tidak Teramati"){
                    if(data.var_wasap != "-"){
                        visual += "berwarna " + data.var_wasap
                    }
                    if(data.var_intasap != "-"){
                        visual += " dengan intensitas "+data.var_intasap
                    }
                    if (data.var_tasap != "-"){
                        visual += if(data.var_tasap != data.var_tasap_min){
                            " dan tinggi "+data.var_tasap_min +"-"+data.var_tasap+"M diatas Puncak Kawah"
                        }else{
                            " dan tinggi "+data.var_tasap_min+"M diatas Puncak Kawah"
                        }
                    }
                }
                visualView {
                    id("informationDashboard")
                    pengamatan(
                        "Gunung Merapi Terlihat ${data.var_visibility} \n" +
                            "Asap Kawah ${data.var_asap} ${visual}")
                }
            }
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
                        lastLocation.longitude,
                        lastLocation.altitude
                    )
                }
            }
        }, null)
    }

    private fun getMerapiInfo() {
        viewModel.asyncSubscribe(
            subscriptionLifecycleOwner,
            DashboardState::volcanosResponse,
            onSuccess = {
                it.res.forEach {
                    if (it.ga_nama_gapi.toUpperCase().contains("MERAPI")) {
                        viewModel.getVolcano(it.no)
                    }
                }
            }

        )
    }


    override fun onStart() {
        super.onStart()
        disposable = RxBus.listen(RxEvent.LocationChangeListener::class.java).subscribe {
            postLocation(it.data.latitude, it.data.longitude, it.data.altitude)
        }
    }

    override fun onStop() {
        disposable.dispose()
        super.onStop()
    }
    override fun onPause() {
        disposable.dispose()
        super.onPause()

    }
    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()

    }
}