package com.example.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.StageStyle;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.text.Text;

public class ClientController implements Initializable {
    Client client;
    @javafx.fxml.FXML
    private TextField messageEntry;
    @javafx.fxml.FXML
    private Button btn;
    private Socket socket;
    @javafx.fxml.FXML
    private ScrollPane MsgArea;
    @javafx.fxml.FXML
    private VBox mesagebox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       Dialog<String> dialog = new TextInputDialog("");
       dialog.setGraphic(null);
       dialog.setTitle("Login");
       dialog.setHeaderText("Enter username");
       Optional<String> result = dialog.showAndWait();
       String user = "";
       if (result.isPresent()) {
           user = result.get();
       } else{
              System.exit(0);
       }
        try {
            socket = new Socket("localhost", 8080);
            client = new Client(socket, user, this);
            client.ListenForMessages();
            client.sendUserName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void receiveMSG(String msg){
//        textField.appendText(msg + "\n");
        System.out.println(msg);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(msg.contains("USER: ")){
                    String[] message = msg.split(": ", 3);
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setPadding(new Insets(5, 5, 5, 10));
                    Text namer = new Text(message[1]);
                    TextFlow name = new TextFlow(namer);
                    name.setPadding(new Insets(5, 0, 5, 5));
                    namer.setFill(Color.rgb(120,120,120));
                    Text text = new Text(message[2]);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color: rgb(236,229,229);"
                            + "-fx-background-color: rgb(122,137,152);"
                            + "-fx-background-radius: 20px");
                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934,0.945,0.996));
                    hbox.getChildren().add(textFlow);
                    hbox.getChildren().add(name);
                    mesagebox.getChildren().add(hbox);
                } else if(msg.contains("SERVER: ")) {
                    String[] message = msg.split(": ", 2);
                    HBox hbox = new HBox();
                    hbox.setAlignment(Pos.CENTER);
                    hbox.setPadding(new Insets(0, 5, 0, 10));
                    Text text = new Text(message[1]);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setPadding(new Insets(0, 10, 0, 10));
                    text.setFill(Color.rgb(120,120,120));
                    hbox.getChildren().add(textFlow);
                    mesagebox.getChildren().add(hbox);
                }
            }
        });

    }

    @FXML
    public void sendMsg(ActionEvent actionEvent) {
        String msg = messageEntry.getText();
        if(!msg.isEmpty()){
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.setPadding(new Insets(5, 5, 5, 10));
            Text namer = new Text("Me");
            TextFlow name = new TextFlow(namer);
            name.setPadding(new Insets(5, 5, 5, 0));
            namer.setFill(Color.rgb(120,120,120));
            Text text = new Text(msg);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(236,229,229);"
                    + "-fx-background-color: rgb(39,140,246);"
                    + "-fx-background-radius: 20px");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934,0.945,0.996));
            hbox.getChildren().add(name);
            hbox.getChildren().add(textFlow);
            mesagebox.getChildren().add(hbox);
        }
        if(messageEntry.getText().equals("/exit")){
            client.sendJavaFxMessage(messageEntry.getText());
            shutdown();
            return;
        }
        client.sendJavaFxMessage(msg);
        messageEntry.clear();
    }
    public void shutdown(){
        client.shutdown();
        Platform.exit();
        System.exit(0);
    }
}
