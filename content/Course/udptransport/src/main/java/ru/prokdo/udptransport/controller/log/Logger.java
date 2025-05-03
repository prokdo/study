package ru.prokdo.udptransport.controller.log;

import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.TextInputControl;

public final class Logger {
    private TextInputControl logArea;

    public Logger(TextInputControl logArea) {
        this.logArea = Objects.requireNonNull(logArea);
    }

    public void log(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }
}
