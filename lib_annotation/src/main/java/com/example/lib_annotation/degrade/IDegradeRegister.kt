package com.example.lib_annotation.degrade

import com.example.lib_annotation.data.DegradeMeta

interface IDegradeRegister {

    fun register(list: MutableList<DegradeMeta>)
}