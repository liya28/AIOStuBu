package io.serateam.stewboo.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotesController {

    @FXML private ComboBox<Integer> fontSizeComboBox;
    @FXML private ColorPicker colorPicker;
    @FXML private ListView<String> topicListView;
    @FXML private Label statusBar;
    @FXML private Label titleLabel;
    @FXML private ComboBox<String> fontComboBox;

    // Menu Items
    @FXML private MenuItem newNote;
    @FXML private MenuItem saveNote;
    @FXML private MenuItem openNote;
    @FXML private MenuItem exitApp;

    // Formatting Buttons
    @FXML private Button boldButton;
    @FXML private Button italicButton;
    @FXML private Button underlineButton;

    // Text Editor
    @FXML private TextField inputField;
    @FXML private TextArea textArea;
    @FXML private TextField titleTextField;
    @FXML private TextField textField;
    @FXML private TextFlow textFlow;

    private ScheduledExecutorService autosaveScheduler;
    private File activeFile;

    private void startAutosave() {
        autosaveScheduler = Executors.newSingleThreadScheduledExecutor();
        autosaveScheduler.scheduleAtFixedRate(() -> {
            autosaveNote();
        }, 0, 1, TimeUnit.MINUTES); // Autosave every 1 minute
    }

    private void stopAutosave() {
        if (autosaveScheduler != null && !autosaveScheduler.isShutdown()) {
            autosaveScheduler.shutdown();
        }
    }

    private void autosaveNote() {
        File fileToSave;
        if (activeFile != null) {
            fileToSave = activeFile;
        } else {
            fileToSave = new File("autosave/autosave_note.txt");
            fileToSave.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(fileToSave)) {
            writer.write(textArea.getText());
            Platform.runLater(() -> statusBar.setText("Autosaved at: " + java.time.LocalTime.now()));
        } catch (IOException e) {
            // Autosave errors are not critical but should be logged
            System.err.println("Autosave failed: " + e.getMessage());
        }
    }


    @FXML
    public void initialize() {
        initializeComboBoxes();
        initializeEventHandlers();
        startAutosave();
        statusBar.setText("Welcome! Start editing...");
    }

    private void initializeComboBoxes() {
        fontSizeComboBox.getItems().addAll(10, 12, 14, 16, 18, 20, 24, 28, 32);
        fontSizeComboBox.setValue(14); // Default font size

        fontComboBox.getItems().addAll("Arial", "Verdana", "Times New Roman", "Courier New", "Tahoma");
        fontComboBox.setValue("Arial"); // Default font

        fontSizeComboBox.setOnAction(e -> applyTextStyles());
        fontComboBox.setOnAction(e -> applyTextStyles());
        colorPicker.setOnAction(e -> applyTextStyles());
    }

    private void initializeEventHandlers() {
        // Formatting Buttons
        boldButton.setOnAction(e -> onBoldClicked());
        italicButton.setOnAction(e -> onItalicClicked());
        underlineButton.setOnAction(e -> onUnderlineClicked());

        newNote.setOnAction(e -> newNoteAction());
        saveNote.setOnAction(e -> saveNoteAction());
        openNote.setOnAction(e -> openNoteAction());
        exitApp.setOnAction(e -> System.exit(0));
    }

    private void toggleTextStyle(FontWeight weight, FontPosture posture) {
        String selectedText = textArea.getSelectedText();
        if (!selectedText.isEmpty()) {
            int start = textArea.getSelection().getStart();
            int end = textArea.getSelection().getEnd();

            String fontFamily = fontComboBox.getValue();
            double fontSize = fontSizeComboBox.getValue();
            Color color = colorPicker.getValue();

            Text text = new Text(selectedText);
            text.setFont(Font.font(fontFamily, weight, posture, fontSize)); // Apply weight, posture, and size
            text.setFill(color); // Apply selected color

            textArea.replaceText(start, end, text.getText());

        } else {
            statusBar.setText("No text selected for formatting!");
        }
    }

    @FXML
    private void onAddTopicClicked() {
        addTopic(); // Calls the method that opens the dialog and adds the topic
    }

    private void addTopic() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Topic");
        dialog.setHeaderText("Enter the new topic name");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(topic -> topicListView.getItems().add(topic));
    }

    @FXML
    private void onTextEntered() {
        String inputText = inputField.getText();

        if (!inputText.isEmpty()) {
            Text textNode = new Text(inputText + " ");
            String fontFamily = fontComboBox.getValue();
            double fontSize = Double.parseDouble(fontSizeComboBox.getValue().toString());
            Color color = colorPicker.getValue();

            textNode.setFont(Font.font(fontFamily, fontSize));
            textNode.setFill(color);

            // Add the new text to the TextFlow
            textFlow.getChildren().add(textNode);

            // Clear the input field
            inputField.clear();
        }
    }


    @FXML
    private void onBoldClicked() {
        int start = textArea.getSelection().getStart();
        int end = textArea.getSelection().getEnd();
        if (start != end) {
            textArea.setStyle("-fx-font-weight: bold;");
        }
    }


    @FXML
    private void onItalicClicked() {
        int start = textArea.getSelection().getStart();
        int end = textArea.getSelection().getEnd();
        if (start != end) {
            textArea.setStyle("-fx-font-style: italic;");
        }
    }

    @FXML
    private void onUnderlineClicked() {
        if (!textFlow.getChildren().isEmpty()) {
            Text lastText = (Text) textFlow.getChildren().get(textFlow.getChildren().size() - 1);
            lastText.setUnderline(!lastText.isUnderline()); // Toggle underline
        } else {
            statusBar.setText("No text selected for formatting!");
        }
    }

    @FXML
    private void onFontChanged() {
        applyTextStyles();
    }

    @FXML
    private void onFontSizeChanged() {
        applyTextStyles();
    }

    @FXML
    private void onTextColorChanged() {
        applyTextStyles();
    }

    private void applyTextStyles() {
        String font = fontComboBox.getValue();
        int size = fontSizeComboBox.getValue();
        Color color = colorPicker.getValue();

        String style = String.format("-fx-font-family: '%s'; -fx-font-size: %dpx; -fx-text-fill: #%s;",
                font, size, color.toString().substring(2, 8));
        textArea.setStyle(style);

        statusBar.setText(String.format("Applied font: %s, size: %d, color: %s", font, size, color));
    }

    @FXML
    private void newNoteAction() {
        textArea.clear();
        titleLabel.setText("Untitled Note");
        statusBar.setText("New note created.");
    }

    @FXML
    private void saveNoteAction() {
        String title = titleTextField.getText();
        if (title.equals("Untitled Note") || title.trim().isEmpty()) {
            showError("Error", "Please enter a topic name before saving.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName(title + ".txt"); // Set default file name to the title
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.write(textArea.getText());
                statusBar.setText("Note saved to " + file.getName());
            } catch (IOException e) {
                showError("Error saving file", e.getMessage());
            }
        }
    }


    @FXML
    private void openNoteAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                textArea.setText(content.toString());
                titleTextField.clear();
            } catch (IOException e) {
                showError("Error opening file", e.getMessage());
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onTopicClicked() {
        String selectedTopic = topicListView.getSelectionModel().getSelectedItem();
        if (selectedTopic != null) {
            loadNoteForTopic(selectedTopic);
        }
    }

    private void loadNoteForTopic(String topic) {
        File file = new File("notes/" + topic + ".txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                textArea.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.appendText(line + "\n");
                }
                titleLabel.setText(topic);
                statusBar.setText("Loaded note: " + topic);
            } catch (IOException e) {
                showError("Error loading note", e.getMessage());
            }
        } else {
            textArea.clear();
            titleLabel.setText(topic);
            statusBar.setText("New note for topic: " + topic);
        }
    }

    @FXML
    private void addTextToEditor() {
        String inputText = textField.getText();
        if (!inputText.isEmpty()) {
            Text textNode = new Text(inputText);
            textNode.setStyle("-fx-font-size: 14px;");
            textFlow.getChildren().add(textNode);
            textField.clear();
        }
    }
}
