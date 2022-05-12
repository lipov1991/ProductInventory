package pl.lanku.productinventory.di

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.lanku.productinventory.data.ProductDb
import pl.lanku.productinventory.data.ProductRepository
import pl.lanku.productinventory.data.dao.ProductDao
import pl.lanku.productinventory.presentation.products.ProductsViewModel

private const val DB_NAME = "products"

val repositoryModule = module {

    fun provideDb(application: Application): ProductDb =
        Room.databaseBuilder(application, ProductDb::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

    fun provideDao(productDb: ProductDb): ProductDao = productDb.productDao

    single { provideDb(androidApplication()) }

    single { ProductRepository(productDao = provideDao(productDb = get())) }
}

val viewModelModule = module {
    viewModel { ProductsViewModel(productsRepository = get()) }
}
