package id.allana.inventorybarang_androidmocktest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import id.allana.inventorybarang_androidmocktest.repository.InventoryBarangRepository
import id.allana.inventorybarang_androidmocktest.repository.base.BaseInventoryBarangRepository

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {

    @ViewModelScoped
    @Provides
    fun provideInventoryBarangRepository() = InventoryBarangRepository() as BaseInventoryBarangRepository
}