package com.example.textstream;

public class BlockItem {
    private String title;
    private Class<?> activityClass;
    private String objectives; // Additional string example
    private String notes; // Another additional string
    private String book;
    private String bookbutton;
    public BlockItem(String title, String bb, Class<?> activityClass, String objectives, String notes,String book) {
        this.title = title;
        this.activityClass = activityClass;
        this.objectives = objectives;
        this.notes = notes;
        this.bookbutton = bb;
        this.book=book;
    }

    public String getTitle() {
        return title;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public String getObjectives() {
        return objectives;
    }

    public String getNotes() {
        return notes;
    }
    public String getBook() {
        return book;
    }
    public String getBookbutton() {
        return bookbutton;
    }
}
