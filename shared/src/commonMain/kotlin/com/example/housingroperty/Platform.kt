package com.example.housingroperty

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform