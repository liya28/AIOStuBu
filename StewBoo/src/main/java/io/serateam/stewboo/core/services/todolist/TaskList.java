package io.serateam.stewboo.core.services.todolist;

import io.serateam.stewboo.core.utility.ISerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskList implements ISerializable, Iterable<TaskModel>
{
    private final List<TaskModel> tasks = new ArrayList<>();

    public void addTask(TaskModel task)
    {
        tasks.add(task);
    }

    public void removeTask(TaskModel task)
    {
        tasks.remove(task);
    }

    public TaskModel findTask(String str, boolean flag)
    {
        for(TaskModel task : tasks) {
            if(task.getTaskContent().equalsIgnoreCase(str)) {
                if(task.isCompleted() == flag) {
                    return task;
                }
            }
        }
        return null;
    }

    public TaskList getTaskList()
    {
        return this;
    }

    @Override
    public Iterator<TaskModel> iterator()
    {
        return tasks.iterator();
    }
}