package io.serateam.stewboo.core.services.flashcard;

import io.serateam.stewboo.core.utility.ISerializable;

import java.util.ArrayList;
import java.util.List;

public class Deck implements ISerializable
{
    private String name;
    private List<Card> flashCards;

    public Deck(String name)
    {
        this.name = name;
        this.flashCards = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public List<Card> getFlashCards()
    {
        return flashCards;
    }

    public int getflashCardsCount()
    {
        return flashCards.size();
    }

    public void addflashCard(Card card)
    {
        flashCards.add(card);
    }

    public void removeflashCard(Card card)
    {
        flashCards.remove(card);
    }
}
