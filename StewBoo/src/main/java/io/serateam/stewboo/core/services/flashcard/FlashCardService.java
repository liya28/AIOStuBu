package io.serateam.stewboo.core.services.flashcard;

import io.serateam.stewboo.core.services.IService;
import io.serateam.stewboo.core.utility.ISerializable;
import io.serateam.stewboo.core.utility.JSONService;
import io.serateam.stewboo.core.utility.SharedVariables;

import java.util.List;

public class FlashCardService implements IService, ISerializable
{
    private static FlashCardService instance;
    private static DeckList decks;

    private FlashCardService()
    {
        decks = DeckList.getInstance();
    }

    public static FlashCardService getInstance()
    {
        if (instance == null) {
            instance = new FlashCardService();
        }
        return instance;
    }

    @Override
    public void initializeService()
    {
        decks = JSONService.deserialize(SharedVariables.Path.flashcardJSON, DeckList.class);
        if (decks == null)
        {
            decks = DeckList.getInstance();
            Deck sampleDeck = new Deck("Sample Deck");
            sampleDeck.addflashCard(new Card("What is 2 + 2?", "4"));
            sampleDeck.addflashCard(new Card("What is the capital of France?", "Paris"));
            decks.addDeck(sampleDeck);
            saveDecksToFile();
        }
    }

    public void addflashCard(Deck deck, Card card)
    {
        deck.addflashCard(card);
        saveDecksToFile();
    }

    public List<Deck> getDecks()
    {
        return decks.getDecks();
    }

    public void addDeck(Deck deckName)
    {
        decks.addDeck(deckName);
        saveDecksToFile();
    }

    public void deleteDeck(Deck deck)
    {
        decks.deleteDeck(deck);
        saveDecksToFile();
    }

    private void saveDecksToFile()
    {
        JSONService.serializeAndWriteToFile(SharedVariables.Path.flashcardJSON, decks);
    }
}
