package com.wisnu.kurniawan.wallee.foundation.extension

import android.content.Context
import android.telephony.TelephonyManager
import java.util.*

/**
 * Returns the best available ISO 3166-1 alpha-2 country code for currency recommendation.
 *
 * AS Team fallback order:
 * 1. Current mobile network country.
 * 2. SIM country when mobile network information is unavailable.
 * 3. Device locale for Wi-Fi tablets, emulators, or devices without telephony service.
 *
 * No phone-state permission is requested for this lookup.
 */
fun getCountryCode(context: Context): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager

    val countryCode = sequenceOf(
        telephonyManager?.networkCountryIso,
        telephonyManager?.simCountryIso,
        Locale.getDefault().country,
    )
        .filterNotNull()
        .map { it.trim() }
        .firstOrNull { it.length == 2 }
        .orEmpty()

    return countryCode.uppercase(Locale.US)
}
