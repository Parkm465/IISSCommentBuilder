package org.example.iisscommentbuilder;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class MainViewController extends Controller {

    @FXML
    private ListView<String> goalListView;
    @FXML
    private ListView<String> activityListView;
    @FXML
    private TextArea commentTextArea;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button createButton;

    private JDBC mySQLConn;
    private String currentGoal;
    private String currentActivity;
    private HashMap<String, Goal> goalList;

    final Clipboard clipboard = Clipboard.getSystemClipboard();
    final ClipboardContent content = new ClipboardContent();

    private final StageManager stageManager = new StageManager();

    /*
    This method accepts JDBC to initialize
    @Para JDBC
     */
    @Override
    public void sendDatabaseConnection(JDBC mySQLConn) {
        this.mySQLConn = mySQLConn;
    }

    @Override
    public void sendCommonData(HashMap<String, Goal> goalHashMap, String currentGoal, String currentActivity) {
        goalList = goalHashMap;
        this.currentGoal = currentGoal;
        this.currentActivity = currentActivity;
    }

    @Override
    public void init() {

        if (goalList == null) {
            goalList = mySQLConn.getGoals();
        }
        goalListView.getItems().addAll(goalList.keySet());
        goalListView.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> {
            currentGoal = goalListView.getSelectionModel().getSelectedItem();
            activityListView.getItems().clear();
            activityListView.getItems().addAll(goalList.get(currentGoal).getActivities());
            createButton.setDisable(false);
            editButton.setDisable(true);
            addButton.setDisable(true);
            commentTextArea.clear();
        });
        activityListView.getSelectionModel().selectedItemProperty().addListener((_, _, _) -> {
            currentActivity = activityListView.getSelectionModel().getSelectedItem();
            addButton.setDisable(false);
            editButton.setDisable(false);
        });
        activityListView.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                onAddClick(new ActionEvent());
            }
        });

        commentTextArea.setWrapText(true);
        if (currentGoal != null) {
            goalListView.getSelectionModel().select(currentGoal);
        }
        if (currentActivity != null) {
            activityListView.getSelectionModel().select(currentActivity);
        }
    }

    /*
    Copies text within the scene builder to the user's clipboard.
     */
    public void onCopyClick(ActionEvent ignoredActionEvent) {
        content.putString(commentTextArea.getText());
        clipboard.setContent(content);
    }

    public void onAddClick(ActionEvent ignoredActionEvent) {
        if (currentActivity == null) {
           return;
        }
        commentTextArea.appendText(goalList.get(currentGoal).getActivityDescription(currentActivity) + "\n");
    }

    public void onCreateClick(ActionEvent actionEvent) {
        stageManager.setData(mySQLConn, goalList, currentGoal, currentActivity);
        stageManager.moveToCreateView(actionEvent);
    }
    public void onEditClick(ActionEvent actionEvent) {
        stageManager.setData(mySQLConn, goalList, currentGoal, currentActivity);
        stageManager.moveToEditorView(actionEvent);
    }

    public void onQuitClick(ActionEvent ignoredActionEvent) {
        Platform.exit();
    }
}
