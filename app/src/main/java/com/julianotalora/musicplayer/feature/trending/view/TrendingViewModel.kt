package com.julianotalora.musicplayer.feature.trending.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julianotalora.musicplayer.common.runCatchingWithMinimumTime
import com.julianotalora.musicplayer.di.IoDispatcher
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import com.julianotalora.musicplayer.feature.trending.usecase.GetTrendingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TrendingViewModel @Inject constructor(
    val getTrendingListUseCase: GetTrendingListUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _trendingListFlow = MutableStateFlow<List<TrendingElement>>(emptyList())
    val trendingListFlow: Flow<List<TrendingElement>> = _trendingListFlow

    private val _rockListFlow = MutableStateFlow<List<TrendingElement>>(emptyList())
    val rockListFlow: Flow<List<TrendingElement>> = _rockListFlow

    private val _hipHopListFlow = MutableStateFlow<List<TrendingElement>>(emptyList())
    val hipHopListFlow: Flow<List<TrendingElement>> = _hipHopListFlow

    private val _electroListFlow = MutableStateFlow<List<TrendingElement>>(emptyList())
    val electroListFlow: Flow<List<TrendingElement>> = _electroListFlow

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: Flow<Boolean> = _loadingFlow

    init{
        getTrendingList()
    }


    fun addOrRemoveFavorite(favorite: Boolean, id: String){
        viewModelScope.launch(ioDispatcher){
            runCatchingWithMinimumTime {
                getTrendingListUseCase.setFavorite(id, favorite)
            }.onSuccess {
                //_trendingListFlow.value = it
            }.onFailure {
                it.cause
            }
        }
    }

    fun getTabList(category: Int){
        viewModelScope.launch(ioDispatcher){
            showLoading()
            runCatchingWithMinimumTime {
                getTrendingListUseCase.getTrendingList(category.toString())
            }.onSuccess {
                when(category){
                    152 -> {
                        _rockListFlow.value = it
                    }
                    116 -> {
                        _hipHopListFlow.value = it
                    }
                    106 -> {
                        _electroListFlow.value = it
                    }
                }
                hideLoading()
            }.onFailure {
                it.cause
                hideLoading()
            }
        }
    }

    fun getTrendingList(){
        viewModelScope.launch(ioDispatcher){
            showLoading()
            runCatchingWithMinimumTime {
                getTrendingListUseCase.getTrendingList()
            }.onSuccess {
                _trendingListFlow.value = it
                hideLoading()
            }.onFailure {
                it.cause
                hideLoading()
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