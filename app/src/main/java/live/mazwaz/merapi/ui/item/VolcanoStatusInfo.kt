package live.mazwaz.merapi.ui.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.item_volcano_info.view.*
import live.mazwaz.merapi.R

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
    fun setData(aa: String){
        cvStatus.setCardBackgroundColor(resources.getColor(R.color.colorAwas) )
        tvStatus.text = "Awas"

    }
}