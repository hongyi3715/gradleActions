package com.lq.gradletest

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.lib_annotation.Route
import com.example.lib_annotation.RouteDeepLink
import com.example.lib_api.HRouter
import com.lq.gradletest.ui.theme.GradleTestTheme
import kotlinx.coroutines.launch

@Route(path = "/main/main")
@RouteDeepLink(["myapp://login","http://www.baidu.com/login"])
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


    fun mockInfo(){
        lifecycleScope.launch {

        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(onClick = {
        toLogin(context)
    },modifier = Modifier.padding(50.dp,100.dp)) {
        Text(text = "跳转信息")
    }
}

fun toLogin(context: Context){

    HRouter.build("/login/login")
        .withContext(context)
        .with {
            string("userName", "Android")
            int("account", 100)
            parameter("parameter","current parameter")
        }.navigate()
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GradleTestTheme {
        Greeting("Android")
    }

}