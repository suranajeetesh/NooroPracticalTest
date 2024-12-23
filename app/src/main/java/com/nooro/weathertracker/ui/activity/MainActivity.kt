package com.nooro.weathertracker.ui.activity

import android.icu.text.DecimalFormat
import android.icu.text.MessageFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.nooro.weathertracker.R
import com.nooro.weathertracker.data.remote.model.response.weatherData.WeatherDataResponse
import com.nooro.weathertracker.ui.theme.AntiFlashWhite
import com.nooro.weathertracker.ui.theme.CharlestonGreen
import com.nooro.weathertracker.ui.theme.CommonMaterialTheme
import com.nooro.weathertracker.ui.theme.SilverSand
import com.nooro.weathertracker.ui.theme.SpanishGray
import com.nooro.weathertracker.ui.theme.font
import com.nooro.weathertracker.ui.theme.getBackgroundColor
import com.nooro.weathertracker.ui.theme.getDefaultTextColor
import com.nooro.weathertracker.util.CommonTextField
import com.nooro.weathertracker.util.Constant.Validation.EMPTY
import com.nooro.weathertracker.util.GlideImage
import com.nooro.weathertracker.util.LoaderDialog
import com.nooro.weathertracker.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentView()
        }
    }

    @Composable
    private fun ContentView() {
        val snackBarHostState = remember { SnackbarHostState() }
        val keyboardController = LocalSoftwareKeyboardController.current
        var searchValue by remember { mutableStateOf(TextFieldValue("")) }
        val getSearchValue = searchValue.text.trim()
        var searchError by remember { mutableStateOf("") }
        var searchIsEmpty by remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()
        val listWeatherData by homeViewModel.listWeatherData.collectAsState()
        var weatherResponseData by remember { mutableStateOf(WeatherDataResponse()) }
        var isLoading by remember { mutableStateOf(false) }
        val lazyListState = rememberLazyListState()
        val decimalFormat = DecimalFormat("##.##")

        CommonMaterialTheme(snackBarHost = { SnackbarHost(hostState = snackBarHostState) }) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(paddingValues), color = getBackgroundColor()
            ) {
                InitObserver(snackBarHostState) {
                    weatherResponseData = it
                }
                Box {
                    if (searchIsEmpty && listWeatherData.isEmpty()) {
                        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(stringResource(R.string.no_city_selected), style = font(30.sp, fontWeight = FontWeight.SemiBold))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(stringResource(R.string.please_search_for_a_city), style = font(15.sp, fontWeight = FontWeight.SemiBold))
                        }
                    }
                    Column {
                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        CommonTextField(stringResource(R.string.search_location), keyboardController, searchValue, searchError, supportingText = {
                            if (searchError.isNotEmpty() && searchError == EMPTY) {
                                searchIsEmpty = true
                            }
                        }, onValueChange = {
                            weatherResponseData = WeatherDataResponse()
                            searchIsEmpty = false
                            searchError = ""
                            searchValue = it
                            searchIsEmpty = searchValue.text.isEmpty() || searchValue.text.isBlank()
                        }, onTrailingIconClick = {
                            scope.launch {
                                isLoading = true
                                homeViewModel.getData(getSearchValue)
                                isLoading = false
                            }
                        })
                        if (searchIsEmpty && listWeatherData.isNotEmpty() && weatherResponseData.location == null) {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(top = 16.dp),
                                state = lazyListState
                            ) {
                                items(listWeatherData) { weatherData ->
                                    WeatherDataItem(decimalFormat, weatherData, onClick = {
                                        searchIsEmpty = false
                                        weatherResponseData = weatherData
                                    })
                                }
                            }
                        }
                    }
                    if (!searchIsEmpty && weatherResponseData.current != null && weatherResponseData.location != null) {
                        FullViewWeatherData(weatherResponseData, decimalFormat)
                    }
                    LoaderDialog(isVisible = isLoading)
                }
            }
        }
    }

    @Composable
    private fun FullViewWeatherData(weatherResponseData: WeatherDataResponse, decimalFormat: DecimalFormat) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                GlideImage(
                    url = weatherResponseData.current?.condition?.icon,
                    contentDescription = weatherResponseData.current?.condition?.text ?: "",
                    modifier = Modifier.fillMaxHeight(0.2f)
                )
                Spacer(modifier = Modifier.height(27.dp))
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (textRef, imageRef) = createRefs()
                    Text(weatherResponseData.location?.name ?: "", style = font(30.sp, fontWeight = FontWeight.SemiBold),
                       modifier = Modifier.constrainAs(textRef) {
                        // Center the text horizontally in the parent
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                    Image(
                        painter = painterResource(R.drawable.ic_navigation),
                        contentDescription = null,
                        modifier = Modifier.padding(top = 2.dp).constrainAs(imageRef) {
                            // Align the image to the right of the text
                            start.linkTo(textRef.end, margin = 6.dp) // Margin between text and image
                            top.linkTo(textRef.top)
                            bottom.linkTo(textRef.bottom)
                        },
                        colorFilter = ColorFilter.tint(getDefaultTextColor())
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (textCRef, imageCRef) = createRefs()
                    Text(
                        text = decimalFormat.format(weatherResponseData.current?.dewpoint_c) ?: "", style = font(
                            70.sp, fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.constrainAs(textCRef) {
                            // Center the text horizontally in the parent
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_degree),
                        contentDescription = null,
                        modifier = Modifier.size(8.dp).constrainAs(imageCRef) {
                            // Align the image to the right of the text
                            start.linkTo(textCRef.end, margin = 6.dp) // Margin between text and image
                            top.linkTo(textCRef.top, margin = 20.dp)
                        },
                        colorFilter = ColorFilter.tint(getDefaultTextColor())
                    )
                }
                Spacer(modifier = Modifier.height(36.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = AntiFlashWhite),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 50.dp)
                        .padding(top = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(16.dp)) {
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.humidity), style = font(12.sp, fontWeight = FontWeight.Medium, color = SilverSand)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val humidity = weatherResponseData.current?.humidity ?: 0
                            println("humidity--> $humidity")
                            Text(
                                text = "$humidity%", style = font(
                                    15.sp, fontWeight = FontWeight.Medium, color = SpanishGray
                                )
                            )
                        }
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.uv), style = font(12.sp, fontWeight = FontWeight.Medium, color = SilverSand)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            val uv = decimalFormat.format(weatherResponseData.current?.uv ?: 0.0)
                            println("uv--> $uv")
                            Text(
                                text = uv, style = font(15.sp, fontWeight = FontWeight.Medium, color = SpanishGray)
                            )
                        }
                        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.feels_like), style = font(8.sp, fontWeight = FontWeight.Medium, color = SilverSand)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                val formatter = MessageFormat("{0,ordinal}", Locale("es", "ES"))
                                val ordinalValue = formatter.format(arrayOf(weatherResponseData.current?.feelslike_c?:0.0)).replace(".","") // "123º"
                                Text(
                                    text = ordinalValue, style = font(
                                        15.sp, fontWeight = FontWeight.Medium, color = SpanishGray
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun WeatherDataItem(decimalFormat: DecimalFormat, weatherData: WeatherDataResponse, onClick: (WeatherDataResponse) -> Unit) {

        Card(
            colors = CardDefaults.cardColors(containerColor = AntiFlashWhite),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp)
                .clickable {
                    onClick(weatherData)
                }
        ) {
            Box(modifier = Modifier.padding(horizontal = 31.dp, vertical = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Column(modifier = Modifier.wrapContentSize()) {
                            Text(text = weatherData.location?.name ?: "", style = font(20.sp, fontWeight = FontWeight.SemiBold, color = CharlestonGreen))
                            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                Text(
                                    text = decimalFormat.format(weatherData.current?.dewpoint_c) ?: "", style = font(
                                        60.sp, fontWeight = FontWeight.Medium, color = CharlestonGreen
                                    )
                                )
                                Image(
                                    painter = painterResource(R.drawable.ic_degree),
                                    contentDescription = null,
                                    modifier = Modifier.padding(start = 6.dp, top = 20.dp)
                                )
                            }
                        }
                    }
                    GlideImage(
                        url = weatherData.current?.condition?.icon,
                        contentDescription = weatherData.current?.condition?.text ?: "",
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .padding(start = 16.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun InitObserver(snackBarHostState: SnackbarHostState, onDataLoad: (WeatherDataResponse) -> Unit) {
        LaunchedEffect(Unit) {
            combine(homeViewModel.weatherData, homeViewModel.error) { weather, error ->
                if (!error.message.isNullOrEmpty() && error.message.isNotBlank()) {
                    snackBarHostState.showSnackbar(
                        message = error.message.toString(),
                        duration = SnackbarDuration.Short
                    )
                }
                if (weather.location != null && weather.current != null) {
                    homeViewModel.updateWeatherList(weather)
                    onDataLoad(weather)
                }
            }.collect {}
        }
    }
}