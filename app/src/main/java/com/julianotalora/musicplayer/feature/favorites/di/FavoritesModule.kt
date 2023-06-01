package com.julianotalora.musicplayer.feature.favorites.di

import com.julianotalora.musicplayer.data.PlayerDatabase
import com.julianotalora.musicplayer.feature.favorites.repository.GetFavoritesListRepository
import com.julianotalora.musicplayer.feature.favorites.repository.GetFavoritesListRepositoryImpl
import com.julianotalora.musicplayer.feature.favorites.usecase.GetFavoritesListUseCase
import com.julianotalora.musicplayer.feature.favorites.usecase.GetFavoritesListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritesModule {

    @Singleton
    @Provides
    fun provideFavoriteRepository(database: PlayerDatabase) : GetFavoritesListRepository = GetFavoritesListRepositoryImpl(database.trackDao())

    @Singleton
    @Provides
    fun provideFavoriteUseCase(repository: GetFavoritesListRepository) : GetFavoritesListUseCase = GetFavoritesListUseCaseImpl(repository)

}