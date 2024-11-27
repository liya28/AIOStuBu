package io.serateam.stewboo.ui.menus.todolist;


import io.serateam.stewboo.core.services.todolist.TaskModel;
import io.serateam.stewboo.core.services.todolist.TodoListService;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToDoItemInstance extends HBox {
    private TaskModel taskModel;
    private TextField taskField;
    private CheckBox taskCheckBox;
    private Button deleteButton;
    private Button saveButton;
    private final TodoListService todoListService = TodoListService.getInstance();

    public ToDoItemInstance(String taskContent, boolean completed) {

        super(10); // 10 pixels spacing
        setPadding(new Insets(5));
        setStyle("-fx-background-color: lightblue; -fx-border-color: gray;");

        // Task text field
        taskField = new TextField(taskContent);
        taskField.setPrefWidth(300);
        taskField.setDisable(completed);

        taskField.textProperty().addListener((observable, oldValue, newValue) -> {
            todoListService.setTaskContent(newValue, taskModel);
            try {
                todoListService.setTaskContent(taskField.getText(), taskModel);
                System.out.println("Task saved: " + newValue);
                todoListService.saveList();
            } catch (Exception ex) {
                System.err.println("Error saving task: ");
                ex.printStackTrace();
            }
            // Restart delay on each keystroke
        });

        // Checkbox
        taskCheckBox = new CheckBox();
        taskCheckBox.setSelected(completed);
        taskCheckBox.setOnAction(e -> {
            taskField.setDisable(taskCheckBox.isSelected());
        });

        // Delete button
        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e->{
            todoListService.deleteTaskItem(taskField.getText(),taskCheckBox.isSelected());
            todoListService.saveList();
            ((VBox) this.getParent()).getChildren().remove(this);
        });
        saveButton = new Button("Save");
        saveButton.setOnAction(e->{
            todoListService.saveList();
        });

        getChildren().addAll(taskCheckBox, taskField, deleteButton, saveButton);
    }

    public ToDoItemInstance(TaskModel taskModel) {
        super(10);

        setPadding(new Insets(5));
        setStyle("-fx-background-color: lightblue; -fx-border-color: gray;");

        taskField = createTaskField(taskModel);
        taskCheckBox = createCheckBox(taskModel);
        deleteButton = createDeleteButton(taskModel);


        getChildren().addAll(taskCheckBox, taskField, deleteButton);
    }

    public ToDoItemInstance() {
        super(10);
        taskModel = todoListService.createTaskItem("", false);

        setPadding(new Insets(5));
        setStyle("-fx-background-color: lightblue; -fx-border-color: gray;");

        // Task text field
        taskField = createTaskField(taskModel);
        taskCheckBox = createCheckBox(taskModel);
        deleteButton = createDeleteButton(taskModel);

        getChildren().addAll(taskCheckBox, taskField, deleteButton);
    }

    public Button getDeleteButton() {
        return deleteButton;
    }
    public Button getSaveButton() {
        return saveButton;
    }

    public String getTaskText() {
        return taskField.getText();
    }

    public boolean isTaskChecked() {
        return taskCheckBox.isSelected();
    }

    private TextField createTaskField(TaskModel taskModel) {
        taskField = new TextField(taskModel.getTaskContent());
        taskField.setPrefWidth(300);
        taskField.setDisable(taskModel.isCompleted());

        taskField.textProperty().addListener((observable, oldValue, newValue) -> {
            todoListService.setTaskContent(newValue, this.taskModel); // Update TaskModel
            todoListService.saveList(); // Persist changes
        });
        return taskField;
    }

    private Button createDeleteButton(TaskModel taskModel) {
        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e-> {
            ((VBox)getParent()).getChildren().remove(this);
            todoListService.deleteTaskItem(taskModel.getTaskContent(), taskModel.isCompleted());
            todoListService.saveList();
        });
        return deleteButton;
    }

    private CheckBox createCheckBox(TaskModel taskModel) {
        taskCheckBox = new CheckBox();
        taskCheckBox.setSelected(taskModel.isCompleted());
        taskCheckBox.setOnAction(e -> {
            taskField.setDisable(taskCheckBox.isSelected());
            todoListService.setCompleted(taskCheckBox.isSelected(), taskModel); // Update TaskModel
            todoListService.saveList(); // Persist changes
        });

        return taskCheckBox;
    }
}
