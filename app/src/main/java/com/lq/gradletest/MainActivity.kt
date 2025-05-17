package com.lq.gradletest

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lib_annotation.AutoWired
import com.example.lib_annotation.Route
import com.example.lib_api.HRouter
import com.lq.gradletest.ui.theme.GradleTestTheme

@Route(path = "/main/main")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GradleTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Button(onClick = {
        HRouter.build("/main/login")
            .with {
                string("userName", name)
                int("account", 100)
                parameter("parameter","current parameter")
            }.navigate()
    },modifier = Modifier.padding(50.dp,100.dp)) {
        Text(text = "跳转信息")
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GradleTestTheme {
        Greeting("Android")
    }

}