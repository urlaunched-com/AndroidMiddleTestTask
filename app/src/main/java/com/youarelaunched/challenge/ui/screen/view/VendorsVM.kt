package com.youarelaunched.challenge.ui.screen.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youarelaunched.challenge.data.repository.VendorsRepository
import com.youarelaunched.challenge.data.repository.model.Vendor
import com.youarelaunched.challenge.ui.screen.state.VendorsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorsVM @Inject constructor(
    private val repository: VendorsRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _uiState = MutableStateFlow(
        VendorsScreenUiState(
            vendors = null
        )
    )

    suspend fun fetchVendors(): List<Vendor> {
        return if (searchText.value.isNotEmpty())
            repository.getVendorsByCompanyName(searchText.value)
        else
            repository.getVendors()
    }

    @OptIn(FlowPreview::class)
    val uiState = _uiState.debounce(500L)
        .combine(searchText) { vendors, searchText ->
            if (searchText.isBlank()) {
                vendors
            } else if (searchText.length < 3) {
                vendors
            } else {
                VendorsScreenUiState(
                    fetchVendors()
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            VendorsScreenUiState(
                vendors = _uiState.value.vendors
            )
        )

    init {
        getVendors()
    }

    fun getVendors() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    vendors = fetchVendors()
                )
            }
        }
    }

    fun onTextChange(text: String) {
        _searchText.value = text
    }
}