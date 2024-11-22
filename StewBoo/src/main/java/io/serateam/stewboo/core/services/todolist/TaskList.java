package io.serateam.stewboo.core.services.todolist;

import io.serateam.stewboo.core.utility.ISerializable;

import java.util.ArrayList;
import java.util.List;

class TaskList implements ISerializable
{
    private List<TaskModel> tasks = new ArrayList<>();

    void addTask(TaskModel task)
    {
        tasks.add(task);
    }


    void removeTask(TaskModel task)
    {
        tasks.remove(task);
    }
}

// TODO: delete everything below here once done