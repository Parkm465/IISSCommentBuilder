package org.example.iisscommentbuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class Controller {
    private JDBC MySQLConn = new JDBC();
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField urlTextField;

    private String username, password, url;
    private Application application = new Application();


    public void onLoginButtonClick (ActionEvent actionEvent) {
        username = usernameTextField.getText();
        password = passwordTextField.getText();
        url = urlTextField.getText();
        if (username == null || password == null || url == null) {
            System.out.println("One of the fields are empty");
        } else {
            try {
                MySQLConn.login(username, password, url);
                MySQLConn.connect();
                System.out.println("Connected to database");
                switchScene("main-view.fxml", "Task-Builder", 1000, 800);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Connection failed");
            }
        }
        System.out.println("Submitted login");
    }
    private void switchScene(String file, String title, int width, int height) {
        try {
            application.switchScene(file, title, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}