package live.mazwaz.merapi.ui.fragment

import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.activityViewModel
import io.reactivex.disposables.Disposable
import live.mazwaz.merapi.*
import live.mazwaz.merapi.databinding.FragmentBaseBinding
import live.mazwaz.merapi.services.GpsService
import live.mazwaz.merapi.ui.base.BaseEpoxyFragment
import live.mazwaz.merapi.ui.base.MvRxEpoxyController
import live.mazwaz.merapi.ui.base.buildController
import live.mazwaz.merapi.ui.item.volcanoStatusInfo
import live.mazwaz.merapi.ui.viewModel.DashboardViewModel
import live.mazwaz.merapi.utils.Constants.Companion.DistanceTo
import live.mazwaz.merapi.utils.RxBus
import live.mazwaz.merapi.utils.RxEvent
import java.text.DecimalFormat
import kotlin.math.round

class Dashboard : BaseEpoxyFragment<FragmentBaseBinding>() {
    private val viewModel: DashboardViewModel by activityViewModel()
    override var fragmentLayout: Int = R.layout.fragment_base
    private lateinit var disposable: Disposable
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
}