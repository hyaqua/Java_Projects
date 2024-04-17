package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientRun extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource("client-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Client");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setOnHidden(e -> ((ClientController) fxmlLoader.getController()).shutdown());
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
