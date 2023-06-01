package com.julianotalora.musicplayer.common

import android.net.Uri

class Destination(
    var destination: String
) {
    fun destinationWithArguments(vararg arguments: Pair<String, Any>): String {
        return with(Uri.Builder()) {
            path(destination)
            arguments.forEach {
                appendQueryParameter(it.first, it.second.toString())
            }
            build().toString()
        }
    }
}