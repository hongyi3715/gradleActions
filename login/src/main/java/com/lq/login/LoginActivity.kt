package com.lq.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lib_annotation.AutoWired
import com.example.lib_annotation.Route
import com.example.lib_annotation.data.InterceptorMeta
import com.example.lib_api.HRouter
import com.example.lib_api.HRouterDelegate
import com.example.lib_api.interceptor.InterceptorManager
import com.lq.router.InterceptorRegisterLogin

@Route(path = "/login/login")
class LoginActivity: ComponentActivity() {
    @AutoWired(required = false)
    var userName:String?=null

    @AutoWired(required = true)
    var account :Int?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HRouter.inject(this)

        setContent {
            LoginScreen()
        }
    }

    @Composable
    fun LoginScreen(){
        Column(modifier = Modifier.fillMaxSize().padding(88.dp)) {
            Text(text = "登录信息", fontSize = 28.sp)
            Text("userName: $userName")
            Text("account: $account")
        }
        val interceptors = mutableListOf<InterceptorMeta>()
        InterceptorRegisterLogin.register(interceptors)
        InterceptorManager.addAllInterceptor()

    }
}

