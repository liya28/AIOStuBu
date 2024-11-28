package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Card;
import io.serateam.stewboo.core.services.flashcard.Deck;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import io.serateam.stewboo.ui.utility.ControllerAlerter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CardCreationController
{
    private FlashCardService service = FlashCardService.getInstance();
    private FlashCardMenuController menuController;
    private Deck deck;

    @FXML private Button btn_doneDeck;
    @FXML private TextField questionTextField;
    @FXML private TextField answerTextField;
    @FXML private TextField deckNameField;
    @FXML private Label WarningLabel;

    @FXML
    public void initialize()
    {
        if (WarningLabel != null)
        {
            WarningLabel.setVisible(false);
        }

        if (questionTextField != null)
        {
            questionTextField.setOnAction(event -> addCard());
        }

        if (answerTextField != null)
        {
            answerTextField.setOnKeyPressed(event ->
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

    private boolean CharacterLengthChecker(String string)
    {
        int count = 0;
        for (int i = 0; i < string.length(); i++)
        {
            if(string.charAt(i) != ' ')
            {
                count++;
            }
        }
        if(count >= 30)
        {
            return false;
        }
        return true;
    }

    @FXML
    private void addCard()
    {
        String question = questionTextField.getText();
        String answer = answerTextField.getText();

        if (!question.isEmpty() && !answer.isEmpty() && CharacterLengthChecker(question) && CharacterLengthChecker(answer))
        {
            Card card = new Card(question, answer);
            if (deck != null)
            {
                service.addflashCard(deck, card);
                menuController.updateflashcardCount(); // Notify FlashCardMenuController
            }

            questionTextField.clear();
            answerTextField.clear();
        }
        else if(!question.isEmpty() && !answer.isEmpty() && !CharacterLengthChecker(question) && !CharacterLengthChecker(answer))
        {
            ControllerAlerter.showError("Error", "Question and Answer cannot exceed 30 characters", "Please enter a question and answer within 30 characters");
        }
        else if(!question.isEmpty() && !answer.isEmpty() && !CharacterLengthChecker(question) && CharacterLengthChecker(answer))
        {
            ControllerAlerter.showError("Error", "Answer cannot exceed 30 characters", "Please enter an answer within 30 characters.");
        }
        else if(!question.isEmpty() && !answer.isEmpty() && CharacterLengthChecker(question) && !CharacterLengthChecker(answer))
        {
            ControllerAlerter.showError("Error", "Question cannot exceed 30 characters", "Please enter a question within 30 characters");
        }
        else if(question.isEmpty() && answer.isEmpty())
        {
            ControllerAlerter.showError("Error", "Question and Answer cannot be empty", "Please enter a question and an answer.");
        }
        else if(question.isEmpty())
        {
            ControllerAlerter.showError("Error", "Question cannot be empty", "Please enter a question.");
        }
        else if(answer.isEmpty())
        {
            ControllerAlerter.showError("Error", "Answer cannot be empty", "Please enter an answer to your question.");
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
