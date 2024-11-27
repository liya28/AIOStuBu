package io.serateam.stewboo.core.services.todolist;

import io.serateam.stewboo.core.utility.ISerializable;

public class TaskModel implements ISerializable {
    private String taskContent;
    private boolean isCompleted;

    public TaskModel(String text, boolean isCompleted)
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
}

