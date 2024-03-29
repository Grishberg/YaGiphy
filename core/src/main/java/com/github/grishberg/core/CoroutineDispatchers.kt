package com.github.grishberg.core

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}