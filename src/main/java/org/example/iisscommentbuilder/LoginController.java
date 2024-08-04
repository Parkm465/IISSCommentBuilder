package org.example.iisscommentbuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class LoginController {
    private final JDBC MySQLConn;
    @FXML
    private TextField passwordTextField;

    {
        MySQLConn = new JDBC();
    }
    public void onLoginButtonClick (ActionEvent event) {
        String password = passwordTextField.getText();
        if (password.isBlank()) {
            Controller.createAlert("Password is empty");
            return;
        }
        try {
            MySQLConn.connect(password);
            StageManager stageManager = new StageManager();
            stageManager.setData(MySQLConn, null, null, null);
            stageManager.moveToMainView(event);
        } catch (SQLException e) {
            Controller.createAlert(e.getMessage());
        }
    }

}