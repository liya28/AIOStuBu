package io.serateam.stewboo.ui.menus.todolist;


import io.serateam.stewboo.core.services.todolist.TodoListService;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToDoItemInstance extends HBox {
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
            todoListService.setTaskContent(newValue);
            try {
                todoListService.setTaskContent(taskField.getText());
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

    public ToDoItemInstance() {

        super(10); // 10 pixels spacing
        setPadding(new Insets(5));
        setStyle("-fx-background-color: lightblue; -fx-border-color: gray;");

        // Task text field
        taskField = new TextField(todoListService.getTaskContent());
        taskField.setPrefWidth(300);
        taskField.setDisable(todoListService.isCompleted());

        taskField.textProperty().addListener((observable, oldValue, newValue) -> {
            todoListService.setTaskContent(newValue);
                try {
                    todoListService.setTaskContent(taskField.getText());
                    System.out.println("Task saved: " + newValue);
                } catch (Exception ex) {
                    System.err.println("Error saving task: ");
                    ex.printStackTrace();
                }
            // Restart delay on each keystroke
        });

        // Checkbox
        taskCheckBox = new CheckBox();
        taskCheckBox.setOnAction(e -> {
            taskField.setDisable(taskCheckBox.isSelected());
            todoListService.setCompleted(taskCheckBox.isSelected());
        });

        // Delete button
        deleteButton = new Button("Delete");
        saveButton = new Button("Save");

        getChildren().addAll(taskCheckBox, taskField, deleteButton, saveButton);
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

    public String setTaskField(String taskContent) {
        return taskContent;
    }
}
