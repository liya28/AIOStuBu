package io.serateam.stewboo.ui.menus.todolist;

import io.serateam.stewboo.core.services.todolist.TaskModel;
import io.serateam.stewboo.core.services.todolist.TodoListService;

import io.serateam.stewboo.ui.SharedVariables;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ToDoItemInstance extends HBox
{
    private TaskModel taskModel;
    private TextField taskField;
    private CheckBox taskCheckBox;
    private Button deleteButton;
    private final TodoListService todoListService = TodoListService.getInstance();

    /**
     *  Used when initializing a to-do item where we already know the contents of the model.
     *  For example, when loading an item from a list.
     * @param taskModel
     */
    public ToDoItemInstance(TaskModel taskModel)
    {
        super(10);
        this.taskModel = taskModel;
        createItemInList();
    }

    /**
     * Used when initializing a to-do item with a new model. For example, when creating a new item.
     */
    public ToDoItemInstance()
    {
        super(10);
        taskModel = todoListService.createTaskItem("", false);
        createItemInList();
    }

    private void createItemInList()
    {
        getStyleClass().add("styled-vbox");
        getStylesheets().add(getClass().getResource(SharedVariables.path_directoryCSS_TodoList).toExternalForm());

        // Task text field
        taskField = createTaskField(taskModel);
        taskCheckBox = createCheckBox(taskModel);
        deleteButton = createDeleteButton(taskModel);

        getChildren().addAll(taskCheckBox, taskField, deleteButton);
    }

    public Button getDeleteButton()
    {
        return deleteButton;
    }

    public String getTaskText()
    {
        return taskField.getText();
    }

    public boolean isTaskChecked()
    {
        return taskCheckBox.isSelected();
    }

    private TextField createTaskField(TaskModel taskModel)
    {
        taskField = new TextField(taskModel.getTaskContent());
        taskField.getStyleClass().add("task-field");

        taskField.getStylesheets().add(getClass().getResource(SharedVariables.path_directoryCSS_TodoList).toExternalForm());

        taskField.setPrefWidth(740);
        taskField.setDisable(taskModel.isCompleted());



        taskField.textProperty().addListener((observable, oldValue, newValue) -> {
            todoListService.setTaskContent(newValue, this.taskModel); // Update TaskModel
            todoListService.saveList(); // Persist changes
        });
        return taskField;
    }

    private Button createDeleteButton(TaskModel taskModel)
    {
        deleteButton = new Button();
        deleteButton.getStyleClass().add("delete-button");

        Image deleteIcon = new Image(getClass().getResourceAsStream("/io/serateam/stewboo/ui/images/exit.png"));
        ImageView iconView = new ImageView(deleteIcon);
        iconView.setFitHeight(20);
        iconView.setFitWidth(20);

        deleteButton.setGraphic(iconView);
        deleteButton.getStylesheets().add(getClass().getResource(SharedVariables.path_directoryCSS_TodoList).toExternalForm());

        deleteButton.setOnAction(e-> {
            ((VBox)getParent()).getChildren().remove(this);
            todoListService.deleteTaskItem(taskModel.getTaskContent(), taskModel.isCompleted());
            todoListService.saveList();
        });
        return deleteButton;
    }

    private CheckBox createCheckBox(TaskModel taskModel)
    {
        taskCheckBox = new CheckBox();
        taskCheckBox.getStyleClass().add("check-box");

        taskCheckBox.getStylesheets().add(getClass().getResource(SharedVariables.path_directoryCSS_TodoList).toExternalForm());

        taskCheckBox.setSelected(taskModel.isCompleted());
        taskCheckBox.setOnAction(e -> {
            taskField.setDisable(taskCheckBox.isSelected());
            todoListService.setCompleted(taskCheckBox.isSelected(), taskModel); // Update TaskModel
            todoListService.saveList(); // Persist changes
        });
        return taskCheckBox;
    }
    private void deleteAnimation() {

    }
}
