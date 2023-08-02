package com.youarelaunched.challenge.ui.screen.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.youarelaunched.challenge.ui.screen.state.VendorsScreenUiState
import com.youarelaunched.challenge.ui.screen.view.components.ChatsumerSnackbar
import com.youarelaunched.challenge.ui.screen.view.components.CoordinatedScroll
import com.youarelaunched.challenge.ui.screen.view.components.EmptySearchResult
import com.youarelaunched.challenge.ui.screen.view.components.SearchField
import com.youarelaunched.challenge.ui.screen.view.components.VendorItem
import com.youarelaunched.challenge.ui.theme.VendorAppTheme

@Composable
fun VendorsRoute(
    viewModel: VendorsVM
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    VendorsScreen(
        uiState = uiState,
        searchText = searchText,
        onTextChange = viewModel::onTextChange,
        performSearch = viewModel::getVendors
    )
}

@Composable
fun VendorsScreen(
    uiState: VendorsScreenUiState,
    searchText: String,
    onTextChange: (String) -> Unit,
    performSearch: () -> Unit
) {
    val toolbarHeightPx = remember { mutableStateOf(0f) }
    val topPanelOffset = remember { mutableStateOf(0f) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("VENDORS_LIST_TEST_TAG"),
        backgroundColor = VendorAppTheme.colors.background,
        snackbarHost = { ChatsumerSnackbar(it) }
    ) { paddings ->

        CoordinatedScroll(
            collapsingAreaHeightPx = toolbarHeightPx
        ) { offset, nestedScroll ->
            topPanelOffset.value = offset

            Column(
                Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScroll)
            ) {
                SearchField(
                    currentOffset = offset,
                    maxOffset = toolbarHeightPx.value,
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxWidth(),
                    onHeightCalculated = {
                        toolbarHeightPx.value = it
                    },
                    searchText = searchText,
                    onTextChange = onTextChange,
                    performSearch = performSearch
                )

                if (!uiState.vendors.isNullOrEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddings)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            vertical = 24.dp,
                            horizontal = 16.dp
                        )
                    ) {
                        items(uiState.vendors) { vendor ->
                            VendorItem(
                                vendor = vendor
                            )
                        }
                    }
                } else if (uiState.vendors != null && uiState.vendors.isEmpty()) {
                    EmptySearchResult()
                }
            }
        }
    }
}
