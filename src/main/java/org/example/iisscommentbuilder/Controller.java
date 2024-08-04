package org.example.iisscommentbuilder;


import javafx.scene.control.Alert;

import java.util.HashMap;

public abstract class Controller {
    public abstract void sendDatabaseConnection(JDBC MySQLConn);
    public abstract void sendCommonData(HashMap<String, Goal> goalHashMap, String currentGoal, String currentActivity);
    public abstract void init();
    protected static void createAlert(String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.show();
    }

}
