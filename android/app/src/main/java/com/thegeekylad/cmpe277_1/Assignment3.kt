package com.thegeekylad.cmpe277_1

import android.os.AsyncTask
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.google.gson.annotations.SerializedName
import com.thegeekylad.cmpe277_1.ui.theme.CMPE277_1Theme
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.net.URL

@ExperimentalUnitApi
@ExperimentalMaterial3Api
class Assignment3 : ComponentActivity() {

    val response = mutableStateOf("")
    val request = mutableStateOf("")
    val isRequestInProgress = mutableStateOf(false)

    var apiCallTask: ChatGptTask? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CMPE277_1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.weight(1f, true),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Android ChatGPT App",
                                fontSize = TextUnit(20f, TextUnitType.Sp)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(3f, true),
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Text(
                                text = "Prompt:"
                            )
                            TextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = request.value,
                                onValueChange = { request.value = it }
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f, true),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    apiCallTask = ChatGptTask()
                                    apiCallTask!!.execute()
                                },
                                colors = ButtonDefaults.buttonColors()
                            ) {
                                Text(text = "Send")
                            }
                            TextButton(
                                onClick = {
                                    apiCallTask!!.cancel(true)
                                    isRequestInProgress.value = false
                                }
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                        Column(
                            modifier = Modifier.weight(4f, true),
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Text(
                                text = "Response:"
                            )
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.primaryContainer,
                            ) {
                                if (isRequestInProgress.value)
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = Color.Transparent
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                Text(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .verticalScroll(
                                            rememberScrollState()
                                        ),
                                    text = response.value
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun callChatGpt(prompt: String): Call<ResponseChatCompletions> {
        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openai.com/v1/")
            .build()

        val openApiService = retrofit.create(OpenApiService::class.java)

        return openApiService.prompt(
            RequestPrompt(
                "gpt-3.5-turbo",
                arrayOf(
                    RequestMessages(
                        "user",
                        prompt
                    )
                ),
                0.7
            )
        )
    }

    interface OpenApiService {
        @Headers("Authorization: Bearer <api_key>")  // TODO paste openai api key
        @POST("chat/completions")
        fun prompt(@Body requestPrompt: RequestPrompt): Call<ResponseChatCompletions>

    }

    data class RequestPrompt(
        val model: String,
        val messages: Array<RequestMessages>,
        val temperature: Double
    )

    data class RequestMessages(
        val role: String,
        val content: String,
    )

    data class ResponseChatCompletions(
        val id: String,
        @SerializedName("object") val obj: String,
        val created: Long,
        val model: String,
        val choices: Array<ResponseMessages>,
        val usage: Usage
    )

    data class ResponseMessages(
        val index: Int,
        val message: ResponseMessage
    )

    data class ResponseMessage(
        val role: String,
        val content: String,
        @SerializedName("finish_reason") val finishReason: String,
    )

    data class Usage (
        @SerializedName("prompt_tokens") val promptTokens: Int,
        @SerializedName("completion_tokens") val completionTokens: Int,
        val totalTokens: Int,
    )

    inner class ChatGptTask: AsyncTask<URL, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            isRequestInProgress.value = true
            response.value = ""
        }
        override fun doInBackground(vararg params: URL?): String {
            val responseChatCompletions = callChatGpt(request.value)
            return responseChatCompletions.execute().body()!!.choices[0].message.content
        }

        override fun onProgressUpdate(vararg values: String?) {
            // TODO anything?
        }

        override fun onPostExecute(result: String?) {
            response.value = result!!
            isRequestInProgress.value = false
        }
    }
}