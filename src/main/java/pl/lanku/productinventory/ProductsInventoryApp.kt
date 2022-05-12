package pl.lanku.productinventory

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.lanku.productinventory.di.repositoryModule
import pl.lanku.productinventory.di.viewModelModule

class ProductsInventoryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ProductsInventoryApp)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}
