package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Deck;
import io.serateam.stewboo.ui.menus.IMenu;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;

class FlashCardMenu implements IMenu {
    @FXML private ListView<Deck> deckListView;
    @FXML private ObservableList<Deck> observableDeckList;
    @FXML private Button deleteDeckButoon;

    @FXML
    public void initialize()
    {

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

    }
}
