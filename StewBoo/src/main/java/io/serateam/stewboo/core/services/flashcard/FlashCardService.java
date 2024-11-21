package io.serateam.stewboo.core.services.flashcard;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.SharedVariables;

import java.util.ArrayList;
import java.util.List;

public class FlashCardService implements IService {
    private static FlashCardService instance;
    private List<Deck> decks;

    private FlashCardService() {
        decks = new ArrayList<>();
    }

    public static FlashCardService getInstance() {
        if (instance == null) {
            instance = new FlashCardService();
        }
        return instance;
    }

    @Override
    public void initializeService() {
//        decks = JSONService.deserialize(SharedVariables.Path.flashcardJSON, this);
        //if (decks == null) {
            Deck sampleDeck = new Deck("Sample Deck");
            sampleDeck.addflashCard(new Card("What is 2 + 2?", "4"));
            sampleDeck.addflashCard(new Card("What is the capital of France?", "Paris"));
            decks.add(sampleDeck);
            //saveDecksToFile();
        //}
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public void addDeck(String deckName) {
        Deck newDeck = new Deck(deckName);
        decks.add(newDeck);
        //saveDecksToFile();
    }

    public Deck getDeckByName(String deckName) {
        return decks.stream().filter(deck -> deck.getName().equals(deckName)).findFirst().orElse(null);
    }

    public void deleteDeck(Deck deck) {
        decks.remove(deck);
        //saveDecksToFile();
    }

//    private void saveDecksToFile() {
//        JSONService.serializeAndWriteToFile(SharedVariables.Path.flashcardJSON, decks);
//    }
}
