package com.example.lib_compiler

import com.google.devtools.ksp.processing.Resolver

interface ProcessorPart {
    fun process(resolver: Resolver)
}