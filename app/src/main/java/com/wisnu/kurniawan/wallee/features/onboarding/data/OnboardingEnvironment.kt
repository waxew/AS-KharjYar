package com.wisnu.kurniawan.wallee.features.onboarding.data

import android.content.Context
import com.wisnu.kurniawan.wallee.foundation.datasource.local.LocalManager
import com.wisnu.kurniawan.wallee.foundation.datasource.preference.PreferenceManager
import com.wisnu.kurniawan.wallee.foundation.extension.defaultAccount
import com.wisnu.kurniawan.wallee.foundation.extension.getCountryCode
import com.wisnu.kurniawan.wallee.foundation.wrapper.DateTimeProvider
import com.wisnu.kurniawan.wallee.model.Currency
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class OnboardingEnvironment @Inject constructor(
    private val localManager: LocalManager,
    private val preferenceManager: PreferenceManager,
    private val dateTimeProvider: DateTimeProvider,
    @ApplicationContext private val context: Context
) : IOnboardingEnvironment {

    override fun getCurrentCountryCode(): Flow<String> {
        return flowOf(getCountryCode(context))
    }

    override suspend fun saveAccount(currency: Currency) {
        // AS Team privacy policy: onboarding data remains local. Creating the first account must
        // not send the selected country or currency to third-party analytics/crash services.
        val account = defaultAccount(currency, dateTimeProvider.now())
        localManager.insertAccount(account)
        preferenceManager.setFinishOnboarding(true)
    }
}
