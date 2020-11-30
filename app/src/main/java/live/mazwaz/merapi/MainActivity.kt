package live.mazwaz.merapi

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import live.mazwaz.merapi.databinding.ActivityMainBinding
import live.mazwaz.merapi.services.GpsService
import live.mazwaz.merapi.ui.base.BaseActivity
import live.mazwaz.merapi.ui.base.BaseFragment

class MainActivity : BaseActivity<ActivityMainBinding>(){
    override fun getLayoutRes() = R.layout.activity_main
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }

}