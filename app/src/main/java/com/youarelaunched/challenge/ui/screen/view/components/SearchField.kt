package com.youarelaunched.challenge.ui.screen.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationDefaults.Elevation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.youarelaunched.challenge.middle.R
import com.youarelaunched.challenge.ui.theme.VendorAppTheme
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun SearchField(
    currentOffset: Float,
    maxOffset: Float,
    modifier: Modifier = Modifier,
    onHeightCalculated: (Float) -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    performSearch: () -> Unit,
) {

    val alpha = (kotlin.math.abs(currentOffset) / maxOffset).pow(0.75f)
    Surface(
        modifier = modifier
            .offset { IntOffset(x = 0, y = currentOffset.roundToInt()) }
            .padding(top = 24.dp, start = 12.dp, end = 12.dp)
            .onGloballyPositioned {
                onHeightCalculated(it.size.height.toFloat() + 100f)
            },
        elevation = Elevation,
        color = VendorAppTheme.colors.background,
        shape = RoundedCornerShape(size = 16.dp),
    ) {
        TextField(
            value = searchText,
            onValueChange = onTextChange,
            modifier = Modifier
                .alpha(1f - alpha)
                .fillMaxWidth()
                .height(height = 52.dp)
                .testTag("VENDORS_SEARCH_FIELD_TEST_TAG"),
            textStyle = MaterialTheme.typography.subtitle1,
            trailingIcon = {
                IconButton(onClick = performSearch) {
                    Icon(
                        contentDescription = "Search Icon",
                        painter = painterResource(id = R.drawable.ic_search),
                        modifier = Modifier.size(24.dp),
                        tint = VendorAppTheme.colors.text
                    )
                }
            },
            placeholder = {
                Text(
                    text = stringResource(id = R.string.msg_search_field_hint),
                    color = VendorAppTheme.colors.text,
                    style = VendorAppTheme.typography.subtitle2
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = VendorAppTheme.colors.text,
                backgroundColor = VendorAppTheme.colors.background,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Transparent
            ),
            singleLine = true,
        )
    }
}


@Composable
internal fun CoordinatedScroll(
    collapsingAreaHeightPx: MutableState<Float> = remember { mutableStateOf(0f) },
    content: @Composable (Float, NestedScrollConnection) -> Unit
) {
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val currentAbsoluteOffsetPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                var absoluteOffset = currentAbsoluteOffsetPx.value + delta
                if (absoluteOffset > 0f) {
                    absoluteOffset = 0f
                }
                currentAbsoluteOffsetPx.value = absoluteOffset

                if (absoluteOffset >= -collapsingAreaHeightPx.value) {
                    toolbarOffsetHeightPx.value = absoluteOffset
                } else {
                    toolbarOffsetHeightPx.value = -collapsingAreaHeightPx.value
                }

                return Offset.Zero
            }
        }
    }

    content(
        toolbarOffsetHeightPx.value,
        nestedScrollConnection
    )
}
