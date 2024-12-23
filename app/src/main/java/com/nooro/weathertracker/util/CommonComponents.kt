package com.nooro.weathertracker.util

import android.content.Context
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nooro.weathertracker.R
import com.nooro.weathertracker.ui.theme.AntiFlashWhite
import com.nooro.weathertracker.ui.theme.CharlestonGreen
import com.nooro.weathertracker.ui.theme.SilverSand
import com.nooro.weathertracker.ui.theme.font
import com.nooro.weathertracker.ui.theme.getTextFiledColor

/**
 * Created by Jeetesh Surana.
 */


@Composable
internal fun CommonTextField(
    placeHolder: String? = stringResource(R.string.search_location),
    keyboardController: SoftwareKeyboardController?,
    searchValue: TextFieldValue,
    errorMessage: String?,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (TextFieldValue) -> Unit,
    onTrailingIconClick: () -> Unit,
) {
    TextField(
        value = searchValue,
        onValueChange = onValueChange,
        isError = !errorMessage.isNullOrEmpty(),
        supportingText = supportingText,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
            onTrailingIconClick()
        }),
        singleLine = true,
        maxLines = 1,
        placeholder = {
            placeHolder?.let {
                Text(
                    it, style = font(15.sp, fontWeight = FontWeight.Normal,darkTheme = false), color = SilverSand,lineHeight = 0.sp,
                modifier = Modifier.padding(0.dp))
            }
        },
        trailingIcon = {
            Box(modifier = Modifier.padding(end = 12.dp)
                .circularClickable { onTrailingIconClick() }) {
                Image(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        modifier = Modifier.fillMaxWidth().padding(24.dp), colors = getTextFieldColors(),
        textStyle = font(15.sp, fontWeight = FontWeight.Normal, color = CharlestonGreen),
    )
}

@Composable
internal fun getTextFieldColors() = TextFieldDefaults.colors().copy(
    focusedContainerColor = AntiFlashWhite,
    unfocusedContainerColor = AntiFlashWhite,
    focusedTextColor = getTextFiledColor(),
    errorIndicatorColor = Color.Transparent,
    unfocusedTextColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent
)

fun Modifier.circularClickable(
    onClick: () -> Unit
): Modifier {
    return this
        .clip(CircleShape) // Clip the content into a circular shape
        .clickable { onClick() } // Handle the click
}

@Composable
fun GlideImage(
    url: String?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context: Context ->
            ImageView(context).apply {
                // You can set layout parameters if needed
            }
        },
        update = { imageView ->
            Glide.with(imageView.context)
                .load(url?.correctWeatherIconUrl())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_logo) // Placeholder image
                        .error(R.drawable.ic_cloud_with_sun) // Error image
                )
                .into(imageView)
        },
        modifier = modifier
    )
}

fun String.correctWeatherIconUrl(): String {
    val baseUrl = "https:"
    return if (this.startsWith("/")) {
        "$baseUrl$this"
    } else {
        "$baseUrl/$this"
    }
}