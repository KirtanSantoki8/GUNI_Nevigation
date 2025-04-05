package com.devkt.guninevigation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devkt.guninevigation.common.Result
import com.devkt.guninevigation.model.GetMoreLocationsResponse
import com.devkt.guninevigation.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetMoreLocationsViewModel @Inject constructor(private val repo: Repo): ViewModel() {
    private val _getMoreLocations = MutableStateFlow(GetMoreLocationsState())
    val getMoreLocations = _getMoreLocations.asStateFlow()

    fun getMoreLocations(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getMoreLocations().collect {
                when(it) {
                    is Result.Loading -> {
                        _getMoreLocations.value = GetMoreLocationsState(isLoading = true)
                    }
                    is Result.Success -> {
                        _getMoreLocations.value = GetMoreLocationsState(data = it.data.body())
                    }
                    is Result.Error -> {
                        _getMoreLocations.value = GetMoreLocationsState(error = it.message)
                    }
                }
            }
        }
    }
}

data class GetMoreLocationsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: GetMoreLocationsResponse? = null
)