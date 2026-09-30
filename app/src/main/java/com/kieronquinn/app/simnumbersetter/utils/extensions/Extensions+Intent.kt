package com.kieronquinn.app.simnumbersetter.utils.extensions

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.kieronquinn.app.simnumbersetter.BuildConfig

private const val KEY_PENDING_INTENT = "SECURITY_PENDING_INTENT"
private const val PENDING_INTENT_REQUEST_CODE = 1001

fun Intent.applySecurity(context: Context) {
    putExtra(
        KEY_PENDING_INTENT, PendingIntent.getActivity(
        context,
        PENDING_INTENT_REQUEST_CODE,
        Intent(),
        PendingIntent.FLAG_IMMUTABLE
    ))
}

fun Intent.checkSecurity(context: Context): Boolean {
    val pendingIntent = getParcelableExtra<PendingIntent>(KEY_PENDING_INTENT) ?: return false
    val creatorUid = pendingIntent.creatorUid
    val selfUid = context.packageManager.getApplicationInfo(
        BuildConfig.APPLICATION_ID, 0
    ).uid
    return creatorUid == selfUid
}
