package live.mazwaz.merapi.ui.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*
import live.mazwaz.merapi.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class Image @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.item_image, this)
    }
    @ModelProp
    fun setImage(data: String){
        Glide
            .with(context)
            .load(data)
            .centerCrop()
            .placeholder(R.drawable.ic_image_notfound)
            .into(imageVolcano);
    }

}