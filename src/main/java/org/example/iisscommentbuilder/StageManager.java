package org.example.iisscommentbuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;

public class StageManager {

    private JDBC MySQLConnection;
    private HashMap<String, Goal> goalHashMap;
    private String currentGoal;
    private String currentActivity;
    private boolean hasData = false;

    private void moveScenes(String fileName, String title, ActionEvent event) {
        if (!hasData) {
            Controller.createAlert("You need to set data first [code error]");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
            //Moves JDBC to main view class
            Parent root = loader.load();
            Controller controller = loader.getController();
            //Creates new scene
            Scene scene = new Scene(root);
            controller.sendDatabaseConnection(MySQLConnection);
            controller.sendCommonData(goalHashMap, currentGoal, currentActivity);
            controller.init();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setX(20);
            window.setY(0);
            window.setTitle(title);
            window.show();
        } catch (IOException e) {
            Controller.createAlert("FXML File Failure [Code issue]");
            System.out.println(e.getMessage());
        }
    }
    public void setData(JDBC mySQLConnection, HashMap<String, Goal> goalHashMap, String currentGoal, String currentActivity) {
        this.MySQLConnection = mySQLConnection;
        this.goalHashMap = goalHashMap;
        this.currentGoal = currentGoal;
        this.currentActivity = currentActivity;
        hasData = true;
    }
    public void moveToMainView(ActionEvent event) {moveScenes("main-view.fxml", "Main View", event);}
    public void moveToCreateView(ActionEvent event) {moveScenes("create-view.fxml", "Create New Activity", event);}
    public void moveToEditorView(ActionEvent event) {moveScenes("edit-view.fxml", "Edit an Activity", event);}

}
