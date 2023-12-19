package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import udp.UDPReceiver;
import udp.UDPSender;

public class PrimaryController implements Initializable {
    @FXML
    Label selectionLabel;
    @FXML
    ComboBox<String> modeComboBox;
    @FXML
    Button selectFileOrDirButton;
    @FXML
    Label filePathLabel;
    @FXML
    TextArea logsTextArea;
    @FXML
    TextField ipTextField;
    @FXML
    TextField portSenderTextField;
    @FXML
    TextField portReceiverTextField;
    public final String DEF_IP = "";
    public final String DEF_PORT = "";
    public final String DEF_REC_PORT = "";

    public String IP = DEF_IP;
    public String port = DEF_PORT;
    public String recPort = DEF_REC_PORT;

    public String filePath;

    private UDPReceiver receiver = new UDPReceiver();
    private UDPSender sender = new UDPSender();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<String> args = FXCollections.observableArrayList("Отправка", "Получение");
        modeComboBox.getItems().addAll(args);
        modeComboBox.setValue("Отправка");
        selectionLabel.setText("Выберете файл для отправки");

        portSenderTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            changePortEvent(newValue);
        });
        portReceiverTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            changeReceiverPortEvent(newValue);
        });
        ipTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            changeIPEvent(newValue);
        });

        receiver.setController(this);
        sender.setController(this);
    }

    public void selectModeEvent() {
        switch (modeComboBox.getValue()) {
            case "Отправка": {
                selectionLabel.setText("Выберете файл для отправки");
                break;
            }
            case "Получение": {
                selectionLabel.setText("Выберете директорию получения");
                break;
            }
        }
    }

    public void selectFile() {
        switch (modeComboBox.getValue()) {
            case "Отправка": {
                FileChooser fc = new FileChooser();
                filePath = fc.showOpenDialog(null).toString();
                filePathLabel.setText("Путь до файла/директории: " + filePath);
                break;
            }
            case "Получение": {
                DirectoryChooser dc = new DirectoryChooser();
                filePath = dc.showDialog(null).toString();
                filePathLabel.setText("Путь до файла/директории: " + filePath);
                break;
            }
        }
    }

    public void changePortEvent(String newPort) {
        port = newPort;
    }

    public void changeReceiverPortEvent(String newPort) {
        recPort = newPort;
    }

    public void changeIPEvent(String newIP) {
        IP = newIP;
    }

    public void start() {
        switch (modeComboBox.getValue()) {
            case "Отправка": {

                sender.setPort(Integer.parseInt(port));
                sender.setReceiverPort(Integer.parseInt(recPort));
                sender.setReceiverAddres(IP);
                sender.start();

                if (sender.send(filePath))
                    addLog("File sent successfully");
                else
                    addLog("File sent unsuccessfully");
                sender.stop();

                break;
            }
            case "Получение": {
                receiver.setPort(Integer.parseInt(recPort));
                receiver.setSenderPort(Integer.parseInt(port));
                receiver.setSenderAddres(IP);
                receiver.start();
                if (receiver.receive(filePath))
                    addLog("File received");
                else
                    addLog("File not received");
                receiver.stop();
                break;
            }
        }
    }

    public void addLog(String text) {
        logsTextArea.setText(logsTextArea.getText() + text + "\n");
    }
}
