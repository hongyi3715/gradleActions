package com.lq.gradletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lib_api.HRouter
import com.example.lib_api.deeplink.DeepLinkManager

class DeepLinkDispatcherActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DispatcherScreen()
        }
        getInfo()
    }

    private fun getInfo(){
        val uri = intent?.data
        if(uri!=null){
            val path  = DeepLinkManager.getPathFromUri(uri.toString())
            if(!path.isNullOrEmpty()){
                HRouter.build(path).withContext(this).navigate()
            }
        }
    }
}

@Composable
fun DispatcherScreen(){
    @Composable
    fun TestScreen(){
        Column(modifier = Modifier.fillMaxSize().padding(128.dp)) {
            Text(text = "DeepLink", fontSize = 48.sp)
        }
    }
}