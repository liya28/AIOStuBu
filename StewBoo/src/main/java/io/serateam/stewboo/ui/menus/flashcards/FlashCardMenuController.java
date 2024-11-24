package io.serateam.stewboo.ui.menus.flashcards;

import com.jfoenix.controls.JFXButton;
import io.serateam.stewboo.core.services.flashcard.Deck;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import io.serateam.stewboo.ui.SharedVariables;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class FlashCardMenuController implements IMenu {
    @FXML private ListView<Deck> deckListView;
    @FXML private ObservableList<Deck> observableDeckList;
    @FXML private JFXButton deleteDeckButton;

    private final FlashCardService service = FlashCardService.getInstance();

    @FXML
    public void initialize() {
        observableDeckList = FXCollections.observableArrayList(service.getDecks());
        deckListView.setItems(observableDeckList);

        deckListView.setCellFactory(listView -> new ListCell<Deck>() {
            @Override
            protected void updateItem(Deck deck, boolean empty) {
                super.updateItem(deck, empty);
                setText(empty || deck == null ? null : deck.getName() + " - FlashCards: " + deck.getflashCardsCount());
            }
        });

        deleteDeckButton.disableProperty().bind(deckListView.getSelectionModel().selectedItemProperty().isNull());

        deckListView.setOnMouseClicked(event ->
        {
            if(event.getClickCount() == 2)
            {
                Deck n_deck = deckListView.getSelectionModel().getSelectedItem();
                if(n_deck != null)
                {
                    openFlashCard(n_deck);
                }
            }
        });
    }

    @FXML
    private void addDeck()
    {
        try {
            FXMLLoader loader = new FXMLLoader(SharedVariables.url_path_flashcardsDeckFxml);
            Parent root = loader.load();

            CardCreationController controller = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            String deckName = controller.getDeckName();
            if (deckName != null && !deckName.trim().isEmpty())
            {
                service.addDeck(deckName);
                Deck newDeck = service.getDeckByName(deckName);
                observableDeckList.add(newDeck);
                openCardCreationView(newDeck);
            }
            else
            {
                showAlert("Error", "Deck name cannot be empty.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFlashCard(Deck deck)
    {
        try {
            FXMLLoader loader = new FXMLLoader(SharedVariables.url_path_flashcardsCardFxml);
            Parent root = loader.load();

            CardViewController con = loader.getController();
            con.setflashcards(deck.getFlashCards());
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            //deckListView.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCardCreationView(Deck deck)
    {
        try {
            FXMLLoader loader = new FXMLLoader(SharedVariables.url_path_flashcardsCardCreationFxml);
            Parent root = loader.load();

            CardCreationController controller = loader.getController();
            controller.setDeck(deck, this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Cards");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateflashcardCount()
    {
        deckListView.refresh();
    }

    private void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void deleteDeck()
    {
        Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();
        if (selectedDeck != null)
        {
            observableDeckList.remove(selectedDeck);
            service.deleteDeck(selectedDeck);
        }
        else
        {
            showAlert("Error", "No deck selected to delete.");
        }
    }
}
