package io.serateam.stewboo.ui.menus;

import io.serateam.stewboo.core.services.todolist.TaskList;
import io.serateam.stewboo.core.services.todolist.TaskModel;
import io.serateam.stewboo.core.services.todolist.TodoListService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class TodoListMenu implements IMenu {

    @FXML
    private Button addTask;

    @FXML
    private VBox taskContainer; // Renamed to follow naming conventions

    private final List<TaskModel> tasks = new ArrayList<>();

    @FXML
    void initialize() {
        try {
            List<TaskModel> loadedTasks = TaskList.loadTasks();
            if (loadedTasks != null) {
                tasks.addAll(loadedTasks);
                tasks.forEach(this::addTaskToView);
            }
        } catch (Exception e) {
            System.err.println("Error loading tasks: ");
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddTask(ActionEvent event) {
        TaskModel newTask = new TaskModel("", false);
        tasks.add(newTask);
        addTaskToView(newTask);
    }

    private void addTaskToView(TaskModel newTask) {
        TodoListService taskComponent = new TodoListService(newTask);

        taskComponent.getDeleteButton().setOnAction(e -> {
            taskContainer.getChildren().remove(taskComponent); // Fix: Modify taskContainer
            tasks.remove(newTask);
            saveTasks();
        });

        taskContainer.getChildren().add(0, taskComponent); // Fix: Modify taskContainer
    }

    private void saveTasks() {
        try {
            TaskList.saveTasks(tasks);
        } catch (Exception e) {
            System.err.println("Error saving tasks: ");
            e.printStackTrace();
        }
    }

    @FXML
    public void onExit() {
        saveTasks();
    }
}

