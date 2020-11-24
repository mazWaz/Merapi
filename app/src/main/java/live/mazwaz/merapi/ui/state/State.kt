package live.mazwaz.merapi.ui.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized

data class DashboardState(
    val volcanoStatus: String? = null,
    val geoLocation: String? = null,
    val distance: String? = null,
    val distanceTitle: String? = null,
    val altitude: String? = null
) : MvRxState
