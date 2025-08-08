package org.colman.travelie.data.firebase

import dev.gitlive.firebase.storage.Data

actual class PlatformData actual constructor(private val bytes: ByteArray) {
    actual fun toGitLiveData(): Data = Data(bytes)
}