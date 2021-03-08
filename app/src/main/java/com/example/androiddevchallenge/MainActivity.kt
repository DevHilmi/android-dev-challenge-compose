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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.model.MainSurfaceModel
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

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
            CountDownChooserView(
                mainSurfaceModel?.hour,
                mainSurfaceModel?.minute,
                mainSurfaceModel?.second
            )

            TimerButton(false, mainViewModel)
        }
    }
}

@Composable
fun CountDownChooserView(hour: String?, minute: String?, second: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CountDownChooserText(message = hour, countDownType = CountDownType.HOURS)
        CountDownChooserText(message = minute, countDownType = CountDownType.MINUTES)
        CountDownChooserText(message = second, countDownType = CountDownType.SECONDS)
    }
}

@Composable
fun CountDownView(hour: String?, minute: String?, second: String?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

    }
}

@Composable
fun CountDownChooserText(message: String?, countDownType: CountDownType) {
    Row(modifier = Modifier.padding(16.dp)) {
        message?.let {
            val countdownLimit: Int = when (countDownType) {
                CountDownType.HOURS -> 23
                CountDownType.MINUTES -> 59
                CountDownType.SECONDS -> 59
            }
            val countdownSymbol: String = when (countDownType) {
                CountDownType.HOURS -> "hour"
                CountDownType.MINUTES -> "min"
                CountDownType.SECONDS -> "second"
            }
            Row {
                Column(
                    modifier = Modifier
                        .height(150.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    for (i in 0..countdownLimit) {
                        Text(text = i.toString())
                    }
                }
                Text(text = countdownSymbol)
            }

        }
    }
}

@Composable
fun TimerButton(running: Boolean, mainViewModel: MainViewModel) {
    // TODO (hilmi.rizaldi) : Style button & Button logic (Pause|Stop|Start)
    Button(modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (!running) {
                mainViewModel.startTimer(1, 1, 1)
            }
        }) {}
}

enum class CountDownType {
    HOURS, MINUTES, SECONDS
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
