package com.wisnu.kurniawan.wallee.runtime.initializer

import android.content.Context
import androidx.startup.Initializer
import com.wisnu.foundation.coreloggr.Loggr
import com.wisnu.foundation.testdebug.DebugTools

/**
 * Initializes local/debug logging for the AS Team build.
 *
 * Upstream Firebase Crashlytics logging is intentionally not registered here. Production
 * crash reporting can be connected later to an AS Team-owned service without leaking data
 * to the upstream Firebase project.
 */
class LoggrInitializer : Initializer<Loggr> {
    override fun create(context: Context): Loggr {
        val loggings = DebugTools.getLoggings().toMutableList()
        Loggr.initialize(loggings)
        return Loggr
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
