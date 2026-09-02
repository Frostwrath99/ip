package es.gui;
import es.Es;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/** Controller for the Es chat window. */
public class MainWindow extends AnchorPane {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox dialogContainer;
    @FXML private TextField userInput;
    private Es es;
    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image esImage = new Image(getClass().getResourceAsStream("/images/DaDuke.png"));
    @FXML public void initialize() { scrollPane.vvalueProperty().bind(dialogContainer.heightProperty()); }
    /** Injects the chatbot. */
    public void setEs(Es chatbot) { es = chatbot; }
    /** Handles a submitted command. */
    @FXML private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) return;
        dialogContainer.getChildren().addAll(DialogBox.user(input, userImage),
                DialogBox.reply(es.getResponse(input), esImage));
        userInput.clear();
        if (input.equalsIgnoreCase("bye")) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> Platform.exit());
            pause.play();
        }
    }
}
