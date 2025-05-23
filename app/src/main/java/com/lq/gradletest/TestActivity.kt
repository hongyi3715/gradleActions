package com.lq.gradletest

import com.example.lib_annotation.AutoWired
import com.example.lib_annotation.Route

@Route(path = "/main/test")
class TestActivity {

    @AutoWired
     var userName: String?=null
}