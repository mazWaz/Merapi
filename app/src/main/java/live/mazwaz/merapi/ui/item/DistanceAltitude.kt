package live.mazwaz.merapi.ui.item

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.item_distance_altitude.view.*
import live.mazwaz.merapi.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DistanceAltitude @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    var krb = 0.0
    val words = arrayOf(
        "Selalu Jauhi Jarak Bahaya Dari Puncak...",
        "Patuhi Peraturan dari Pemerintah...",
        "Jangan Lupa selalu melindungi diri...",
        "Keluarga Menunggu Di rumah...",
        "Jangan Panik..."
    )
    init {
        View.inflate(context, R.layout.item_distance_altitude, this)
    }
    @ModelProp
    fun setDistance(data: String){
        Log.d("PPPPP", (krb>=data.toDouble()).toString())
        if(krb > data.toDouble() ){
            cvDistance.setCardBackgroundColor(resources.getColor(R.color.colorAwas))
            tvDistance.setTextColor(resources.getColor(R.color.colorWhite))
            tvTitleDistance.setTextColor(resources.getColor(R.color.colorWhite))
            tvterm.text = "Anda Berada di Zona KRB, Harap Segera Menjauh"
            tvterm.setBackgroundColor(resources.getColor(R.color.colorAwas))
            tvterm.setTextColor(resources.getColor(R.color.colorWhite))
        }
        tvterm.text = words.random()
        tvDistance.text = data+" M"
    }

    @ModelProp
    fun setAltitude(data: String){
        tvAltitude.text = data+ " M"
    }
    @ModelProp
    fun setKrb(data: String){
        krb = data.toDouble()
    }

    @CallbackProp
    fun onClicked(onChange: ((String) -> Unit)?) {
        cvDistance.setOnClickListener { _ ->
            onChange?.invoke("PP")
        }
        tvTitleDistance.setOnClickListener { _ ->
            onChange?.invoke("PP")
        }
        imageView4.setOnClickListener { _ ->
            onChange?.invoke("PP")
        }
    }



}