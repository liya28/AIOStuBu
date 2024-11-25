package io.serateam.stewboo.ui.menus;

import io.serateam.stewboo.core.services.notes.NotesService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import java.util.List;

public class NotesController {
    @FXML private HTMLEditor htmlEditor;
    @FXML private ListView<String> listView_topics;
    @FXML private TextField textField_title;

    private final NotesService notesService = new NotesService();

    public void initialize() {
        NotesService.initializeNotesService();
        List<String> titles = notesService.getAllTitles();
        if (titles != null) {
            titles.forEach(title -> {
                if (!listView_topics.getItems().contains(title)) {
                    listView_topics.getItems().add(title);
                }
            });
        }
        listView_topics.setOnMouseClicked(event -> openContent());
    }

    @FXML
    private void openContent() {
        String selectedTitle = listView_topics.getSelectionModel().getSelectedItem();

        if (selectedTitle != null) {
            String content = notesService.getContentByTitle(selectedTitle);
            if (content != null) {
                textField_title.setText(selectedTitle);
                htmlEditor.setHtmlText(content);
            } else {
                showError("Note Selection", "Note not found.");
            }
        } else {
            showError("Topic Selection", "No topic selected.");
        }
    }

    @FXML
    public void createContent() {
        htmlEditor.setHtmlText("");
        textField_title.clear();
    }

    @FXML
    public void saveContent() {
        String title = textField_title.getText();
        String content = htmlEditor.getHtmlText();

        if (title.isEmpty() || content.isEmpty()) {
            showError("Input Error", "Both title and content are required.");
        } else {
            notesService.saveNote(title, content);
            refreshListView();
            createContent(); // Clear fields after saving
        }
    }

    @FXML
    private void deleteNote() {
        String selectedTitle = listView_topics.getSelectionModel().getSelectedItem();

        if (selectedTitle != null) {
            notesService.deleteNoteByTitle(selectedTitle);
            refreshListView();
            createContent();
        } else {
            showError("Note Deletion", "No topic selected.");
        }
    }

    private void refreshListView() {
        listView_topics.getItems().setAll(notesService.getAllTitles());
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
