package io.serateam.stewboo.core.services.todolist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskList
{
    private static final String FILE_PATH = "todoItems.txt";

    public static List<TaskModel> loadTasks() throws IOException {
        File file = new File(FILE_PATH);
        List<TaskModel> taskModels = new ArrayList<>();

        if(file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while((line = br.readLine()) != null) {
                    TaskModel taskModel = TaskModel.fromFileString(line);
                    if(taskModel != null) {
                        taskModels.add(taskModel);
                    }
                }
            }
        }
        return taskModels;
    }

    public static void saveTasks(List<TaskModel> taskModels) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {

            for(TaskModel taskModel : taskModels) {
                bw.write(taskModel.toFileString());
                bw.newLine();
            }
        }
    }
}

//public class TaskList {
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