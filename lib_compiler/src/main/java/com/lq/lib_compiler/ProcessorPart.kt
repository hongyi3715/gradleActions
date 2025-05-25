package com.lq.lib_compiler

import com.google.devtools.ksp.processing.Resolver

internal interface ProcessorPart {
    fun process(resolver: Resolver)
}