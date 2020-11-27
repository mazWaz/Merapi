package live.mazwaz.merapi.ui.item

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.item_volcano_info.view.*
import live.mazwaz.merapi.R
import live.mazwaz.merapi.utils.Constants
import live.mazwaz.merapi.utils.Constants.Companion.PREVIOUS_STATUS

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class VolcanoStatusInfo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.item_volcano_info, this)
    }

    @ModelProp
    fun setData(data: String){
        if(Hawk.get<String>(PREVIOUS_STATUS) != data){
            if(data.toUpperCase().contains("NORMAL")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorNormal))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_normal))
            }else if(data.toUpperCase().contains("WASPADA")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorWaspada) )
                tvTitle.setTextColor(resources.getColor(R.color.colorWhite))
                tvStatus.setTextColor(resources.getColor(R.color.colorWhite))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_waspada))
            }else if(data.toUpperCase().contains("SIAGA")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorSiaga) )
                tvTitle.setTextColor(resources.getColor(R.color.colorWhite))
                tvStatus.setTextColor(resources.getColor(R.color.colorWhite))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_siaga))
            }else if(data.toUpperCase().contains("AWAS")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorAwas) )
                tvTitle.setTextColor(resources.getColor(R.color.colorWhite))
                tvStatus.setTextColor(resources.getColor(R.color.colorWhite))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_awas))
            }
            Hawk.put(PREVIOUS_STATUS, data)
            tvStatus.text = data
        }else{
            val previousData = Hawk.get<String>(PREVIOUS_STATUS)
            if(previousData.toUpperCase().contains("NORMAL")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorNormal))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_normal))
            }else if(previousData.toUpperCase().contains("WASPADA")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorWaspada) )
                tvTitle.setTextColor(resources.getColor(R.color.colorWhite))
                tvStatus.setTextColor(resources.getColor(R.color.colorWhite))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_waspada))
            }else if(previousData.toUpperCase().contains("SIAGA")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorSiaga) )
                tvTitle.setTextColor(resources.getColor(R.color.colorWhite))
                tvStatus.setTextColor(resources.getColor(R.color.colorWhite))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_siaga))
            }else if(previousData.toUpperCase().contains("AWAS")){
                cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorAwas) )
                tvTitle.setTextColor(resources.getColor(R.color.colorWhite))
                tvStatus.setTextColor(resources.getColor(R.color.colorWhite))
                imageStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_volcano_awas))
            }
            tvStatus.text = previousData
        }
        
       

    }
}