package io.serateam.stewboo.core.services.todolist;

import io.serateam.stewboo.core.utility.ISerializable;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskList implements ISerializable, Iterable<TaskModel>
{
    private final List<TaskModel> tasks = new ArrayList<>();

    void addTask(TaskModel task)
    {
        tasks.add(task);
    }


    void removeTask(TaskModel task)
    {
        if(task == null) {
            System.out.println("This task Does not Exist");
        }
        tasks.remove(task);
    }

    TaskModel findTask(String str, boolean flag) {
        for(TaskModel task : tasks) {
            if(task.getTaskContent().equalsIgnoreCase(str)) {
                if(task.isCompleted() == flag) {
                    return task;
                }
            }
        }
        return null;
    }

    List<TaskModel> getTaskList() {
        return tasks;
    }

    @Override
    public Iterator<TaskModel> iterator() {
        return tasks.iterator();
    }
}