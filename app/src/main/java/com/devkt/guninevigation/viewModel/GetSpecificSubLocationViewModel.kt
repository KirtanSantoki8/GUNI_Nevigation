package com.devkt.guninevigation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devkt.guninevigation.common.Result
import com.devkt.guninevigation.model.SpecificLocationResponse
import com.devkt.guninevigation.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetSpecificSubLocationViewModel @Inject constructor(private val repo: Repo): ViewModel() {
    private val _getSpecificSubLocation = MutableStateFlow(GetSpecificSubLocationState())
    val getSpecificSubLocation = _getSpecificSubLocation.asStateFlow()

    fun getSpecificSubLocation(subLocation: String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getSpecificSubLocation(subLocation).collect {
                when(it) {
                    is Result.Loading -> {
                        _getSpecificSubLocation.value = GetSpecificSubLocationState(isLoading = true)
                    }
                    is Result.Success -> {
                        _getSpecificSubLocation.value = GetSpecificSubLocationState(data = it.data.body())
                    }
                    is Result.Error -> {
                        _getSpecificSubLocation.value = GetSpecificSubLocationState(error = it.message)
                    }
                }
            }
        }
    }
}

data class GetSpecificSubLocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val data: SpecificLocationResponse? = null
)