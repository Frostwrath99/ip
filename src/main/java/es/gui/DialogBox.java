package es.gui;
import java.io.IOException;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
/** Displays a chat message and speaker image. */
public class DialogBox extends HBox {
    @FXML private Label dialog;
    @FXML private ImageView displayPicture;
    private DialogBox(String text, Image image) {
        try { FXMLLoader l = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml")); l.setController(this); l.setRoot(this); l.load(); }
        catch (IOException e) { throw new IllegalStateException("Unable to load dialog", e); }
        dialog.setText(text); displayPicture.setImage(image);
    }
    private void flip() { ObservableList<Node> c = FXCollections.observableArrayList(getChildren()); Collections.reverse(c); getChildren().setAll(c); setAlignment(Pos.TOP_LEFT); dialog.getStyleClass().add("reply-label"); }
    /** Creates a user message. */
    public static DialogBox user(String text, Image image) { return new DialogBox(text, image); }
    /** Creates an Es reply. */
    public static DialogBox reply(String text, Image image) { DialogBox b = new DialogBox(text, image); b.flip(); return b; }
}
