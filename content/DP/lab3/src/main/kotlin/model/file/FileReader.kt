package model.file

import java.io.File

object FileReader {
    fun read(file: File): List<String>? {
        val result = mutableListOf<String>()
        try { file.forEachLine { result.add(it) } }
        catch (ignored: Exception) { return null }

        return result.toList()
    }
}