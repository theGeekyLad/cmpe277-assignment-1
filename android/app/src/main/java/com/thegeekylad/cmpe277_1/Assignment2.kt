package com.thegeekylad.cmpe277_1

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.thegeekylad.cmpe277_1.ui.theme.CMPE277_1Theme

@ExperimentalMaterial3Api
class Assignment2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WebLinkAndPhoneCallApp(applicationContext = applicationContext) {
                finish()
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun WebLinkAndPhoneCallApp(applicationContext: Context, onClose: () -> Unit) {
    val url = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    CMPE277_1Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    modifier = Modifier.weight(3f, true)
                ) {
                    TextField(
                        value = url.value,
                        onValueChange = { url.value = it },
                        placeholder = {
                            Text(text = "URL")
                        }
                    )
                    TextButton(
                        onClick = {
                            if (TextUtils.isEmpty(url.value))
                                Toast.makeText(applicationContext, "URL cannot be empty!", Toast.LENGTH_LONG).show()
                            else {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.value))
                                intent.resolveActivity(applicationContext.packageManager)
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                                applicationContext.startActivity(intent)
                            }
                        },
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(text = "Launch")
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    modifier = Modifier.weight(1f, true)
                ) {
                    Divider(
                        thickness = 1.dp,
                        color = Color.Gray,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    modifier = Modifier.weight(3f, true)
                ) {
                    TextField(
                        value = phoneNumber.value,
                        onValueChange = { phoneNumber.value = it },
                        placeholder = {
                            Text(text = "Phone")
                        }
                    )
                    TextButton(
                        onClick = {
                            if (TextUtils.isEmpty(phoneNumber.value))
                                Toast.makeText(applicationContext, "Phone number cannot be empty!", Toast.LENGTH_LONG).show()
                            else {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${phoneNumber.value}")
                                }
                                intent.resolveActivity(applicationContext.packageManager)
                                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                                applicationContext.startActivity(intent)
                            }
                        },
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(text = "Ring")
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    modifier = Modifier.weight(1f, true)
                ) {
                    Divider(
                        thickness = 1.dp,
                        color = Color.Gray,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                    modifier = Modifier.weight(2f, true)
                ) {
                    TextButton(
                        onClick = {
                            onClose()
                        },
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(text = "Close App")
                    }
                }
            }
        }
    }
}