package live.mazwaz.merapi.ui.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.epoxy.stickyheader.StickyHeaderLinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import live.mazwaz.merapi.R


abstract class BaseEpoxyFragment<VDB : ViewDataBinding> : BaseMvRxFragment(){
    protected lateinit var binding: VDB
    protected lateinit var recyclerView: EpoxyRecyclerView
    private val epoxyController by lazy { epoxyController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
    }

    @LayoutRes
    open var fragmentLayout: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, fragmentLayout, container, false)

        return binding.root.apply {
            recyclerView = findViewById(R.id.recyclerView)
            recyclerView.layoutManager = StickyHeaderLinearLayoutManager(context)
            recyclerView.isHorizontalScrollBarEnabled = false
            recyclerView.setController(epoxyController)

        }
    }

    override fun invalidate() {
        recyclerView.requestModelBuild()
    }

    abstract fun epoxyController(): MvRxEpoxyController

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        epoxyController.cancelPendingModelBuild()
        super.onDestroyView()
    }

    protected fun hideSoftKey(context: Context?, view: View) {
        (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)
    }

}