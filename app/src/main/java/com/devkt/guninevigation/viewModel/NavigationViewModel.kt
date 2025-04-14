package com.devkt.guninevigation.viewModel

import androidx.lifecycle.ViewModel
import com.devkt.guninevigation.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

}