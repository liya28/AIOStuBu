package io.serateam.stewboo.core.services.todolist;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.SharedVariables;

public class TodoListService implements IService {

    private static TaskList list;
    private static TodoListService instance;
    private TaskModel model = null;


    private TodoListService()
    {
        getTaskList();
    }

    public static TodoListService getInstance()
    {
        if(instance == null)
        {
            instance = new TodoListService();
        }
        return instance;
    }
    @Override
    public void initializeService()
    {
        // TODO: Add meaningful initialization if needed
    }

    public TaskModel createTaskItem(String content, boolean completed)
    {
        model = new TaskModel(content, completed);
        addTask(model);
        return model;
    }

    public void deleteTaskItem(String content, boolean completed)
    {
        removeTask(list.findTask(content,completed));
    }

    public void setTaskContent(String content, TaskModel taskModel)
    {
        taskModel.setTaskContent(content);
    }

    public void setCompleted(boolean completed, TaskModel taskModel)
    {
        taskModel.setCompleted(completed);
    }

    private void addTask(TaskModel taskModel)
    {
        list.addTask(taskModel);
    }
    private void removeTask(TaskModel taskModel)
    {
        list.removeTask(taskModel);
    }

    public void saveList()
    {
        JSONService.serializeAndWriteToFile(SharedVariables.Path.path_todoList, list);
    }

    public TaskList getTaskList()
    {
        if(list == null)
        {
            list = JSONService.deserialize(SharedVariables.Path.path_todoList, TaskList.class);
            if(list == null) list = new TaskList();
        }
        return list.getTaskList();
    }

}