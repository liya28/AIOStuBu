package io.serateam.stewboo.core.services.todolist;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.SharedVariables;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;

public class TodoListService extends HBox implements IService {

    private static TaskList list;
    private static TodoListService instance;
    private TextField taskField = null;
    private CheckBox taskCheckBox = null;
    private Button deleteButton = null;

    private TodoListService() {
        list = new TaskList();
    }
    public static TodoListService getInstance() {
        if(instance == null) {
            instance = new TodoListService();
        }
        return instance;
    }

    public TodoListService(TaskModel taskModel) {
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
        taskCheckBox.setSelected(taskModel.isCompleted());
        taskCheckBox.setOnAction(e -> taskField.setDisable(taskCheckBox.isSelected()));

        // Delete button
        deleteButton = new Button("Delete");

        getChildren().addAll(taskCheckBox, taskField, deleteButton);
    }

    @Override
    public void initializeService() {
        // Add meaningful initialization if needed
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public String getTaskText() {
        return taskField.getText();
    }

    public boolean isTaskChecked() {
        return taskCheckBox.isSelected();
    }

    public void updateTask(TaskModel taskModel) {
        taskModel.setTaskContent(getTaskText());
        taskModel.setCompleted(isTaskChecked());
    }

    private void saveList(TaskModel taskModel) throws IOException {
        // Update the task model with the text field content and checkbox status
        taskModel.setTaskContent(taskField.getText());
        taskModel.setCompleted(taskCheckBox.isSelected());

        // Call the save method to persist the task
//        saveTasks(Collections.singletonList(taskModel));  // Saving the single updated task
    }
    /////////////////////////


    public void createTaskItem(String content, boolean completed) {
        TaskModel model = new TaskModel(content, completed);
        addTask(model);
    }

    private void addTask(TaskModel taskModel)
    {
        list.addTask(taskModel);
    }

    public void saveList() {
        JSONService.serializeAndWriteToFile(SharedVariables.path_todoList, list);
    }
    // TODO: delete list
    // TODO: read list
}



//public class TodoListService {
//    @FXML
//    private ResourceBundle resources;
//
//    @FXML
//    private VBox taskContainer;
//
//    protected final List<Task> todoItems = new ArrayList<>();
//    private final String FILE_PATH = "todoItems.txt";
//
//    @FXML
//    private ListView<Task> taskListView;
//
//    @FXML
//    void initialize() {
//        try {
//            loadItems();
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//
//    }
//
//    protected void saveItems() {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
//            for (Task task : todoItems) {
//                task.saveItem(writer);// Call the new saveItem method
//            }
//            System.out.println("Saved");
//        } catch (IOException e) {
//            System.out.println("Err");
//            e.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("Error?");
//        }
//    }
//
//    private void loadItems() {
//        File file = new File(FILE_PATH);
//
//        // Check if file exists and is empty
//        if (file.exists() && file.length() == 0) {
//            loadDefaultPane(); // Load a default empty pane
//            return;
//        }
//
//        // If file has content, load the tasks
//        if (file.exists()) {
//            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    String[] parts = line.split(";");
//                    if (parts.length == 2) {
//                        String taskText = parts[0];
//                        boolean isChecked = Boolean.parseBoolean(parts[1]);
//                        Task task = new Task(taskContainer);
//                        task.setTaskText(taskText);
//                        task.setTaskChecked(isChecked);
//                        taskContainer.getChildren().add(0, task);
//                        todoItems.add(task);
//                    }
//                }
//
//                // If no tasks loaded, show default pane
//                if (todoItems.isEmpty()) {
//                    loadDefaultPane();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            // If the file does not exist, load default pane
//            loadDefaultPane();
//        }
//    }
//    @FXML
//    protected void onAddItem(ActionEvent event) {
//        try {
//            Task taskComponent = new Task(taskContainer);
//            taskContainer.getChildren().add(0, taskComponent);
//            todoItems.add(taskComponent);
//            saveItems();
//        } catch (Exception e) {
//            System.out.println("Error");
//        }
//
//    }
//
//    private void loadDefaultPane() {
//        try {
//            Task defaultTask = new Task(taskContainer);
//            defaultTask.setTaskText("No tasks available. Add a new task!");
//            defaultTask.setTaskChecked(false);
//            taskContainer.getChildren().add(defaultTask);
//            defaultTask.setDisable(true);
//        } catch (Exception e) {
//            System.out.println("Err");
//        }
//
//    }
//
//
//    @FXML
//    protected void onRemoveItem(Task task) {
//        todoItems.remove(task);
//        saveItems();
//    }
//
//    public void stop() {
//        saveItems();
//    }
//
//}