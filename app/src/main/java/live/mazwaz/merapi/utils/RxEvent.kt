package live.mazwaz.merapi.utils

import live.mazwaz.merapi.model.RawLocation

class RxEvent {
    data class LocationChangeListener(
        val data: RawLocation
    )
}