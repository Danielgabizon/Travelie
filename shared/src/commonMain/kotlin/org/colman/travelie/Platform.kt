package org.colman.travelie

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform