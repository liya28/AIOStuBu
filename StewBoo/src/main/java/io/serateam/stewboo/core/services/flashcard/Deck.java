package io.serateam.stewboo.core.services.flashcard;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private String name;
    private List<Card> flashCards;

    public Deck(String name)
    {
        this.name = name;
        this.flashCards = new ArrayList<>();
    }

    public void addflashCard(Card flashCard)
    {
        flashCards.add(flashCard);
    }

    public int getflashCardsCount()
    {
        return flashCards.size();
    }

    public List<Card> getflashCards()
    {
        return flashCards;
    }

    public String getName()
    {
        return name;
    }

    public void clearflashcards()
    {
        flashCards.clear();
    }
}
