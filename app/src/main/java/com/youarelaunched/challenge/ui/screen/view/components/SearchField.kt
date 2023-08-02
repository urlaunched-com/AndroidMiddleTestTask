package com.youarelaunched.challenge.ui.screen.view.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.youarelaunched.challenge.middle.R
import com.youarelaunched.challenge.ui.theme.VendorAppTheme

@Composable
fun SearchField(
    searchText: String,
    onTextChange: (String) -> Unit,
    performSearch: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 12.dp, end = 12.dp),
        elevation = Elevation,
        color = VendorAppTheme.colors.background,
        shape = RoundedCornerShape(size = 16.dp),
    ) {
        TextField(
            value = searchText,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 52.dp),
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
