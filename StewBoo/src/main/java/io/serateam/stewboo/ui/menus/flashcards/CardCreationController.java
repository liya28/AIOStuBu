package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Card;
import io.serateam.stewboo.core.services.flashcard.Deck;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CardCreationController {
    private FlashCardService service = FlashCardService.getInstance();
    private FlashCardMenuController menuController;
    private Deck deck;

    @FXML private TextField questionTextField;
    @FXML private TextArea answerTextArea;
    @FXML private TextField deckNameField;
    @FXML private Label WarningLabel;

    @FXML
    public void initialize() {
        if (WarningLabel != null)
        {
            WarningLabel.setVisible(false);
        }

        if (questionTextField != null)
        {
            questionTextField.setOnAction(event -> addCard());
        }

        if (answerTextArea != null)
        {
            answerTextArea.setOnKeyPressed(event ->
            {
                if (event.getCode().getName().equals("Enter"))
                {
                    addCard();
                }
            });
        }
    }

    public void setDeck(Deck deck, FlashCardMenuController menuController)
    {
        this.deck = deck;
        this.menuController = menuController;

        if (deckNameField != null)
        {
            deckNameField.setText(deck.getName());
            deckNameField.setEditable(false);
        }
    }

    public String getDeckName()
    {
        return deckNameField.getText();
    }

    @FXML
    private void addCard()
    {
        String question = questionTextField.getText();
        String answer = answerTextArea.getText();

        if (!question.isEmpty() && !answer.isEmpty())
        {
            Card card = new Card(question, answer);
            if (deck != null)
            {
                service.addflashCard(deck, card);
                menuController.updateflashcardCount(); // Notify FlashCardMenuController
            }

            questionTextField.clear();
            answerTextArea.clear();
        }
        else
        {
            displayWarningMessage(question, answer);
        }
    }

    private void displayWarningMessage(String question, String answer)
    {
        if (WarningLabel != null)
        {
            WarningLabel.setVisible(true);

            if (question.isEmpty() && answer.isEmpty())
            {
                WarningLabel.setText("Question and Answer must be filled");
            }
            else if (question.isEmpty())
            {
                WarningLabel.setText("Please enter a question.");
            }
            else if (answer.isEmpty())
            {
                WarningLabel.setText("Please enter an answer to your question.");
            }
        }
    }

    @FXML
    private void closeWindow(ActionEvent event)
    {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
