package ru.geekbrains.chat.client;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainChatController {


    public TextArea chatArea;
    public ListView onlineUsers;
    public TextField inputField;
    public Button btnSendMessage;

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
        appendTextFromTF();
    }

    private void appendTextFromTF() {
        String msg = inputField.getText();
        if (msg.isEmpty()) return;
        chatArea.appendText("ME: " + msg + System.lineSeparator());
        inputField.clear();
    }
}
