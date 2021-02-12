package uk.ac.soton.comp1206.ui;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.utility.Utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Chat window which will display chat messages and a way to send new messages
 */
public class ChatWindow {

    private static final Logger logger = LogManager.getLogger(ChatWindow.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private final App app;
    private final Scene scene;
    private final Communicator communicator;
    private final TextFlow messages;
    private final TextField textField;
    private final ScrollPane scroller;

    /**
     * Create a new Chat Window, linked to the main App and the Communicator
     * @param app the main app
     * @param communicator the communicator
     */
    public ChatWindow(App app, Communicator communicator) {
        this.app = app;
        this.communicator = communicator;

        //Setup scene with a border pane
        var pane = new BorderPane();
        this.scene = new Scene(pane,640,480);

        //Link the communicator to this window
        communicator.setWindow(this);

        scroller = new ScrollPane();
        messages = new TextFlow();

        pane.setCenter(scroller);
        scroller.setContent(messages);
        scroller.setFitToWidth(true);

        var horizontalPane = new HBox();
        textField = new TextField();
        var button = new Button("Send");

        pane.setBottom(horizontalPane);

        horizontalPane.getChildren().add(textField);
        horizontalPane.getChildren().add(button);

        HBox.setHgrow(textField,Priority.ALWAYS);
        HBox.setHgrow(button,Priority.NEVER);

        receiveMessage("Ghost:This is a pretend message");

        button.setOnAction((e) -> {
            sendCurrentMessage(textField.getText());
        });

        EventHandler<ActionEvent> clickyButton = (event) -> {
            System.out.println("The button was pressed");
        };
        button.setOnAction(clickyButton);

        button.setOnAction((event) -> System.out.println("The button was pressed"));

        EventHandler<ActionEvent> handler = (event) -> {
            System.out.println("The button was pressed!");
        };

        textField.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ENTER) return;
            sendCurrentMessage(textField.getText());
        });


        //Set the stylesheet for this window
        String css = this.getClass().getResource("/chat.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    /**
     * Handle an incoming message from the Communicator
     * @param message The message that has been received, in the form User:Message
     */
    public void receiveMessage(String message) {
        var text = new Text(message + "\n");
        messages.getChildren().add(text);
        scroller.setVvalue(1);
    }

    /**
     * Send an outgoing message from the ChatWindow
     * @param text The text of the message to send to the Communicator
     */
    private void sendCurrentMessage(String text) {
        communicator.send(app.getUsername() + ":" + text);
    }

    /**
     * Get the scene contained inside the Chat Window
     * @return
     */
    public Scene getScene() {
        return scene;
    }
}
