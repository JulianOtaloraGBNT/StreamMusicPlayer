package com.julianotalora.musicplayer.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay

suspend inline fun <R> runCatchingWithMinimumTime(time: Long = 2000, block: () -> R): Result<R> {
    val result: Result<R>

    val measuredTime = measureTimeMillis {
        result = try {
            Result.success(block())
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    if (measuredTime < time) {
        delay(time - measuredTime)
    }

    return result
}

fun Context.isPermissionAskedForFirstTime(
    permission: String
): Boolean {

    return getSharedPreferences(
        packageName, MODE_PRIVATE
    ).getBoolean(permission, true)
}

fun Context.permissionAskedForFirsTime(
    permission: String
) {
    getSharedPreferences(
        packageName, MODE_PRIVATE
    ).edit().putBoolean(permission, false).apply()
}

fun Context.openApplicationSettings() {
    startActivity(Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.parse("package:${packageName}")
    })
}