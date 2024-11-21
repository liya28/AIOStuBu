package io.serateam.stewboo.core.services.flashcard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Deck {
    private String name;
    private ObservableList<Card> flashCards;

    public Deck(String name) {
        this.name = name;
        this.flashCards = FXCollections.observableArrayList();
    }

    public String getName() {
        return name;
    }

    public ObservableList<Card> getFlashCards() {
        return flashCards;
    }

    public int getflashCardsCount() {
        return flashCards.size();
    }

    public void addflashCard(Card card) {
        flashCards.add(card);
    }

    public void removeflashCard(Card card) {
        flashCards.remove(card);
    }
}
