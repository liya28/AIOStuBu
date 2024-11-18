package io.serateam.stewboo.core.services.todolist;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Task extends Pane {

    @FXML
    private TextField taskField;

    @FXML
    private CheckBox taskCheckBox;

    @FXML
    private HBox tasks;

    @FXML
    private Button taskDelete;

    private String TaskComponentCSS;

    public Task(VBox parentContainer) {
        Scanner sc = new Scanner(System.in);
        tasks = new HBox(10); // 10 pixels of spacing between elements




        taskField = new TextField();
        taskField.setPrefSize(300, 40);
        taskField.setPromptText("Enter Task Here");
        try {
            taskField.textProperty().addListener(e -> {// Remove from UI
                if (parentContainer.getScene() != null) {
                    Object userData = parentContainer.getScene().getUserData();
                    if (userData instanceof TaskList) {
                        TaskList controller = (TaskList) userData;
                        controller.saveItems();
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }

        taskCheckBox = new CheckBox();
        taskCheckBox.setPrefSize(20, 20); // Optional: set size if needed
        taskCheckBox.setOnAction( e -> {
            if(taskCheckBox.isSelected()) {
                String tasktext = taskField.getText().replaceAll("<[^>]*", "");

                taskField.setText("<html><s>" + tasktext + "</s></html>");
            } else if(!taskCheckBox.isSelected()) {
                String tasktext = taskField.getText().replaceAll("<[^>]*", "");

                taskField.setText(tasktext);
            }
        });

        taskDelete = new Button("Delete");
        try {
            taskDelete.setOnAction( e -> {
                parentContainer.getChildren().remove(this); // Remove from UI
                if (parentContainer.getScene().getUserData() instanceof TaskList) {
                    TaskList controller = (TaskList) parentContainer.getScene().getUserData();
                    controller.onRemoveItem(this);
                }
            });
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }


        // Add components to HBox
        tasks.getChildren().addAll(taskCheckBox, taskField, taskDelete);

        // Add HBox to this Pane
        this.setMinSize(200, 50);
        this.setPrefSize(200, 50);
        this.setMaxSize(200, 50);

        this.getChildren().add(tasks);


        // Optional: Style the HBox for better visibility
        tasks.setStyle("-fx-padding: 5; -fx-background-color: lightblue;");


    }

    public TextField getTaskField() {
        return taskField;
    }

    public String getTaskText() {
        return taskField.getText();
    }

    public boolean isTaskChecked() {
        return taskCheckBox.isSelected();
    }

    public void setTaskText(String text) {
        taskField.setText(text);
    }

    public void setTaskChecked(boolean checked) {
        taskCheckBox.setSelected(checked);
        taskField.setStyle(checked ? "-fx-strikethrough: true;" : "-fx-strikethrough: false;");

    }

    public void saveItem(BufferedWriter writer) throws IOException {
        writer.write(getTaskText() + ";" + isTaskChecked());
        writer.newLine();
    }


}
