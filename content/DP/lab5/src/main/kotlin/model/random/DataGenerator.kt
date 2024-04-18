package model.random


import java.io.File

import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.createFile
import kotlin.random.Random


object DataGenerator {
    private const val ALPHABET = "0123456789!@#$%^&*-_=+aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ" 

    operator fun invoke(path: String) {
        if (!Path("$path/database").exists()) {
            Path("$path/database").createDirectories()
            Path("$path/database/users").createDirectory()
            Path("$path/database/phrases").createDirectory()
        }
        else {
            File("$path/database/users").deleteRecursively()
            File("$path/database/phrases").deleteRecursively()

            Path("$path/database/users").createDirectory()
            Path("$path/database/phrases").createDirectory()
        }

        val phrases = Array<String>(101) { "" }

        for (i in phrases.indices) {
            val phraseBuilder = StringBuilder()

            IntRange(0, 24).forEach { phraseBuilder.append(ALPHABET.random()) }

            phrases[i] = phraseBuilder.toString()
        }

        phrases.forEach { phrase -> File("$path/database/phrases/${phrase.hashCode()}").writeText(phrase) }
    }
}