package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Deck;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

class FlashCardMenuController implements IMenu {
    @FXML private ListView<Deck> deckListView;
    @FXML private ObservableList<Deck> observableDeckList;
    @FXML private Button deleteDeckButoon;

    @FXML
    public void initialize()
    {
        if(deckListView != null)
        {
            List<Deck> decks = new ArrayList<>();
            observableDeckList = FXCollections.observableList(decks);
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
        }

        assert deckListView != null;
        deckListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();

                if (selectedDeck != null) {
                    try {

                    } catch (Exception e) {

                    }
                }
            }
        });

        deleteDeckButoon.disabledProperty().and(deckListView.getSelectionModel().selectedItemProperty().isNull());
    }

    public void updateFlashCardCount()
    {
        deckListView.refresh();
    }

    @FXML
    private void addDeck()
    {

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
