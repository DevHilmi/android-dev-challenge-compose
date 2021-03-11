/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.model.MainSurfaceModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@InternalCoroutinesApi
@Composable
fun MyApp() {
    val mainViewModel: MainViewModel = viewModel()
    val mainSurfaceModel: MainSurfaceModel? by mainViewModel.getMainSurfaceModel().observeAsState()
    mainViewModel.resetTimer()
    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            mainSurfaceModel?.let {
                CountDownView(
                    it,
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}

@InternalCoroutinesApi
@Composable
fun CountDownView(
    mainSurfaceModel: MainSurfaceModel,
    mainViewModel: MainViewModel,
) {
    Column {
        lateinit var hourState: LazyListState
        lateinit var minuteState: LazyListState
        lateinit var secondState: LazyListState
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            hourState = countDownChooserText(countDownType = CountDownType.HOURS)
            minuteState =
                countDownChooserText(countDownType = CountDownType.MINUTES)
            secondState =
                countDownChooserText(countDownType = CountDownType.SECONDS)
        }
        CountDownTimerText(
            mainSurfaceModel,
            mainViewModel,
            hour = hourState,
            minute = minuteState,
            second = secondState
        )
    }


}

@Composable
fun CountDownTimerText(
    mainSurfaceModel: MainSurfaceModel,
    mainViewModel: MainViewModel,
    hour: LazyListState,
    minute: LazyListState,
    second: LazyListState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val hourValue by remember {
            derivedStateOf {
                hour.firstVisibleItemIndex
            }
        }
        val minuteValue by remember {
            derivedStateOf {
                minute.firstVisibleItemIndex
            }
        }
        val secondValue by remember {
            derivedStateOf {
                second.firstVisibleItemIndex
            }
        }
        if (!mainSurfaceModel.running) {
            Text(text = hourValue.toString() + "h " + minuteValue.toString() + "m " + secondValue.toString() + "s")
        } else {
            Text(text = mainSurfaceModel.hour + "h " + mainSurfaceModel.minute + "m " + mainSurfaceModel.second + "s")
        }
        TimerButton(mainSurfaceModel.running, mainViewModel, hourValue, minuteValue, secondValue)
    }
}

@InternalCoroutinesApi
@Composable
fun countDownChooserText(countDownType: CountDownType): LazyListState {
    val listState = rememberLazyListState()

    Row(modifier = Modifier.padding(16.dp)) {
        val countdownLimit: Int = when (countDownType) {
            CountDownType.HOURS -> 24
            CountDownType.MINUTES -> 60
            CountDownType.SECONDS -> 60
        }
        val countdownSymbol: String = when (countDownType) {
            CountDownType.HOURS -> "hour"
            CountDownType.MINUTES -> "min"
            CountDownType.SECONDS -> "second"
        }
        Row {
            LazyColumn(
                modifier = Modifier
                    .height(16.dp),
                state = listState
            ) {
                items(countdownLimit) { index ->
                    Text(text = index.toString())
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                listState.scrollToItem(listState.firstVisibleItemIndex, 16)
            }

            Text(text = countdownSymbol)
        }
    }
    return listState
}

@Composable
fun TimerButton(
    running: Boolean,
    mainViewModel: MainViewModel,
    hour: Int,
    minute: Int,
    second: Int
) {
    // TODO (hilmi.rizaldi) : Style button & Button logic (Pause|Stop|Start)
    Button(modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (!running) {
                mainViewModel.startTimer(hour, minute, second)
            }
        }) {
        Text(text = "Start")
    }
}

enum class CountDownType {
    HOURS, MINUTES, SECONDS
}

@InternalCoroutinesApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@InternalCoroutinesApi
@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
