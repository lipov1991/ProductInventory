package pl.lanku.inventory

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.zeugmasolutions.localehelper.LocaleHelper
import com.zeugmasolutions.localehelper.LocaleHelperApplicationDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.lanku.inventory.di.repositoryModule
import pl.lanku.inventory.di.utilsModule
import pl.lanku.inventory.di.viewModelModule

class ProductsInventoryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ProductsInventoryApp)
            modules(listOf(repositoryModule, viewModelModule, utilsModule))
        }
    }

    private val localeAppDelegate = LocaleHelperApplicationDelegate()

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(localeAppDelegate.attachBaseContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeAppDelegate.onConfigurationChanged(this)
    }

    override fun getApplicationContext(): Context =
        LocaleHelper.onAttach(super.getApplicationContext())
}
