package live.mazwaz.merapi

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.MvRxViewModelConfigFactory
import com.orhanobut.hawk.Hawk
import live.mazwaz.merapi.di.appModule
import live.mazwaz.merapi.utils.Constants.Companion.API_URL
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).isLowRamDevice
        }
        (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass >= 128
        Runtime.getRuntime().availableProcessors() >= 4

        Hawk.init(this).build()
        if(!Hawk.contains(API_URL)){
            Hawk.put(API_URL, "http://mes.development.io")
        }

        MvRx.viewModelConfigFactory = MvRxViewModelConfigFactory(applicationContext)

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

    }
}