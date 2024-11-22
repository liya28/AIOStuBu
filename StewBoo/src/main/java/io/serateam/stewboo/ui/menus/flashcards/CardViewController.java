package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Card;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

import java.util.Collections;
import java.util.List;

public class CardViewController {
    @FXML private Label QuestionLabel;
    @FXML private Label AnswerLabel;
    @FXML private Label counterLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button nextButton;
    @FXML private Button previousButton;
    @FXML private Button shuffleButton;
    @FXML private Button showAnswerButton;

    private List<Card> flashCards;
    private int currentIndex;
    private String answer;

    public void setflashcards(List<Card> flashCards)
    {
        this.flashCards = flashCards;
        this.currentIndex = 0;

        if (flashCards == null || flashCards.isEmpty())
        {
            QuestionLabel.setText("No flashcards available.");
            AnswerLabel.setVisible(false);
        }
        else
        {
            loadFlashcard(currentIndex);
        }
    }

    private void loadFlashcard(int index) {
        if (flashCards != null && index < flashCards.size())
        {
            Card flashCard = flashCards.get(index);
            QuestionLabel.setText(flashCard.getQuestion());
            QuestionLabel.setMaxWidth(400);
            QuestionLabel.setAlignment(Pos.TOP_CENTER);
            this.answer = flashCard.getAnswer();
            AnswerLabel.setVisible(false);

            updateProgressBar();
            updateCounterLabel();
        }
    }

    private void updateProgressBar()
    {
        double progress = (double) currentIndex / flashCards.size();
        progressBar.setProgress(progress);
    }

    private void updateCounterLabel()
    {
        counterLabel.setText("Card " + (currentIndex + 1) + " of " + flashCards.size());
    }

    @FXML
    private void showAnswer()
    {
        AnswerLabel.setText(answer);
        AnswerLabel.setVisible(true);
    }

    @FXML
    private void nextCard()
    {
        currentIndex = (currentIndex + 1) % flashCards.size();
        loadFlashcard(currentIndex);
    }

    @FXML
    private void previousCard()
    {
        currentIndex = (currentIndex - 1 + flashCards.size()) % flashCards.size();
        loadFlashcard(currentIndex);
    }

    @FXML
    private void shuffleDeck()
    {
        Collections.shuffle(flashCards);
        currentIndex = 0;
        loadFlashcard(currentIndex);
    }

    @FXML
    private void handleRestartButton() {
        currentIndex = 0;
        loadFlashcard(currentIndex);
    }
}
