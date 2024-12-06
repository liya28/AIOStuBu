package io.serateam.stewboo.ui.menus.notes;

import io.serateam.stewboo.core.services.notes.NotesService;
import io.serateam.stewboo.ui.menus.IMenu;
import io.serateam.stewboo.ui.utility.ControllerAlerter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NotesController implements Initializable, IMenu
{
    @FXML private HTMLEditor htmlEditor;
    @FXML private ListView<String> listView_topics;
    @FXML private TextField textField_title;

    private static NotesService notesService;

    @Override
    public void initialize(URL url , ResourceBundle rb)
    {
        if (notesService == null)
        {
            notesService = NotesService.getInstance();
        }

        List<String> titles = notesService.getAllTitles();
        if (titles != null)
        {
            titles.forEach(title ->
            {
                if (!listView_topics.getItems().contains(title))
                {
                    listView_topics.getItems().add(title);
                }
            });
        }
        listView_topics.setOnMouseClicked(event -> openContent());
    }

    @FXML
    private void openContent()
    {
        String selectedTitle = listView_topics.getSelectionModel().getSelectedItem();

        if (selectedTitle != null)
        {
            String content = notesService.getContentByTitle(selectedTitle);
            if (content != null)
            {
                textField_title.setText(selectedTitle);
                htmlEditor.setHtmlText(content);
            }
            else
            {
                ControllerAlerter.showInfo("Note Selection", "Empty Note Content", "There is no content found.");
            }
        }
        else
        {
            ControllerAlerter.showInfo("Note Selection", "Invalid Selection", "There is no topic selected. Please try again.");
        }
    }

    @FXML
    public void createContent()
    {
        htmlEditor.setHtmlText("");
        textField_title.clear();
    }

    @FXML
    public void saveContent()
    {
        String title = handleFileName(textField_title.getText());
        String content = handleFileContent(htmlEditor.getHtmlText());

        if (title != null && content != null)
        {
            notesService.saveNote(title, content);
            refreshListView();
            createContent();
        }
    }

    private String handleFileContent(String content)
    {
        if (content.isEmpty() || content.trim().isEmpty() || content.equals("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>"))
        {
            ControllerAlerter.showError("Input Error", "Missing Required Field", "Content is required.");
            return null;
        }

        return content;
    }

    private String handleFileName(String title)
    {
        if (title.isEmpty())
        {
            ControllerAlerter.showError("Input Error", "Missing Required Field", "Title is required.");
            return null;
        }
        else if (!isValidFileName(title))
        {
            String sanitizedTitle = sanitizeFileName(title);

            if (sanitizedTitle != null)
            {
                ControllerAlerter.showInfo("Title Renamed", "Invalid Title", "The title was invalid and has been renamed to: " + sanitizedTitle + ".");
            }

            return sanitizedTitle;
        }
        return title;
    }

    private boolean isValidFileName(String title)
    {
        // Disallow reserved characters: \ / : * ? " < > | and leading/trailing spaces
        String regex = "^(?!\\s)[^\\\\/:*?\"<>|]+(?<!\\s)$";
        return title.matches(regex);
    }

    private String sanitizeFileName(String title)
    {
        // Replace invalid characters with an underscore
        return title.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
    }

    @FXML
    private void deleteNote()
    {
        String selectedTitle = listView_topics.getSelectionModel().getSelectedItem();

        if (selectedTitle != null)
        {
            notesService.deleteNoteByTitle(selectedTitle);
            refreshListView();
            createContent();
        }
        else
        {
            ControllerAlerter.showInfo("Note Deletion", "Invalid note selected", "There is no topic selected. Please try again.");
        }
    }

    private void refreshListView()
    {
        listView_topics.getItems().setAll(notesService.getAllTitles());
    }
}
