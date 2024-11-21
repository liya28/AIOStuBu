package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Deck;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;

public class FlashCardMenuController implements IMenu {
    @FXML private ListView<Deck> deckListView;
    @FXML private ObservableList<Deck> observableDeckList;
    @FXML private Button deleteDeckButton;
    ActionEvent event;
    @FXML private TextField textField;
    String deckName;

    private final FlashCardService service = new FlashCardService();

    private void loadView(String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();
            Scene scene = new Scene(view);
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize()
    {
        service.initializeService(); //FOR JSON Loading decks
        observableDeckList = FXCollections.observableList(service.getDecks());
        deckListView.setItems(observableDeckList);

            deckListView.setCellFactory(listView -> new ListCell<Deck>()
            {
                @Override
                protected void updateItem(Deck deck, boolean empty)
                {
                    super.updateItem(deck, empty);
                    if(empty || deck == null)
                    {
                        setText(null);
                    }
                    else
                    {
                        setText(deck.getName() + " - Flashcards: " + deck.getflashCardsCount());
                    }
                }
            });

        deckListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();

                if (selectedDeck != null) {
                    try {
                        openCardView(selectedDeck);
                    } catch (Exception e) {

                    }
                }
            }
        });

        deleteDeckButton.disabledProperty().and(deckListView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void openCardView(Deck deck) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("flashcards/flashcards_cardview.fxml"));
            Parent root = loader.load();

            CardViewController controller = loader.getController();
            controller.setFlashcards(deck.getflashCards());
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }catch (Exception e) {

        }
    }

    public void updateFlashCardCount()
    {
        deckListView.refresh();
    }

    @FXML
    String getDeckName()
    {
        return deckName;
    }

    @FXML
    void closeDeck()
    {
        deckName = textField.getText();
        Stage currentStage = (Stage) textField.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void addDeck()
    {
        loadView("flashcards/flashcards_deckName.fxml");
        String deck = getDeckName();
        if(!deck.isEmpty())
        {
            service.addDeck(deck);
            Deck newDeck = service.getDeckByName(deck);
            observableDeckList.add(newDeck);
            openCardView(newDeck);
        }
    }


    @FXML
    private void backButton(ActionEvent event)
    {

    }

    @FXML
    private void deleteDeck()
    {
        Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();

        if(selectedDeck != null)
        {
            observableDeckList.remove(selectedDeck);

            selectedDeck.clearflashcards();

            deckListView.refresh();
        }
        else
        {
            System.out.println("No deck selected to delete");
        }
    }
}
