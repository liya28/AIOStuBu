package io.serateam.stewboo.ui.menus;

import io.serateam.stewboo.core.services.todolist.TodoListService;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class TodoListMenu implements IMenu {
    private final TextField taskField;
    private final CheckBox taskCheckBox;
    private final Button taskDelete;

    public TodoListMenu(TodoListService controller, VBox parentContainer) {
        super(); // Spacing between elements

        taskField = new TextField(controller.getTask().getTaskContent());
        taskField.setPrefSize(300, 40);
        taskField.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.getTask().setTaskContent(newValue);
        });

        taskCheckBox = new CheckBox();
        taskCheckBox.setSelected(controller.getTask().isCompleted());
        taskCheckBox.setOnAction(e -> controller.toggleCheck());

        taskDelete = new Button("Delete");
        taskDelete.setOnAction(e -> {
            parentContainer.getChildren().remove(this); // Remove UI component
            // Notify parent container or controller if needed
        });

        // Add components to the HBox
        this.getChildren().addAll(taskCheckBox, taskField, taskDelete);

        // Optional: Style the HBox
        this.setStyle("-fx-padding: 5; -fx-background-color: lightblue;");
    }

    public void updateView(TodoListService controller) {
        taskField.setText(controller.getTask().getTaskContent());
        taskCheckBox.setSelected(controller.getTask().isCompleted());
    }
}
