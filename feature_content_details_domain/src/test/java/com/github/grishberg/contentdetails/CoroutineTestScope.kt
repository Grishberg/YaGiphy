package com.github.grishberg.contentdetails

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Helper Coroutine test rule for avoiding mem leaks.
 */
class CoroutineTestScope : CoroutineScope {
    override val coroutineContext: CoroutineContext = ImmediatelyCoroutineDispatcher()
}