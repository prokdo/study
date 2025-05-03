package ru.prokdo.udptransport.controller;

import java.io.File;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;

import ru.prokdo.udptransport.model.udp.UDPConstants;
import ru.prokdo.udptransport.model.udp.UDPReceiver;
import ru.prokdo.udptransport.model.udp.UDPSender;
import ru.prokdo.udptransport.controller.log.Logger;

public final class MainController extends Controller {

    @FXML
    private Label selectionLabel;

    @FXML
    private ComboBox<String> modeComboBox;

    @FXML
    private Button selectFileOrDirButton;

    @FXML
    private Label filePathLabel;

    @FXML
    private TextArea logsTextArea;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portSenderTextField;

    @FXML
    private TextField portReceiverTextField;

    @FXML
    private Button startButton;

    private Logger logger;
    private UDPSender sender;
    private UDPReceiver receiver;

    private String filePath;
    private String ip;
    private String port;
    private String recPort;

    @FXML
    public void initialize() {
        logger = new Logger(logsTextArea);

        portSenderTextField.setText(String.valueOf(UDPConstants.DEFAULT_SENDER_PORT));
        portReceiverTextField.setText(String.valueOf(UDPConstants.DEFAULT_RECEIVER_PORT));

        modeComboBox.getItems().addAll("Отправка", "Получение");
    }

    @FXML
    public void selectFile() {
        String mode = modeComboBox.getValue();
        if (mode == null) {
            showNotification("Ошибка", "Режим не выбран", AlertType.WARNING);
            return;
        }

        switch (mode) {
            case "Отправка" -> {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(null);
                if (selectedFile != null) {
                    filePath = selectedFile.getAbsolutePath();
                    filePathLabel.setText("Выбран файл: " + filePath);
                    logger.log("Выбран файл для отправки: " + filePath);
                } else {
                    showNotification("Ошибка", "Файл не выбран", AlertType.WARNING);
                }
            }
            case "Получение" -> {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(null);
                if (selectedDirectory != null) {
                    filePath = selectedDirectory.getAbsolutePath();
                    filePathLabel.setText("Выбрана директория: " + filePath);
                    logger.log("Выбрана директория для получения файла: " + filePath);
                } else {
                    showNotification("Ошибка", "Директория не выбрана", AlertType.WARNING);
                }
            }
            default -> logger.log("Неверный режим работы");
        }
    }

    @FXML
    public void start() {
        String mode = modeComboBox.getValue();
        if (mode == null) {
            showNotification("Ошибка", "Режим работы не выбран", AlertType.WARNING);
            return;
        }

        ip = ipTextField.getText();
        port = portSenderTextField.getText();
        recPort = portReceiverTextField.getText();

        if (!validateInput(ip, port, recPort, filePath)) {
            return;
        }

        startButton.setDisable(true);
        modeComboBox.setDisable(true);
        selectFileOrDirButton.setDisable(true);

        try {
            switch (mode) {
                case "Отправка" -> startSendingAsync(ip, Integer.parseInt(port), Integer.parseInt(recPort));
                case "Получение" -> startReceivingAsync(ip, Integer.parseInt(port), Integer.parseInt(recPort));
                default -> logger.log("Неизвестный режим работы");
            }
        } catch (NumberFormatException e) {
            showNotification("Ошибка", "Возникла ошибка", AlertType.ERROR);
        }

        startButton.setDisable(false);
        modeComboBox.setDisable(false);
        selectFileOrDirButton.setDisable(false);
    }

    // Асинхронный метод для отправки файла
    private void startSendingAsync(String ip, int port, int recPort) {
        Task<Void> sendTask = new Task<>() {
            @Override
            protected Void call() {
                startSending(ip, port, recPort);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };

        // Запуск задачи в отдельном потоке
        new Thread(sendTask).start();
    }

    // Асинхронный метод для получения файла
    private void startReceivingAsync(String ip, int port, int recPort) {
        Task<Void> receiveTask = new Task<>() {
            @Override
            protected Void call() {
                startReceiving(ip, port, recPort);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
            }

            @Override
            protected void failed() {
                super.failed();
            }
        };

        // Запуск задачи в отдельном потоке
        new Thread(receiveTask).start();
    }

    private void startSending(String ip, int port, int recPort) {

        if (sender == null) {
            sender = new UDPSender(logger);
        }

        sender.setPort(port);
        sender.setReceiverPort(recPort);
        sender.setReceiverAddress(ip);
        sender.start();

        if (sender.send(filePath)) {
            Platform.runLater(() -> logger.log("Файл успешно отправлен"));
        } else {
            Platform.runLater(() -> logger.log("Ошибка при отправке файла"));
        }

        sender.stop();
    }

    private void startReceiving(String ip, int port, int recPort) {
        if (receiver == null) {
            receiver = new UDPReceiver(logger);
        }

        receiver.setPort(recPort);
        receiver.setSenderPort(port);
        receiver.setSenderAddress(ip);
        receiver.start();

        if (receiver.receive(filePath)) {
            Platform.runLater(() -> logger.log("Файл успешно получен"));
        } else {
            Platform.runLater(() -> logger.log("Ошибка при получении файла"));
        }

        receiver.stop();
    }

    private boolean validateInput(String ip, String port, String recPort, String filePath) {
        if (ip.isEmpty() || port.isEmpty() || recPort.isEmpty()) {
            showNotification("Ошибка", "IP и порты не могут быть пустыми", AlertType.WARNING);
            return false;
        }

        if (filePath == null || filePath.isEmpty()) {
            showNotification("Ошибка", "Путь до файла/директории не указан", AlertType.WARNING);
            return false;
        }

        if (!validatePort(port) || !validatePort(recPort)) {
            showNotification("Ошибка", "Неверный порт", AlertType.ERROR);
            return false;
        }

        if (!validateIP(ip)) {
            showNotification("Ошибка", "Неверный IP адрес", AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validatePort(String port) {
        try {
            int portNumber = Integer.parseInt(port);
            return portNumber > 0 && portNumber <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateIP(String ip) {
        return ip.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
    }

    private void showNotification(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
