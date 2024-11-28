package io.serateam.stewboo.core.services.flashcard;

import io.serateam.stewboo.core.utility.ISerializable;

public class Card implements ISerializable
{
    private String question;
    private String answer;

    public Card(String question, String answer)
    {
        this.question = question;
        this.answer = answer;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }

    public String getQuestion()
    {
        return question;
    }

    public String getAnswer()
    {
        return answer;
    }
}
