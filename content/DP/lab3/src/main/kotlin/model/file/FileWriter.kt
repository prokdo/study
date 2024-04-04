package model.file

import java.io.File

object FileWriter {
    fun write(file: File, content: List<String>): Boolean {
        try { file.writeText("") }
        catch (ignored: Exception) { return false }

        try { content.forEach { file.appendText(it.toString() + "\n") } }
        catch (ignored: Exception) { return false }

        return true
    }
}