package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Card;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import io.serateam.stewboo.ui.SharedVariables;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

public class CardViewController
{
    FlashCardService service = FlashCardService.getInstance();

    @FXML private ProgressBar progressBar;
    @FXML private Label label;
    @FXML private Label current_index;
    @FXML private AnchorPane card;
    @FXML private TextField indexTextField;

    private List<Card> flashCards;
    private int currentIndex;
    private boolean showed_question = false;
    private boolean isEditing = false;

    @FXML
    void initialize()
    {
        card.setOnMouseClicked(event ->
        {
            if(event.getClickCount() == 1) {
                flipCard();
            }
        });
    }

    public void setflashcards(List<Card> flashCards)
    {
        this.flashCards = flashCards;
        this.currentIndex = 0;

        if (flashCards == null || flashCards.isEmpty())
        {
            label.setText("No flashcards available.");
        }
        else
        {
            loadFlashcard(currentIndex);
        }
    }

    @FXML
    private void editFlashcard()
    {
        try{
            FXMLLoader loader = new FXMLLoader(SharedVariables.url_path_flashcardsEditFxml);
            Parent root = loader.load();

            CardCreationController con = loader.getController();
            con.setFlashcard(flashCards.get(currentIndex));

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Flashcard");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            service.saveDecksToFile();
            loadFlashcard(currentIndex);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadFlashcard(int index)
    {
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
}
