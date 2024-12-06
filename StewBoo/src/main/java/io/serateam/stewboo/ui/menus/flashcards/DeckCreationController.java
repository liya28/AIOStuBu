package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.ui.utility.ControllerAlerter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.InputMismatchException;

public class DeckCreationController {

    @FXML private AnchorPane deckName;
    @FXML private TextField deckNameField;

    private boolean isCancelled = false;

    @FXML
    public String getDeckName() {
        if (isCancelled) {
            return null;
        }

        try {
            String deckName = deckNameField.getText();
            if (deckName != null && !deckName.isEmpty()) {
                if (!DeckNameChecker(deckName)) {
                    throw new InputMismatchException();
                } else {
                    close();
                    return deckName;
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch (InputMismatchException ime) {
            ControllerAlerter.showError("Error", "Deck name cannot exceed 20 characters", "Please enter a deck name within 20 characters");
            deckNameField.setText("");
        } catch (IllegalArgumentException iae) {
            ControllerAlerter.showError("Error", "Deck name cannot be empty", "Please enter a deck name");
            deckNameField.setText("");
        }

        return null;
    }

    private void close() {
        Stage stage = (Stage) deckNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        isCancelled = true;
        close();
    }

    private boolean DeckNameChecker(String deckName) {
        int count = 0;
        for (int i = 0; i < deckName.length(); i++) {
            if (deckName.charAt(i) != ' ') {
                count++;
            }
        }
        return count <= 20;  // Ensure name is less than or equal to 20 characters
    }
}
