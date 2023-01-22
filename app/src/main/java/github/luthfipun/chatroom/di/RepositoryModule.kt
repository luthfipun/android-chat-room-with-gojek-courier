package github.luthfipun.chatroom.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import github.luthfipun.chatroom.repository.MainRepository
import github.luthfipun.chatroom.repository.MainRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository =
        mainRepositoryImpl
}