package com.example.textstream;

public class BlockItem {
    private String title;
    private Class<?> activityClass; // The activity to launch when clicked

    public BlockItem(String title, Class<?> activityClass) {
        this.title = title;
        this.activityClass = activityClass;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }
}
