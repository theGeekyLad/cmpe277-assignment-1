package com.thegeekylad.cmpe277_1

import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.thegeekylad.cmpe277_1.ui.theme.CMPE277_1Theme

@ExperimentalMaterial3Api
@ExperimentalUnitApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val counter = remember { mutableStateOf(0) }
            val showDialog = remember { mutableStateOf(false) }

            CMPE277_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainComposable(
                        applicationContext,
                        counter,
                        showDialog
                    )
                    if (showDialog.value)
                        MyAlert(
                            showDialog
                        )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CMPE277_1Theme {
        Greeting("Android")
    }
}

@ExperimentalMaterial3Api
@ExperimentalUnitApi
@Composable
fun MainComposable(
    context: Context,
    counter: MutableState<Int>,
    showDialog: MutableState<Boolean>
) {

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                counter.value = counter.value + Integer.parseInt(data.data.toString())
            }
        }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f, true)
        ) {
            TextButton(
                onClick = {
                    val intent = Intent(context, MainActivityB::class.java)
                    launcher.launch(intent)
                },
                colors = ButtonDefaults.buttonColors()) {
                Text("Start activity B")
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f, true)
        ) {
            TextButton(
                onClick = {
                    val intent = Intent(context, MainActivityC::class.java)
                    launcher.launch(intent)
                },
                colors = ButtonDefaults.buttonColors()) {
                Text("Start activity C")
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f, true)
        ) {
            TextButton(
                onClick = {
                    val intent = Intent(context, Assignment2::class.java)
                    launcher.launch(intent)
                },
                colors = ButtonDefaults.buttonColors()) {
                Text("Assignment - 2")
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f, true)
        ) {
            TextButton(
                onClick = { showDialog.value = true },
                colors = ButtonDefaults.buttonColors()) {
                Text("Start dialog")
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f, true)
        ) {
            Text("Thread counter: " + counter.value)
        }
    }
}

@Composable
fun MyAlert(
    showDialog: MutableState<Boolean>
) {
    AlertDialog(
        title = {
            Text("Dialog")
        },
        text = {
            Text("Hey, this is a dialog!")
        },
        confirmButton = {
            TextButton(
                onClick = { showDialog.value = false }
            ) {
                Text("Close")
            }
        },
        onDismissRequest = { showDialog.value = false }
    )
}