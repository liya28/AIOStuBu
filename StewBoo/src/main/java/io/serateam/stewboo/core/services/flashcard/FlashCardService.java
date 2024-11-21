package io.serateam.stewboo.core.services.flashcard;

import io.serateam.stewboo.core.services.IService;

import java.util.ArrayList;
import java.util.List;

public class FlashCardService implements IService
{
    private static final String DATA_FILE = "";
    private static FlashCardService instance;
    private final List<Deck> decks;

    public FlashCardService()
    {
        this.decks = new ArrayList<>();
    }

    public static FlashCardService getInstance()
    {
        if(instance == null)
        {
            instance = new FlashCardService();
        }
        return instance;
    }

    @Override
    public void initializeService()
    {
        loadDataFromFile();
    }

    //Data Persistence

    private void loadDataFromFile()
    {

    }

    private void saveDataToFile()
    {

    }

    //Deck Management
    public List<Deck> getDecks()
    {
        return  decks;
    }

    public void addDeck(String name)
    {
        Deck newDeck = new Deck(name);
        decks.add(newDeck);
        saveDataToFile();
    }

    public void deleteDeck(Deck deck)
    {
        decks.remove(deck);
        saveDataToFile();
    }

    public Deck getDeckByName(String name)
    {
        return decks.stream().filter(deck -> deck.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    //FLashCard Management
    public void addFlashCardToDeck(Deck deck, String question, String answer)
    {
        Card card = new Card(question, answer);
        deck.addflashCard(card);
        saveDataToFile();
    }

    public void clearFlashCardsFromDeck(Deck deck)
    {
        deck.clearflashcards();
        saveDataToFile();
    }
}
