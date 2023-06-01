package com.julianotalora.musicplayer.feature.trending.di

import android.content.Context
import com.julianotalora.musicplayer.data.DataStoreManagerImpl
import com.julianotalora.musicplayer.data.PlayerDatabase
import com.julianotalora.musicplayer.feature.trending.api.TrendingListAPI
import com.julianotalora.musicplayer.feature.trending.repository.GetTrendingListRepository
import com.julianotalora.musicplayer.feature.trending.repository.GetTrendingListRepositoryImpl
import com.julianotalora.musicplayer.feature.trending.usecase.GetTrendingListUseCase
import com.julianotalora.musicplayer.feature.trending.usecase.GetTrendingListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object TrendingModule {

    @Singleton
    @Provides
    fun provideTrendingListAPI(retrofit: Retrofit) = retrofit.create(TrendingListAPI::class.java)

    @Singleton
    @Provides
    fun provideTrendingListRepository(
        @ApplicationContext context: Context,
        trendigListAPI: TrendingListAPI,
        database: PlayerDatabase
    ): GetTrendingListRepository = GetTrendingListRepositoryImpl(trendigListAPI, DataStoreManagerImpl(context), database.trackDao())

    @Singleton
    @Provides
    fun provideTrendingListUseCase(trendingListRepository: GetTrendingListRepository) : GetTrendingListUseCase = GetTrendingListUseCaseImpl(trendingListRepository)

}
