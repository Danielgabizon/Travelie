package org.colman.travelie.utils

import android.content.Context
import android.net.Uri

fun loadImageData(context: Context, uri: Uri?): Pair<ByteArray?, String?> {
    if (uri == null) return null to null
    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
    val mime = context.contentResolver.getType(uri) ?: "image/jpeg"
    return bytes to mime
}