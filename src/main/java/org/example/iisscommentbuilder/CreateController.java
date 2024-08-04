package org.example.iisscommentbuilder;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.HashMap;

public class CreateController extends Controller {
    @FXML
    private ListView<String> goalListView;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField nameTextField;
    private JDBC MySQLConnection;
    private HashMap<String, Goal> goalHashMap;
    private String currentGoal;
    private String currentActivity;


    @Override
    public void sendDatabaseConnection(JDBC MySQLConn) {
        MySQLConnection = MySQLConn;
    }
    @Override
    public void sendCommonData(HashMap<String, Goal> goalHashMap, String currentGoal, String currentActivity) {
        this.goalHashMap = goalHashMap;
        this.currentGoal = currentGoal;
        this.currentActivity = currentActivity;
    }

    @Override
    public void init() {
        goalListView.getItems().addAll(goalHashMap.keySet());
        goalListView.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> {
            currentGoal = goalListView.getSelectionModel().getSelectedItem();
            descriptionTextArea.clear();

        });
        goalListView.getSelectionModel().select(currentGoal);
        nameTextField.setText(currentActivity);
        descriptionTextArea.setWrapText(true);
        if (goalHashMap.get(currentGoal).getActivities().contains(currentActivity)) {
            descriptionTextArea.appendText(goalHashMap.get(currentGoal).getActivityDescription(currentActivity) + "\n");
        }
    }

    public void onSubmitButtonClicked(ActionEvent actionEvent) {
        currentActivity = nameTextField.getText();
        if (currentActivity == null || currentActivity.isEmpty()) {
            createAlert("Please enter a name for the goal");
            return;
        }
        if (currentGoal == null) {
            createAlert("Please select a goal");
            return;
        }
        if (descriptionTextArea.getText().isEmpty()) {
            createAlert("Please enter a description for the goal");
            return;
        }
        Goal tempGoal = goalHashMap.get(currentGoal);
        if (tempGoal.getNumberOfActivities() != 0 && tempGoal.getActivities().contains(currentActivity)) {
            createAlert("That activity name has already been chosen");
            return;
        }
        boolean check = MySQLConnection.addActivity(currentActivity, descriptionTextArea.getText(), tempGoal.getGoal_id());
        if (!check) {
            createAlert("Something went wrong with MYSQL");
            return;
        }
        int maxId = MySQLConnection.findMaxActivityId();
        if (maxId == -1) {
            createAlert("Couldn't find Max Id");
            onQuitClick(actionEvent);
        }
        Activity tempActivity = new Activity(currentActivity, descriptionTextArea.getText(), maxId);
        tempGoal.addActivity(tempActivity);
        goBack(actionEvent);
    }
    public void onQuitClick(ActionEvent ignoredActionEvent) {
        Platform.exit();
    }
    public void onBuilderButtonClick(ActionEvent actionEvent) {
        currentActivity = null;
        goBack(actionEvent);
    }
    public void onEditButtonClicked(ActionEvent actionEvent) {
        StageManager stageManager = new StageManager();
        stageManager.setData(MySQLConnection, goalHashMap, currentGoal, null);
        stageManager.moveToEditorView(actionEvent);
    }
    private void goBack(ActionEvent actionEvent) {
        StageManager stageManager = new StageManager();
        stageManager.setData(MySQLConnection, goalHashMap, currentGoal, currentActivity);
        stageManager.moveToMainView(actionEvent);
    }

}
