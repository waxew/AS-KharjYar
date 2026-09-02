package com.wisnu.kurniawan.wallee.features.backup.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wisnu.kurniawan.wallee.R
import com.wisnu.kurniawan.wallee.features.backup.data.BackupContract
import com.wisnu.kurniawan.wallee.features.backup.data.BackupManager
import com.wisnu.kurniawan.wallee.foundation.uicomponent.PgModalBackHeader
import com.wisnu.kurniawan.wallee.foundation.uicomponent.PgModalCell
import com.wisnu.kurniawan.wallee.foundation.uicomponent.PgModalLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * User-controlled local backup surface.
 *
 * Android's Storage Access Framework provides the source/destination documents, so KharjYar does
 * not request broad storage permissions. Restore is always validated and explicitly confirmed.
 */
@Composable
fun BackupScreen(
    onClickBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val manager = remember(context.applicationContext) {
        BackupManager(context.applicationContext)
    }

    var isBusy by remember { mutableStateOf(false) }
    var pendingRestore by remember { mutableStateOf<File?>(null) }
    var messageRes by remember { mutableStateOf<Int?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream"),
    ) { uri ->
        if (uri != null && !isBusy) {
            isBusy = true
            scope.launch {
                manager.exportDatabase(uri)
                    .onSuccess { messageRes = R.string.backup_export_success }
                    .onFailure { messageRes = R.string.backup_export_failed }
                isBusy = false
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null && !isBusy) {
            isBusy = true
            scope.launch {
                manager.stageRestore(uri)
                    .onSuccess { pendingRestore = it }
                    .onFailure { messageRes = R.string.backup_restore_failed }
                isBusy = false
            }
        }
    }

    PgModalLayout(
        title = {
            PgModalBackHeader(
                text = stringResource(R.string.backup_title),
                onClickBack = {
                    if (!isBusy) onClickBack()
                },
            )
        },
        content = {
            item {
                Text(
                    text = stringResource(R.string.backup_scope_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                PgModalCell(
                    onClick = {
                        exportLauncher.launch(defaultBackupFileName())
                    },
                    text = stringResource(R.string.backup_export),
                    enabled = !isBusy,
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
            item {
                PgModalCell(
                    onClick = {
                        importLauncher.launch(arrayOf("application/octet-stream", "*/*"))
                    },
                    text = stringResource(R.string.backup_import),
                    enabled = !isBusy,
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Text(
                    text = if (isBusy) {
                        stringResource(R.string.backup_busy)
                    } else {
                        stringResource(R.string.backup_warning)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        },
    )

    val restoreFile = pendingRestore
    if (restoreFile != null) {
        AlertDialog(
            onDismissRequest = {
                restoreFile.delete()
                pendingRestore = null
            },
            title = { Text(stringResource(R.string.backup_restore_confirm_title)) },
            text = { Text(stringResource(R.string.backup_restore_confirm_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingRestore = null
                        isBusy = true
                        scope.launch {
                            manager.restoreDatabase(restoreFile)
                                .onSuccess {
                                    manager.restartApplication()
                                }
                                .onFailure {
                                    restoreFile.delete()
                                    messageRes = R.string.backup_restore_failed
                                    isBusy = false
                                }
                        }
                    },
                ) {
                    Text(stringResource(R.string.backup_restore_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        restoreFile.delete()
                        pendingRestore = null
                    },
                ) {
                    Text(stringResource(R.string.backup_cancel))
                }
            },
        )
    }

    val statusMessage = messageRes
    if (statusMessage != null) {
        AlertDialog(
            onDismissRequest = { messageRes = null },
            text = { Text(stringResource(statusMessage)) },
            confirmButton = {
                TextButton(onClick = { messageRes = null }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
        )
    }
}

private fun defaultBackupFileName(): String {
    val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
    return "KharjYar-$timestamp${BackupContract.BACKUP_EXTENSION}"
}
