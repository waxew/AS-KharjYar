package com.wisnu.kurniawan.wallee.features.update.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ReleaseVersionParserTest {

    @Test
    fun parses_release_tag_with_version_code() {
        val result = ReleaseVersionParser.parse("v1.2.0+12")

        assertEquals("1.2.0", result?.versionName)
        assertEquals(12L, result?.versionCode)
    }

    @Test
    fun rejects_tags_without_version_code() {
        assertNull(ReleaseVersionParser.parse("v1.2.0"))
    }

    @Test
    fun rejects_non_numeric_version_code() {
        assertNull(ReleaseVersionParser.parse("v1.2.0+beta"))
    }
}
