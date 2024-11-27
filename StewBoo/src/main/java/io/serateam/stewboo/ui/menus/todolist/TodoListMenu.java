package io.serateam.stewboo.ui.menus.todolist;

import io.serateam.stewboo.core.services.todolist.TaskList;
import io.serateam.stewboo.core.services.todolist.TaskModel;
import io.serateam.stewboo.core.services.todolist.TodoListService;

import io.serateam.stewboo.ui.menus.IMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TodoListMenu implements Initializable, IMenu {

    @FXML private Button addTask;
    @FXML private Button SaveList;
    @FXML private VBox taskContainer; // Renamed to follow naming conventions

    private final TodoListService service = TodoListService.getInstance();
    private final List<TaskModel> taskList = service.getTaskList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        loadTask();
    }

    @FXML
    protected void onAddTask(ActionEvent event) {
        addTaskToView();
    }

    private void addTaskToView() {
        ToDoItemInstance taskComponent = new ToDoItemInstance();

        taskComponent.getDeleteButton().setOnAction(e -> {
            taskContainer.getChildren().remove(taskComponent);
            String str = taskComponent.getTaskText();
            boolean fl = taskComponent.isTaskChecked();
            service.deleteTaskItem(str, fl);
            saveTasks();
        });

        saveTasks();
        taskContainer.getChildren().add(0, taskComponent); // Fix: Modify taskContainer
    }


    void saveTasks()
    {
        System.out.println("Saving in Menu");
        service.saveList();
    }

    void loadTask()
    {
        for (TaskModel task : taskList) {
            ToDoItemInstance instance = new ToDoItemInstance(task);
            taskContainer.getChildren().add(0, instance);
        }

    }

}

