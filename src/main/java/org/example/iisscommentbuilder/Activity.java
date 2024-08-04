package org.example.iisscommentbuilder;

public class Activity {
    private String name, description;
    private final int activityId;
    public Activity(String name, String description, int activityId) {
        this.name = name;
        this.description = description;
        this.activityId = activityId;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getActivityId() {
        return activityId;
    }
    public void setActivityName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
