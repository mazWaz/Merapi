package live.mazwaz.merapi

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import live.mazwaz.merapi.databinding.ActivityMainBinding
import live.mazwaz.merapi.services.GpsService
import live.mazwaz.merapi.ui.base.BaseActivity
import live.mazwaz.merapi.ui.base.BaseFragment

class MainActivity : BaseActivity<ActivityMainBinding>(){
    override fun getLayoutRes() = R.layout.activity_main


}