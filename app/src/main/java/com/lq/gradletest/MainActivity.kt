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
import com.lq.lib_annotation.Route
import com.lq.lib_annotation.RouteDeepLink
import com.lq.lib_api.HRouter
import com.lq.gradletest.ui.theme.GradleTestTheme
import kotlinx.coroutines.launch

@Route(path = "/main/main")
@RouteDeepLink(["myapp://main","http://www.baidu.com/main"])
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
    val context = LocalContext.current
    Button(onClick = {
        toTest(context)
    },modifier = Modifier.padding(50.dp,100.dp)) {
        Text(text = "跳转信息")
    }
}

fun toLogin(context: Context){
    HRouter.build("/main/test")
        .withContext(context)
        .withParams {
            string("userName", "Android")
            int("account", 100)
            parameter("parameter","current parameter")
        }.navigate()
}

fun toTest(context: Context){
    HRouter.buildUri("myapp://login").withContext(context).navigate()
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GradleTestTheme {
        Greeting("Android")
    }

}