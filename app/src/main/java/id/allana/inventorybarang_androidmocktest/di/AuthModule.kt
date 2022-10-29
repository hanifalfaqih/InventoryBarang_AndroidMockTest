package id.allana.inventorybarang_androidmocktest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import id.allana.inventorybarang_androidmocktest.repository.AuthRepository
import id.allana.inventorybarang_androidmocktest.repository.base.BaseAuthRepository

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @ViewModelScoped
    @Provides
    fun provideAuthRepository() = AuthRepository() as BaseAuthRepository
}