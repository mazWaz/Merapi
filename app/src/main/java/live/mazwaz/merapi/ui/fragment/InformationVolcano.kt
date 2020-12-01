package live.mazwaz.merapi.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import live.mazwaz.merapi.*
import live.mazwaz.merapi.databinding.FragmentBaseBinding
import live.mazwaz.merapi.ui.base.BaseEpoxyFragment
import live.mazwaz.merapi.ui.base.MvRxEpoxyController
import live.mazwaz.merapi.ui.base.buildController
import live.mazwaz.merapi.ui.item.image
import live.mazwaz.merapi.ui.item.volcanoStatusInfo
import live.mazwaz.merapi.ui.state.DashboardState
import live.mazwaz.merapi.ui.viewModel.DashboardViewModel

class InformationVolcano: BaseEpoxyFragment<FragmentBaseBinding>() {
    override var fragmentLayout: Int = R.layout.fragment_base
    private val viewModel: DashboardViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMerapiInfo()
    }
    override fun epoxyController(): MvRxEpoxyController = buildController(viewModel) { state ->

        when (val response = state.volcanoResponse) {
            is Success -> {
                val data = response.invoke().laporan[0]
                image {
                    id("image")
                    image(data.var_image)
                }
                informationDashboard {
                    id("informationDashboard")
                    suhu("${data.var_suhumin} - ${data.var_suhumax} °C")
                    kelembapan("${data.var_kelembabanmin} - ${data.var_kelembabanmax}%")
                    tekanan("${data.var_tekananmin} - ${data.var_tekananmax}")
                }
                weather {
                    id("weather ")
                }
                visualView {
                    id("informationDashboard")
                    pengamatan(
                        "Gunung Merapi Tertutup Kabut ${data.var_visibility} \n" +
                                "Asap Kawah ${data.var_asap}")
                }
            }

        }
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
}