package com.wisnu.kurniawan.wallee.features.update.data

import android.content.Context
import android.os.Build
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val LATEST_RELEASE_API = "https://api.github.com/repos/waxew/AS-KharjYar/releases/latest"
private const val RELEASES_PAGE = "https://github.com/waxew/AS-KharjYar/releases"

/**
 * Release tag contract used by AS-KharjYar.
 *
 * Public releases must use `v<versionName>+<versionCode>`; for example `v1.2.0+12`.
 * The explicit versionCode lets the app compare Android upgrade order instead of guessing from
 * human-readable version names.
 */
data class ReleaseVersion(
    val versionName: String,
    val versionCode: Long,
)

internal object ReleaseVersionParser {
    private val tagPattern = Regex("^v?([0-9]+(?:\\.[0-9]+){1,3})\\+([0-9]+)$")

    fun parse(tag: String): ReleaseVersion? {
        val match = tagPattern.matchEntire(tag.trim()) ?: return null
        return ReleaseVersion(
            versionName = match.groupValues[1],
            versionCode = match.groupValues[2].toLongOrNull() ?: return null,
        )
    }
}

sealed interface UpdateCheckResult {
    data class UpdateAvailable(
        val release: ReleaseVersion,
        val releaseUrl: String,
    ) : UpdateCheckResult

    data object UpToDate : UpdateCheckResult
    data object NoPublishedRelease : UpdateCheckResult
    data object Failed : UpdateCheckResult
}

/**
 * Lightweight GitHub release checker. No analytics, account token, Retrofit or OkHttp is used.
 * Only the public latest-release endpoint is contacted after the user taps Check for updates.
 */
class UpdateChecker(
    private val context: Context,
) {
    suspend fun check(): UpdateCheckResult = withContext(Dispatchers.IO) {
        runCatching {
            val current = currentVersion()
            val connection = (URL(LATEST_RELEASE_API).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 8_000
                readTimeout = 8_000
                setRequestProperty("Accept", "application/vnd.github+json")
                setRequestProperty("User-Agent", "AS-KharjYar/${current.versionName}")
            }

            try {
                when (connection.responseCode) {
                    HttpURLConnection.HTTP_NOT_FOUND -> UpdateCheckResult.NoPublishedRelease
                    in 200..299 -> {
                        val payload = connection.inputStream.bufferedReader().use { it.readText() }
                        val tag = extractJsonString(payload, "tag_name")
                            ?: return@runCatching UpdateCheckResult.Failed
                        val release = ReleaseVersionParser.parse(tag)
                            ?: return@runCatching UpdateCheckResult.Failed
                        val releaseUrl = extractJsonString(payload, "html_url") ?: RELEASES_PAGE

                        if (release.versionCode > current.versionCode) {
                            UpdateCheckResult.UpdateAvailable(release, releaseUrl)
                        } else {
                            UpdateCheckResult.UpToDate
                        }
                    }
                    else -> UpdateCheckResult.Failed
                }
            } finally {
                connection.disconnect()
            }
        }.getOrDefault(UpdateCheckResult.Failed)
    }

    @Suppress("DEPRECATION")
    private fun currentVersion(): ReleaseVersion {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
        return ReleaseVersion(
            versionName = packageInfo.versionName.orEmpty(),
            versionCode = versionCode,
        )
    }

    private fun extractJsonString(json: String, key: String): String? {
        val escapedKey = Regex.escape(key)
        val match = Regex("\\\"$escapedKey\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"")
            .find(json)
            ?: return null
        return match.groupValues[1]
            .replace("\\/", "/")
            .replace("\\\"", "\"")
            .replace("\\\\", "\\")
    }
}
