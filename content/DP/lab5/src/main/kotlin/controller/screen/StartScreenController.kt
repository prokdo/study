package controller.screen


import model.database.entity.EntityType
import view.terminal.Terminal


class StartScreenController : ScreenController() {
    fun verifyChoice(choice: String?): Boolean {
        if (choice == null) return false

        if (choice.isBlank() || choice.isEmpty()) return false

        when(choice) {
            "1", "2", "3" -> return true
            else -> return false
        }
    }
}