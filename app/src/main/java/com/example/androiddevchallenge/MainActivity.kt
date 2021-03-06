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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
    Surface(color = MaterialTheme.colors.background) {
        Column {
            TopAppBar(
                title = { Text(text = "Timer") }
            )
            Text(text = "Ready... Set... GO!")

            CountDownView(
                mainSurfaceModel?.hour,
                mainSurfaceModel?.minute,
                mainSurfaceModel?.second
            )

            Button(onClick = { mainViewModel.startTimer(1, 1, 60) }) {}
        }
    }
}

@Composable
fun CountDownView(hour: String?, minute: String?, second: String?) {
    Row {
        CountDownText(message = hour, countDownType = CountDownType.HOURS)
        CountDownText(message = minute, countDownType = CountDownType.MINUTES)
        CountDownText(message = second, countDownType = CountDownType.SECONDS)
    }
}

@Composable
fun CountDownText(message: String?, countDownType: CountDownType) {
    Row {
        message?.let {
            Text(text = it)
            val countdownSymbol: String = when (countDownType) {
                CountDownType.HOURS -> "h"
                CountDownType.MINUTES -> "m"
                CountDownType.SECONDS -> "s"
            }
            Text(text = countdownSymbol)
        }
    }
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
