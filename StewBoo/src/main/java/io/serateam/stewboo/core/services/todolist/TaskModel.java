package io.serateam.stewboo.core.services.todolist;

import io.serateam.stewboo.core.utility.ISerializable;

public class TaskModel implements ISerializable {
    private String taskContent;
    private boolean isCompleted;

    TaskModel(String text, boolean isCompleted)
    {
        this.taskContent = text;
        this.isCompleted = isCompleted;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return taskContent + " (Checked: " + isCompleted + ")";
    }

//    // TODO: delete
//    public String toFileString() {
//        return taskContent + ";" + isCompleted;
//    }
//
//    // TODO: delete
//    public static TaskModel fromFileString(String line) {
//        String[] parts = line.split(";");
//        if(parts.length == 2) {
//            String taskText = parts[0];
//            boolean isChecked = Boolean.parseBoolean(parts[1]);
//            return new TaskModel(taskText, isChecked);
//        }
//        return null;
//    }
}


// TODO: delete everyhting here once done