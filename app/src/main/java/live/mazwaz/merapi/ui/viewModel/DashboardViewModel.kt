package live.mazwaz.merapi.ui.viewModel

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import io.reactivex.disposables.Disposable
import live.mazwaz.merapi.api.rxApi
import live.mazwaz.merapi.data.service.AppService
import live.mazwaz.merapi.ui.base.MvRxViewModel
import live.mazwaz.merapi.ui.state.DashboardState
import live.mazwaz.merapi.utils.RxBus
import live.mazwaz.merapi.utils.RxEvent
import org.koin.android.ext.android.inject
import java.util.*


class DashboardViewModel(
    state: DashboardState,
    private val service: AppService
) : MvRxViewModel<DashboardState>(state) {
    companion object : MvRxViewModelFactory<DashboardViewModel, DashboardState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: DashboardState
        ): DashboardViewModel? {
            val appService: AppService by viewModelContext.activity.inject()
            return DashboardViewModel(state, appService)
        }


    }
    fun setVolcanoStatus(status: String) = setState {
        copy(volcanoStatus = status)
    }

    fun setGeoLocation(location:String) = setState {
        copy(geoLocation = location)
    }

    fun setDistance(distance: String) = setState {
        copy(distance = distance)
    }

    fun setDistanceTitle(title: String) = setState {
        copy(distanceTitle = title)
    }

    fun setAltitude(altitude: String) = setState {
        copy(altitude = altitude)
    }


}