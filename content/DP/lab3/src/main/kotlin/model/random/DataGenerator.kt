package model.random

import model.database.DataBase
import model.database.access.AccessType
import model.database.entity.File
import model.database.entity.User
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.random.Random

object DataGenerator {
    private val USERNAMES = arrayOf(
       "Dmitry",
        "Elena",
        "Egor",
        "Diana",
        "Alexander",
        "Maria",
        "Oleg",
        "Zoya",
        "Igor",
        "Olga",
        "Maxim",
        "Ekaterina",
        "Nikita",
        "Sophia",
        "Ilya",
        "Larisa",
        "Roman",
        "Anastasia",
        "Boris",
        "Nadezda")

    fun generate(
        path: String,
        filesNumber: Int,
        usersNumber: Int) {
        if (!Path("$path/database").exists()) {
            Path("$path/database").createDirectories()
            Path("$path/database/users").createDirectory()
            Path("$path/database/files").createDirectory()
        }
        else {
            java.io.File("$path/database/files").deleteRecursively()
            java.io.File("$path/database/users").deleteRecursively()

            Path("$path/database/users").createDirectory()
            Path("$path/database/files").createDirectory()
        }

        if (!Path("$path/resource").exists()) Path("$path/resource").createDirectories()
        else {
            java.io.File("$path/resource").deleteRecursively()

            Path("$path/resource").createDirectory()
        }

        val database = DataBase("$path/database", "$path/resource")

        for (i in 0 ..< filesNumber) {
            database.addEntity(File(
                "File$i.txt",
                Random.nextInt(AccessType.PUBLIC, AccessType.TOP_SECRET + 1)))
        }

        val freeNames = USERNAMES.map { _ -> true }.toTypedArray()

        for (i in 0 ..< usersNumber) {
            val name: String
            while (true) {
                val nameIndex = Random.nextInt(USERNAMES.size)

                if (!freeNames[nameIndex]) continue

                freeNames[nameIndex] = false

                name = USERNAMES[nameIndex]
                break
            }

            database.addEntity(User(
                name,
                Random.nextInt(AccessType.PUBLIC, AccessType.TOP_SECRET + 1)))
        }
    }
}