package ru.geekbrains.chat.client;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.geekbrains.april_chat.common.ChatMessage;
import ru.geekbrains.april_chat.common.MessageType;
import ru.geekbrains.april_chat.network.ChatMessageService;
import ru.geekbrains.april_chat.network.ChatMessageServiceImpl;
import ru.geekbrains.april_chat.network.MessageProcessor;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainChatController implements Initializable, MessageProcessor {


    public TextArea chatArea;
    public ListView onlineUsers;
    public TextField inputField;
    public Button btnSendMessage;
    public TextField loginField;
    public PasswordField passwordField;
    public Button btnSendAuth;
    private ChatMessageService messageService;
    private String currentName;

    public void mockAction(ActionEvent actionEvent) {
        System.out.println("MOCK!");
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void showAbout(ActionEvent actionEvent) {
        Label secondLabel = new Label("About Window");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene aboutScene = new Scene(secondaryLayout, 230, 100);

        Stage aboutWindow = new Stage();
        aboutWindow.setTitle("About");
        aboutWindow.setScene(aboutScene);
        aboutWindow.setAlwaysOnTop(true);

        Stage aboutStage = new Stage();
        aboutWindow.setX(aboutStage.getX() + 200);
        aboutWindow.setY(aboutStage.getY() + 100);

        aboutWindow.show();
       // new About();
    }



//    public class About extends JFrame {
//        public About() {
//            setTitle("About");
//            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//            setBounds(800, 200, 400, 400);
//            JLabel label1 = new JLabel("About window");
//            add(label1, BorderLayout.NORTH);
//            setVisible(true);
//            setAlwaysOnTop(true);
//        }
//    }

    public void showHelp(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI("https://docs.google.com/document/d/1wr0YEtIc5yZtKFu-KITqYnBtp8KC28v2FEYUANL0YAM/edit?usp=sharing"));
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = inputField.getText();
        if (text.isEmpty()) return;
        ChatMessage msg = new ChatMessage();
        msg.setMessageType(MessageType.PUBLIC);
        msg.setFrom(currentName);
        msg.setBody(text);
        messageService.send(msg.marshall());
        inputField.clear();
    }

    private void appendTextFromTF(ChatMessage msg) {
        String text = String.format("[%s] %s\n", msg.getFrom(), msg.getBody());
        chatArea.appendText(text);
    }

    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText(e.getMessage());
        VBox dialog = new VBox();
        Label label = new Label("Trace:");
        TextArea textArea = new TextArea();
        //TODO
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement el : e.getStackTrace()) {
            builder.append(el).append(System.lineSeparator());
        }
        textArea.setText(builder.toString());
        dialog.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialog);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.messageService = new ChatMessageServiceImpl("localhost", 12256, this);
        messageService.connect();
    }

    @Override
    public void processMessage(String msg) {
        Platform.runLater(() -> {
                    ChatMessage message = ChatMessage.unmarshall(msg);
                    System.out.println("Received message");

                    switch (message.getMessageType()) {
                        case PUBLIC, PRIVATE -> appendTextFromTF(message);
                        case CLIENT_LIST -> refreshOnlineUsers(message);
                        case AUTH_CONFIRM -> {
                            this.currentName = message.getBody();
                            App.stage1.setTitle(currentName);
                        }
                    }
                }
        );
    }

    private void refreshOnlineUsers(ChatMessage message) {
        this.onlineUsers.setItems(FXCollections.observableArrayList(message.getOnlineUsers()));
    }

    public void sendAuth(ActionEvent actionEvent) {
        String log = loginField.getText();
        String pass = passwordField.getText();
        if (log.isEmpty() || pass.isEmpty()) return;
        ChatMessage msg = new ChatMessage();
        msg.setMessageType(MessageType.SEND_AUTH);
        msg.setLogin(log);
        msg.setPassword(pass);
        messageService.send(msg.marshall());
    }
}
