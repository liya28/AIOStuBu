package io.serateam.stewboo.ui.menus.todolist;

import io.serateam.stewboo.core.services.todolist.TodoListService;

import io.serateam.stewboo.ui.menus.IMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class TodoListMenu implements IMenu {

    @FXML private Button addTask;
    @FXML private Button SaveList;

    @FXML private VBox taskContainer; // Renamed to follow naming conventions

    private final TodoListService service = TodoListService.getInstance();

    @FXML
    void initialize() {

    }

    @FXML
    protected void onAddTask(ActionEvent event) {
        service.createTaskItem("",false);
        addTaskToView(service);
    }

    private void deleteTask() {

    }

    private void addTaskToView(TodoListService newTask) {
        ToDoListInstance taskComponent = new ToDoListInstance(newTask);

        taskComponent.getDeleteButton().setOnAction(e -> {
            taskContainer.getChildren().remove(taskComponent);
            String str = taskComponent.getTaskText();
            saveTasks();
        });


        taskComponent.getSaveButton().setOnAction(e-> {
            saveTasks();
        });

        saveTasks();
        taskContainer.getChildren().add(0, taskComponent); // Fix: Modify taskContainer
    }

    void saveTasks() {
        System.out.println("Saving in Menu");
        service.saveList();
    }

    void loadTask() {
//        List<TaskList> = service.getTaskList();
    }


    @FXML
    public void onExit() {
        saveTasks();
    }
}

