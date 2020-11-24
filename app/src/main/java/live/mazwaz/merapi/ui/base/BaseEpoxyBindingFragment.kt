package live.mazwaz.merapi.ui.base

import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.stickyheader.StickyHeaderLinearLayoutManager
import live.mazwaz.merapi.R
import live.mazwaz.merapi.databinding.FragmentTopBinding

abstract class BaseEpoxyBindingFragment : BaseEpoxyFragment<FragmentTopBinding>(){

    open val toolbarTitle: String = ""
    override var fragmentLayout: Int = R.layout.fragment_top
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.layoutManager = StickyHeaderLinearLayoutManager(requireContext())
            text = toolbarTitle
        }
    }
}