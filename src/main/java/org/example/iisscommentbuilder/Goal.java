package org.example.iisscommentbuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class Goal {
    private final String goal_name;
    private final int goal_id;
    private final HashMap<String, Activity> activities;
    public Goal(String goal_name, int goal_id) {
        this.goal_name = goal_name;
        this.goal_id = goal_id;
        activities = new HashMap<>();
    }
    public void addActivity(Activity activity) {
        activities.put(activity.getName(), activity);
    }
    public void removeActivity(String activityName) {
        activities.remove(activityName);
    }
    public int getGoal_id() {
        return goal_id;
    }
    public ArrayList<String> getActivities() {
        return new ArrayList<>(activities.keySet());
    }
    public String getActivityDescription(String activityName) {
        return activities.get(activityName).getDescription();
    }
    public int getNumberOfActivities() {
        return activities.size();
    }
    public int getActivityId(String activityName) {
        return activities.get(activityName).getActivityId();
    }
    public void setActivityName(String activityName, String newName) {
        Activity tempActivity = activities.get(activityName);
        tempActivity.setActivityName(newName);
        activities.remove(activityName);
        activities.put(newName, tempActivity);
    }
    public void setActivityDescription(String activityName, String description) {
        activities.get(activityName).setDescription(description);
    }
    @Override
    public String toString() {
        return goal_name;
    }
}
