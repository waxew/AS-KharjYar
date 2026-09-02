package com.wisnu.kurniawan.wallee.features.drawer.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.wallee.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val SUPPORT_EMAIL = "AS.Developers.Support@Gmail.Com"
private const val PROFILE_PREFS = "as_drawer_profile"
private const val PROFILE_URI_KEY = "profile_uri"
private const val PROFILE_NAME_KEY = "display_name"
private const val PROFILE_IMAGE_MAX_SIZE = 512
private const val PROFILE_NAME_MAX_LENGTH = 40

/**
 * Shared AS Team navigation drawer for KharjYar.
 *
 * Product-level navigation stays separate from the expense database flow. The drawer itself is
 * always RTL so it opens from the physical right side, while business content follows the active
 * application locale. Profile photo and display name are persisted only on this device and are
 * never uploaded by this component.
 */
@Composable
fun AsNavigationDrawer(
    onHome: () -> Unit,
    onSettings: () -> Unit,
    onTheme: () -> Unit,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val appLayoutDirection = LocalLayoutDirection.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showAboutDialog by remember { mutableStateOf(false) }

    val appTitle = stringResource(R.string.app_name)
    val shareText = stringResource(R.string.as_drawer_share_text, appTitle)
    val shareChooserTitle = stringResource(R.string.as_drawer_share_chooser, appTitle)
    val openMenuDescription = stringResource(R.string.as_drawer_open)

    val versionName = remember(context.packageName) {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull().orEmpty().ifBlank { "—" }
    }

    // Android Back closes the drawer first; it must not exit the app while the menu is visible.
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    fun closeThen(action: () -> Unit) {
        scope.launch {
            drawerState.close()
            action()
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(max = 328.dp),
                ) {
                    DrawerHeader(versionName = versionName)

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    Spacer(Modifier.height(8.dp))

                    // AS Team global drawer contract: Settings = index 0, Share = index 1.
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.as_drawer_settings)) },
                        selected = false,
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        onClick = { closeThen(onSettings) },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.as_drawer_share)) },
                        selected = false,
                        icon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = {
                            closeThen {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(
                                    Intent.createChooser(shareIntent, shareChooserTitle)
                                )
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.as_drawer_home)) },
                        selected = false,
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        onClick = { closeThen(onHome) },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.as_drawer_theme)) },
                        selected = false,
                        icon = { Icon(Icons.Default.DarkMode, contentDescription = null) },
                        onClick = { closeThen(onTheme) },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.as_drawer_contact)) },
                        selected = false,
                        icon = { Icon(Icons.Default.Email, contentDescription = null) },
                        onClick = {
                            closeThen {
                                val subject = Uri.encode(appTitle)
                                val mailIntent = Intent(
                                    Intent.ACTION_SENDTO,
                                    Uri.parse("mailto:$SUPPORT_EMAIL?subject=$subject"),
                                )
                                runCatching { context.startActivity(mailIntent) }
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                    NavigationDrawerItem(
                        label = { Text(stringResource(R.string.as_drawer_about)) },
                        selected = false,
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        onClick = {
                            scope.launch { drawerState.close() }
                            showAboutDialog = true
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )

                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(R.string.as_drawer_footer, versionName),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    )
                }
            },
        ) {
            // This parent stays RTL so TopStart is the physical top-right corner.
            Box(modifier = Modifier.fillMaxSize()) {
                CompositionLocalProvider(LocalLayoutDirection provides appLayoutDirection) {
                    content()
                }

                // Unified AS hamburger entry point: fixed at the physical top-right.
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .statusBarsPadding()
                        .padding(top = 8.dp, start = 8.dp),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 4.dp,
                    shadowElevation = 2.dp,
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = openMenuDescription,
                        )
                    }
                }
            }
        }
    }

    if (showAboutDialog) {
        CompositionLocalProvider(LocalLayoutDirection provides appLayoutDirection) {
            AlertDialog(
                onDismissRequest = { showAboutDialog = false },
                confirmButton = {
                    TextButton(onClick = { showAboutDialog = false }) {
                        Text(stringResource(R.string.as_drawer_close))
                    }
                },
                icon = { Icon(Icons.Default.Info, contentDescription = null) },
                title = { Text(appTitle) },
                text = {
                    Text(
                        stringResource(
                            R.string.as_drawer_about_body,
                            appTitle,
                            SUPPORT_EMAIL,
                            versionName,
                        )
                    )
                },
            )
        }
    }
}

@Composable
private fun DrawerHeader(versionName: String) {
    val context = LocalContext.current
    val defaultProfileName = stringResource(R.string.as_drawer_default_user)
    val preferences = remember {
        context.getSharedPreferences(PROFILE_PREFS, Context.MODE_PRIVATE)
    }

    var profileUri by remember {
        mutableStateOf(
            preferences.getString(PROFILE_URI_KEY, null)
                ?.takeIf { it.isNotBlank() }
                ?.let(Uri::parse)
        )
    }
    var displayName by remember(defaultProfileName) {
        mutableStateOf(
            preferences.getString(PROFILE_NAME_KEY, null)
                ?.trim()
                ?.takeIf { it.isNotBlank() }
                ?: defaultProfileName
        )
    }
    var draftName by remember { mutableStateOf("") }
    var showNameDialog by remember { mutableStateOf(false) }

    val profilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION,
                )
            }
            preferences.edit().putString(PROFILE_URI_KEY, uri.toString()).apply()
            profileUri = uri
        }
    }

    val profileBitmap by produceState<ImageBitmap?>(initialValue = null, key1 = profileUri) {
        value = withContext(Dispatchers.IO) {
            profileUri?.let { loadProfileImage(context, it) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            modifier = Modifier
                .size(80.dp)
                .clickable { profilePicker.launch(arrayOf("image/*")) },
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                val bitmap = profileBitmap
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap,
                        contentDescription = stringResource(R.string.as_drawer_profile),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = stringResource(R.string.as_drawer_profile),
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        Text(
            text = displayName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                draftName = displayName
                showNameDialog = true
            },
        )
        Text(
            text = stringResource(R.string.as_drawer_name_hint),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "AS Team",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.as_drawer_version, versionName),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text(stringResource(R.string.as_drawer_edit_name)) },
            text = {
                OutlinedTextField(
                    value = draftName,
                    onValueChange = {
                        if (it.length <= PROFILE_NAME_MAX_LENGTH) {
                            draftName = it
                        }
                    },
                    label = { Text(stringResource(R.string.as_drawer_name_label)) },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val cleanedName = draftName.trim().ifBlank { defaultProfileName }
                        preferences.edit().putString(PROFILE_NAME_KEY, cleanedName).apply()
                        displayName = cleanedName
                        showNameDialog = false
                    }
                ) {
                    Text(stringResource(R.string.as_drawer_save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text(stringResource(R.string.as_drawer_cancel))
                }
            },
        )
    }
}

private fun loadProfileImage(context: Context, uri: Uri): ImageBitmap? {
    return runCatching {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, bounds)
        }

        var sampleSize = 1
        while (
            bounds.outWidth / sampleSize > PROFILE_IMAGE_MAX_SIZE ||
            bounds.outHeight / sampleSize > PROFILE_IMAGE_MAX_SIZE
        ) {
            sampleSize *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        context.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, options)?.asImageBitmap()
        }
    }.getOrNull()
}
