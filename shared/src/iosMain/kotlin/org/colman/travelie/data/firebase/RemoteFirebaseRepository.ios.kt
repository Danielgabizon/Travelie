package org.colman.travelie.data.firebase

import dev.gitlive.firebase.storage.Data
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create

actual class PlatformData actual constructor(private val bytes: ByteArray) {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    actual fun toGitLiveData(): Data =
        bytes.usePinned { pinned ->
            val nsData: NSData = NSData.create(
                bytes = pinned.addressOf(0),
                length = bytes.size.toULong()
            )
            Data(nsData)
        }
}