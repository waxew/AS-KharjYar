package com.wisnu.kurniawan.wallee.foundation.extension

import com.wisnu.kurniawan.wallee.foundation.datasource.preference.model.LanguagePreference
import com.wisnu.kurniawan.wallee.model.Language

/**
 * Converts the persisted language code to the app language model.
 * Persian is the safe default for empty/new preferences in the AS Team build.
 */
fun LanguagePreference.toLanguage(): Language {
    return when (this.code) {
        Language.PERSIAN.code -> Language.PERSIAN
        Language.ENGLISH.code -> Language.ENGLISH
        Language.INDONESIA.code -> Language.INDONESIA
        else -> Language.PERSIAN
    }
}
