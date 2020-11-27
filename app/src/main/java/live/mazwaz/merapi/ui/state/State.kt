package live.mazwaz.merapi.ui.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import live.mazwaz.merapi.model.VolcanoResponses
import live.mazwaz.merapi.model.VolcanosResponses

data class DashboardState(
    val volcanoStatus: String? = null,
    val geoLocation: String? = null,
    val distance: String? = null,
    val distanceTitle: String? = null,
    val altitude: String? = null,
    val volcanosResponse: Async<VolcanosResponses> = Uninitialized,
    val volcanoResponse: Async<VolcanoResponses> = Uninitialized
) : MvRxState
