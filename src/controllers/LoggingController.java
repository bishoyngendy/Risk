package controllers;

import core.GameBoardCoreImpl;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by programajor on 11/23/18.
 */
public class LoggingController implements Initializable {
    public VBox vbox;
    private MainController mainController;

    /**
     * Initializes the reference to the reference to the main controller pane.
     * @param mainController reference to the the parent.
     */
    final void init(final MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void addLoggingMessage(String message, int player) {
        Label label = new Label(message);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Font.font("Cambria", 14));
        if (player == 1) {
            label.setTextFill(Color.RED);
        } else if (player == 2) {
            label.setTextFill(Color.BLUE);
        } else {
            label.setTextFill(Color.BLACK);
        }
        vbox.getChildren().add(label);
    }

}
