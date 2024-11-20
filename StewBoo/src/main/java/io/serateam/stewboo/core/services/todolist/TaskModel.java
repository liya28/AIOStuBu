package io.serateam.stewboo.core.services.todolist;


public class TaskModel {
    private String taskContent;
    private boolean isCompleted;

    public TaskModel(String text, boolean isCompleted)
    {
        this.taskContent = text;
        this.isCompleted = isCompleted;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return taskContent + " (Checked: " + isCompleted + ")";
    }

    public String toFileString() {
        return taskContent + ";" + isCompleted;
    }

    public static TaskModel fromFileString(String line) {
        String[] parts = line.split(";");
        if(parts.length == 2) {
            String taskText = parts[0];
            boolean isChecked = Boolean.parseBoolean(parts[1]);
            return new TaskModel(taskText, isChecked);
        }
        return null;
    }
}


////public class Task extends Pane {
////
////    @FXML
////    private TextField taskField;
////
////    @FXML
////    private CheckBox taskCheckBox;
////
////    @FXML
////    private HBox tasks;
////
////    @FXML
////    private Button taskDelete;
////
////    private String TaskComponentCSS;
////
////    public Task(VBox parentContainer) {
////        Scanner sc = new Scanner(System.in);
////        tasks = new HBox(10); // 10 pixels of spacing between elements
////
////
////        taskField = new TextField();
////        taskField.setPrefSize(300, 40);
////        taskField.setPromptText("Enter Task Here");
////        try {
////            taskField.textProperty().addListener(e -> {// Remove from UI
////                if (parentContainer.getScene() != null) {
////                    Object userData = parentContainer.getScene().getUserData();
////                    if (userData instanceof TaskList) {
////                        TaskList controller = (TaskList) userData;
////                        controller.saveItems();
////                    }
////                }
////            });
////        } catch (Exception e) {
////            System.out.println("Something went wrong");
////        }
////
////        taskCheckBox = new CheckBox();
////        taskCheckBox.setPrefSize(20, 20); // Optional: set size if needed
////        taskCheckBox.setOnAction( e -> {
////            if(taskCheckBox.isSelected()) {
////                String tasktext = taskField.getText().replaceAll("<[^>]*", "");
////
////                taskField.setText("<html><s>" + tasktext + "</s></html>");
////            } else if(!taskCheckBox.isSelected()) {
////                String tasktext = taskField.getText().replaceAll("<[^>]*", "");
////
////                taskField.setText(tasktext);
////            }
////        });
////
////        taskDelete = new Button("Delete");
////        try {
////            taskDelete.setOnAction( e -> {
////                parentContainer.getChildren().remove(this); // Remove from UI
////                if (parentContainer.getScene().getUserData() instanceof TaskList) {
////                    TaskList controller = (TaskList) parentContainer.getScene().getUserData();
////                    controller.onRemoveItem(this);
////                }
////            });
////        } catch (Exception e) {
////            System.out.println("Something went wrong");
////        }
////
////
////        // Add components to HBox
////        tasks.getChildren().addAll(taskCheckBox, taskField, taskDelete);
//
//        // Add HBox to this Pane
//        this.setMinSize(200, 50);
//        this.setPrefSize(200, 50);
//        this.setMaxSize(200, 50);
//
//        this.getChildren().add(tasks);
//
//
//        // Optional: Style the HBox for better visibility
//        tasks.setStyle("-fx-padding: 5; -fx-background-color: lightblue;");
//
//
//    }
//
//    public TextField getTaskField() {
//        return taskField;
//    }
//
//    public String getTaskText() {
//        return taskField.getText();
//    }
//
//    public boolean isTaskChecked() {
//        return taskCheckBox.isSelected();
//    }
//
//    public void setTaskText(String text) {
//        taskField.setText(text);
//    }
//
//    public void setTaskChecked(boolean checked) {
//        taskCheckBox.setSelected(checked);
//        taskField.setStyle(checked ? "-fx-strikethrough: true;" : "-fx-strikethrough: false;");
//
//    }
//
//    public void saveItem(BufferedWriter writer) throws IOException {
//        writer.write(getTaskText() + ";" + isTaskChecked());
//        writer.newLine();
//    }
//
//
//}
