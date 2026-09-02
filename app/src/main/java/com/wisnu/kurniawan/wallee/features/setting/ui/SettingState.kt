package com.wisnu.kurniawan.wallee.features.setting.ui

import androidx.compose.runtime.Immutable
import com.wisnu.kurniawan.wallee.R

@Immutable
data class SettingState(
    val items: List<SettingItem> = initial()
) {
    companion object {
        private fun initial() = listOf(
            // AS Team settings order: keep core user controls visible first.
            SettingItem.Backup(R.string.setting_backup),
            SettingItem.Theme(R.string.setting_theme),
            SettingItem.Language(R.string.setting_language),
        )
    }
}

sealed class SettingItem(open val title: Int) {
    data class Backup(override val title: Int) : SettingItem(title)
    data class Theme(override val title: Int) : SettingItem(title)
    data class Logout(override val title: Int) : SettingItem(title)
    data class Language(override val title: Int) : SettingItem(title)
}
