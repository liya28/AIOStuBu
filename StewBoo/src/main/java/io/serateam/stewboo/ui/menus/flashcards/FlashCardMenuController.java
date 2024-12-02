package io.serateam.stewboo.ui.menus.flashcards;

import com.jfoenix.controls.JFXButton;
import io.serateam.stewboo.core.services.flashcard.Deck;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import io.serateam.stewboo.ui.utility.ControllerAlerter;
import io.serateam.stewboo.ui.SharedVariables;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.IOException;

public class FlashCardMenuController implements IMenu
{
    @FXML private ListView<Deck> deckListView;
    @FXML private ObservableList<Deck> observableDeckList;
    @FXML private JFXButton deleteDeckButton;
    @FXML private AnchorPane anchor_bg;

    private final FlashCardService service = FlashCardService.getInstance();

    @FXML
    public void initialize()
    {
        SharedVariables.flashCardMenuController = this;
        observableDeckList = FXCollections.observableArrayList(service.getDecks());
        deckListView.setItems(observableDeckList);

        deckListView.setCellFactory(listView -> new ListCell<Deck>()
        {
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

        anchor_bg.setOnMouseClicked(mouseEvent -> {
            deckListView.getSelectionModel().clearSelection();
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
            if (deckName != null && !deckName.trim().isEmpty() && DeckNameChecker(deckName))
            {
                Deck newDeck = new Deck(deckName);
                service.addDeck(newDeck);
                observableDeckList.add(newDeck);
                openCardCreationView(newDeck);
            }
            else if(deckName != null && !deckName.trim().isEmpty() && !DeckNameChecker(deckName))
            {
                ControllerAlerter.showError("Error", "Deck name cannot exceed 20 characters", "Please enter a deck name within 20 characters");
            }
            else
            {
                ControllerAlerter.showError("Error", "Deck name cannot be empty.", "Please input a deckname");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean DeckNameChecker(String deckName)
    {
        int count = 0;
        for (int i = 0; i < deckName.length(); i++)
        {
            if(deckName.charAt(i) != ' ')
            {
                count++;
            }
        }
        if(count >= 20) {
            return false;
        }
        return true;
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

    @FXML
    private void deleteDeck()
    {
        Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();
        if (selectedDeck != null)
        {
            observableDeckList.remove(selectedDeck);
            service.deleteDeck(selectedDeck);
            deckListView.getSelectionModel().clearSelection();
        }
        else
        {
            ControllerAlerter.showError("Error", "No deck selected to delete.", "Please select a deck to be deleted");
        }
    }
}
