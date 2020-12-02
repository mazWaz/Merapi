package live.mazwaz.merapi.ui.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_weather.view.*
import live.mazwaz.merapi.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class Weather @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.item_weather, this)
    }

    @ModelProp
    fun setWeather(data: String) {
        var image = 0
        if (data.toUpperCase().contains("HUJAN")) {
            image = R.drawable.ic_hujan
        } else if (data.toUpperCase().contains("MENDUNG")) {
            image = R.drawable.ic_mendung
        } else if (data.toUpperCase().contains("BERAWAN")) {
            image = R.drawable.ic_berawan
        } else if (data.toUpperCase().contains("CERAH")) {
            image = R.drawable.ic_cerah
        }
        Glide
            .with(context)
            .load(image)
            .centerCrop()
            .placeholder(R.drawable.ic_image_notfound)
            .into(imageWeather);
        tvWeather.text = data
    }
    @ModelProp
    fun setWind(data: String) {
        tvWind.text = data
    }


}