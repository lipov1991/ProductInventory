package pl.lanku.inventory

import android.app.Application
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
}
