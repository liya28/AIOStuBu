package io.serateam.stewboo.ui.menus.todolist;


import io.serateam.stewboo.core.services.todolist.TodoListService;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class ToDoListInstance extends HBox {
    private TextField taskField;
    private CheckBox taskCheckBox;
    private Button deleteButton;
    private Button saveButton;

    public ToDoListInstance(TodoListService taskModel) {

        super(10); // 10 pixels spacing
        setPadding(new Insets(5));
        setStyle("-fx-background-color: lightblue; -fx-border-color: gray;");

        // Task text field
        taskField = new TextField(taskModel.getTaskContent());
        taskField.setPrefWidth(300);
        taskField.setDisable(taskModel.isCompleted());

        PauseTransition pause = new PauseTransition(Duration.millis(500)); // Delay to batch keystrokes
        taskField.textProperty().addListener((observable, oldValue, newValue) -> {
            taskModel.setTaskContent(newValue); // Update model
            pause.setOnFinished(e -> {
                try {
                    taskModel.setTaskContent(taskField.getText());
                    System.out.println("Task saved: " + newValue);
                } catch (Exception ex) {
                    System.err.println("Error saving task: ");
                    ex.printStackTrace();
                }
            });
            pause.playFromStart(); // Restart delay on each keystroke
        });

        // Checkbox
        taskCheckBox = new CheckBox();
        taskCheckBox.setOnAction(e -> {
            taskField.setDisable(taskCheckBox.isSelected());
            taskModel.setCompleted(taskCheckBox.isSelected());
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
}
