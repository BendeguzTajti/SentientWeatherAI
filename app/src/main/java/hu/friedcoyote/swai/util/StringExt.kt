package hu.friedcoyote.swai.util

import java.util.*

fun String.toCityName(): String =
    this.replace("  ", " ")
        .split(" ")
        .joinToString(" ") { word ->
            word.lowercase()
                .replaceFirstChar { letter -> letter.titlecase(Locale.getDefault()) }
        }