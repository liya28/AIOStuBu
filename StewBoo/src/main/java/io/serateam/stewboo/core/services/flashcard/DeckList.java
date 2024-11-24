package io.serateam.stewboo.core.services.flashcard;

import io.serateam.stewboo.core.utility.ISerializable;

import java.util.ArrayList;
import java.util.List;

class DeckList implements ISerializable {
    private static DeckList instance;
    private List<Deck> decks;

    private DeckList() {
        decks = new ArrayList<>();
    }

    public static DeckList getInstance() {
        if(instance == null) {
            instance = new DeckList();
        }
        return instance;
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public void deleteDeck(Deck deck) {
        decks.remove(deck);
    }

    List<Deck> getDecks(){
        return decks;
    }

}
