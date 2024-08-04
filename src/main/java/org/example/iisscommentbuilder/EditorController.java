package org.example.iisscommentbuilder;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.HashMap;

public class EditorController extends Controller {

    private JDBC MySQLConnection;
    private String currentGoal, currentActivity;
    private HashMap<String, Goal> goalHashMap;

    @FXML
    private ListView<String> goalListView;
    @FXML
    private ListView<String> activityListView;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField activityNameTextField;
    @FXML
    private Button submitButton;
    @FXML
    private Button deleteButton;

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
            activityListView.getItems().clear();
            activityListView.getItems().addAll(goalHashMap.get(currentGoal).getActivities());
            descriptionTextArea.clear();
            activityNameTextField.clear();
            submitButton.setDisable(true);
            deleteButton.setDisable(true);
        });
        activityListView.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> {
            currentActivity = activityListView.getSelectionModel().getSelectedItem();
            activityNameTextField.setText(currentActivity);
            descriptionTextArea.clear();
            if (currentActivity != null && !currentActivity.isBlank()) {
                descriptionTextArea.setText(goalHashMap.get(currentGoal).getActivityDescription(currentActivity));
            }
            deleteButton.setDisable(false);
        });
        activityNameTextField.textProperty().addListener((_, _, t1) -> {
            if (currentActivity == null || currentActivity.isBlank() || currentActivity.equals(t1)) {
                return;
            }
            submitButton.setDisable(false);
        });
        descriptionTextArea.textProperty().addListener((_, s, _) -> {
            if (descriptionTextArea.getText().isEmpty() || s.isEmpty()) {
                return;
            }
            if (submitButton.isDisable()) {
                submitButton.setDisable(false);
            }
        });
        descriptionTextArea.setWrapText(true);
        if (currentGoal != null) {
            goalListView.getSelectionModel().select(currentGoal);
        }
        if (currentActivity != null) {
            activityListView.getSelectionModel().select(currentActivity);
        }
    }

    public void onSubmitButtonClick(ActionEvent ignoredActionEvent) {
        //Compare currentActivity to Name to see if there's any changes
        if (currentActivity == null || currentActivity.isBlank()) {
            createAlert("No Activity Selected");
            return;
        }
        if (descriptionTextArea.getText().isEmpty()) {
            createAlert("No Description");
            return;
        }
        Goal tempGoal = goalHashMap.get(currentGoal);
        int activityId = tempGoal.getActivityId(currentActivity);
        String newName = activityNameTextField.getText();
        if (!currentActivity.equals(newName)) {
            boolean check = MySQLConnection.changeActivityName(activityId, newName);
            if (!check) {
                createAlert("SQL Failure in changing Name");
                return;
            }
            String tempDescription = descriptionTextArea.getText();
            tempGoal.setActivityName(currentActivity, newName);
            int tempIndex = activityListView.getItems().indexOf(currentActivity);
            activityListView.getItems().remove(currentActivity);
            activityListView.getItems().add(tempIndex, newName);
            activityListView.getSelectionModel().select(newName);
            currentActivity = newName;
            descriptionTextArea.setText(tempDescription);
        }
        //Compare description from hashmap to description of textarea
        String editedDescription = descriptionTextArea.getText();
        String currentDescription = tempGoal.getActivityDescription(currentActivity);
        if (!editedDescription.equals(currentDescription)) {
            boolean check = MySQLConnection.changeActivityDescription(activityId, editedDescription);
            if (!check) {
                createAlert("SQL Failure in changing Description");
                return;
            }
            tempGoal.setActivityDescription(currentActivity, editedDescription);
        }
        submitButton.setDisable(true);
    }

    public void onDeleteButtonClick(ActionEvent ignoredActionEvent) {
        if (currentActivity == null || currentActivity.isBlank()) {
            createAlert("Activity not selected");
            return;
        }
        Goal tempGoal = goalHashMap.get(currentGoal);
        boolean  check = MySQLConnection.deleteActivity(tempGoal.getActivityId(currentActivity));
        if (!check) {
            createAlert("Activity " + currentActivity + " not found");
            return;
        }
        tempGoal.removeActivity(currentActivity);
        activityListView.getItems().remove(currentActivity);
        descriptionTextArea.clear();
        activityNameTextField.clear();
        currentActivity = null;
    }

    public void onBuilderButtonClick(ActionEvent actionEvent) {
        currentActivity = null;
        goBack(actionEvent);
    }
    private void goBack (ActionEvent actionEvent) {
        StageManager stageManager = new StageManager();
        stageManager.setData(MySQLConnection, goalHashMap, currentGoal, currentActivity);
        stageManager.moveToMainView(actionEvent);
    }
    public void onCreateButtonClick(ActionEvent actionEvent) {
        StageManager stageManager = new StageManager();
        stageManager.setData(MySQLConnection, goalHashMap, currentGoal, currentActivity);
        stageManager.moveToCreateView(actionEvent);
    }
    public void onQuitButtonClick(ActionEvent ignoredActionEvent) {
        Platform.exit();
    }


}
