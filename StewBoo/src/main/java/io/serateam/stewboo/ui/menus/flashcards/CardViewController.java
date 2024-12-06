package io.serateam.stewboo.ui.menus.flashcards;

import io.serateam.stewboo.core.services.flashcard.Card;
import io.serateam.stewboo.core.services.flashcard.FlashCardService;
import io.serateam.stewboo.ui.SharedVariables;
import io.serateam.stewboo.ui.utility.ControllerAlerter;
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
        current_index.setText("0 / 0");
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
        if(flashCards.isEmpty()){
            return;
        }
        isEditing = true;
        System.out.println("Flashcard: Editing card content");
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
            Card c = flashCards.get(currentIndex);
            label.setText(c.getQuestion());
            showed_question = false;
        }catch (Exception e){
            e.printStackTrace();
        }
        isEditing = false;
    }

    @FXML
    private void jumpToCard()
    {
        if(flashCards.isEmpty())
        {
            return;
        }
        String dex = indexTextField.getText();

        if(isNumeric(dex)) {
            int index = Integer.parseInt(indexTextField.getText()) - 1;
            if (index < flashCards.size() && index >= 0) {
                currentIndex = index;
            int index = Integer.parseInt(indexTextField.getText());
            if (index <= flashCards.size() && index > 0) {
                loadFlashcard(currentIndex);
                indexTextField.setText("");
            } else {
                ControllerAlerter.showError("Index Out of Bounds", "Cannot jump to " + index, "Please put a valid number within 1 and " + flashCards.size());
                indexTextField.setText("");
            }
        }
        else if(!isNumeric(dex))
        {
            ControllerAlerter.showError("Input is not a number", "Cannot jump to non valid number", "Please put a valid number within 1 and " + flashCards.size());
            indexTextField.setText("");
        }
    }

    boolean isNumeric(String str)
    {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    @FXML
    private void deleteCard()
    {
        if (flashCards == null || flashCards.isEmpty())
        {
            return;
        }

        int indexToRemove = currentIndex;
        flashCards.remove(indexToRemove);

        if (flashCards.isEmpty())
        {
            FlashCardMenuController menuController = SharedVariables.flashCardMenuController;
            if (menuController != null)
            {
                menuController.updateflashcardCount();
            }

            service.saveDecksToFile();

            Stage currentStage = (Stage) card.getScene().getWindow();
            currentStage.close();

            return;
        }

        if (indexToRemove >= flashCards.size())
        {
            currentIndex = 0;
        }
        loadFlashcard(currentIndex);

        service.saveDecksToFile();

        FlashCardMenuController menuController = SharedVariables.flashCardMenuController;
        if (menuController != null)
        {
            menuController.updateflashcardCount();
        }
    }


    private void loadFlashcard(int index)
    {
        if (flashCards != null && index < flashCards.size())
        {
            Card flashCard = flashCards.get(index);
            label.setText(flashCard.getQuestion());
            showed_question = true;
            isEditing = false;
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
        current_index.setText(String.valueOf(currentIndex + 1) + " / " + String.valueOf(flashCards.size()));
    }

    @FXML
    private void flipCard()
    {
        if (flashCards == null || flashCards.isEmpty())
        {
            return;
        }

        if(isEditing && !showed_question)
        {
            return;
        }
        System.out.println("Flashcard: Card flipped");

        RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.1), card);
        rotateOut.setAxis(Rotate.X_AXIS);
        rotateOut.setFromAngle(0);
        rotateOut.setToAngle(-90);
        rotateOut.setInterpolator(Interpolator.EASE_IN);

        rotateOut.setOnFinished(event ->
        {
            Card currentCard = flashCards.get(currentIndex);
            label.setText(showed_question ? currentCard.getAnswer() : currentCard.getQuestion());
            showed_question = !showed_question;

            RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.1), card);
            rotateIn.setAxis(Rotate.X_AXIS);
            rotateIn.setFromAngle(90);
            rotateIn.setToAngle(0);
            rotateIn.setInterpolator(Interpolator.EASE_OUT);
            rotateIn.play();
        });

        rotateOut.play();
    }

    @FXML
    private void nextCard()
    {
        if(flashCards.isEmpty())
        {
            return;
        }
        currentIndex = (currentIndex + 1) % flashCards.size();
        loadFlashcard(currentIndex);
    }

    @FXML
    private void previousCard()
    {
        if(flashCards.isEmpty())
        {
            return;
        }
        currentIndex = (currentIndex - 1 + flashCards.size()) % flashCards.size();
        loadFlashcard(currentIndex);
    }

    @FXML
    private void shuffleDeck()
    {
        if(flashCards.isEmpty())
        {
            return;
        }
        Collections.shuffle(flashCards);
        currentIndex = 0;
        System.out.println(flashCards.get(currentIndex).getQuestion());
        loadFlashcard(currentIndex);
    }
}
