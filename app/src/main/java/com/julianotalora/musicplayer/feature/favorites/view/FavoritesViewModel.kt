package com.julianotalora.musicplayer.feature.favorites.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianotalora.musicplayer.common.runCatchingWithMinimumTime
import com.julianotalora.musicplayer.di.IoDispatcher
import com.julianotalora.musicplayer.feature.favorites.usecase.GetFavoritesListUseCase
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    val getFavoritesListUseCase: GetFavoritesListUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _favoritesListFlow = MutableStateFlow<List<TrendingElement>>(emptyList())
    val favoritesListFlow: Flow<List<TrendingElement>> = _favoritesListFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: Flow<Boolean> = _loadingFlow

    init{
        getFavoritesList()
    }

    fun getFavoritesList(){
        viewModelScope.launch(ioDispatcher){
            showLoading()
            runCatchingWithMinimumTime {
                getFavoritesListUseCase.getAllFavoritesTracks()
            }.onSuccess {
                hideLoading()
                _favoritesListFlow.value = it
            }.onFailure {
                hideLoading()
                it.cause
            }
        }
    }

    fun showLoading() {
        _loadingFlow.value = true
    }

    fun hideLoading() {
        _loadingFlow.value = false
    }
}