package org.example.iisscommentbuilder;

import java.sql.*;
import java.util.HashMap;

public class JDBC {

    private final String url, username;
    private Statement statement, statement2;
    private Connection conn;
    public JDBC() {
        url = "jdbc:mysql://localhost:3306/task_logger_DB";
        username = "root";
    }

    public void connect(String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        statement = conn.createStatement();
        statement2 = conn.createStatement();

    }
    public HashMap<String, Goal> getGoals() {
        HashMap<String, Goal> goals = new HashMap<>();
        try {
            ResultSet getGoalResultSet = statement.executeQuery("SELECT * FROM goals");
            while (getGoalResultSet.next()) {
                String goalName = getGoalResultSet.getString("goal_name");
                int goalId = getGoalResultSet.getInt("goal_id");
                Goal tempGoal = new Goal(goalName, goalId);
                ResultSet getActivitiesSet = statement2.executeQuery("SELECT * FROM activities WHERE goal_id = " + goalId);
                while (getActivitiesSet.next()) {
                    int activityId = getActivitiesSet.getInt("activity_id");
                    String activityName = getActivitiesSet.getString("name");
                    String activityDescription = getActivitiesSet.getString("description");
                    tempGoal.addActivity(new Activity(activityName, activityDescription, activityId));
                }
                goals.put(goalName, tempGoal);
                getActivitiesSet.close();
            }
            getGoalResultSet.close();

        } catch (SQLException e) {
            System.out.println("Error with MYSQL");
            System.out.println(e.getMessage());
        }
        return goals;
    }

    public boolean addActivity(String name, String description, int goalId) {
        String sqlStatement = "INSERT INTO activities (name,description,goal_id) VALUES (?,?,?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, goalId);
            int rows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rows > 0;
        } catch (SQLException e) {
            Controller.createAlert(e.getMessage());
            return false;
        }
    }
    public boolean deleteActivity(int activityId) {
        String sqlStatement = "DELETE FROM activities WHERE activity_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, activityId);
            int rows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rows > 0;
        } catch (SQLException e) {
            Controller.createAlert(e.getMessage());
            return false;
        }
    }

    public boolean changeActivityName(int activityId, String newName) {
        return updateActivityColumn(activityId, "name", newName);
    }
    public boolean changeActivityDescription(int activityId, String newDescription) {
        return updateActivityColumn(activityId, "description", newDescription);
    }
    private boolean updateActivityColumn(int activityId, String column, String cellValue) {
        String sqlStatement = "UPDATE activities SET " + column + " = ? WHERE activity_id = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            preparedStatement.setString(1, cellValue);
            preparedStatement.setInt(2, activityId);
            int rows = preparedStatement.executeUpdate();
            preparedStatement.close();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("SQL Failed to change description");
            return false;
        }
    }
    public int findMaxActivityId() {
        String sqlStatement = "SELECT max(activity_id) AS max_id FROM activities";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            int ans = -1;
            while (resultSet.next()) {
                ans = resultSet.getInt("max_id");
            }
            return ans;

        } catch (SQLException e) {
            Controller.createAlert(e.getMessage());
            return -1;
        }
    }
}


