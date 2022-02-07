package uk.ac.soton.comp1206.ui;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private final TextFlow messages;
    private final App app;
    private final Scene scene;
    Communicator communicator = null;

    /**
     * Create a new Chat Window, linked to the main App and the Communicator
     * @param app the main app
     * @param communicator the communicator
     */
    public ChatWindow(App app, Communicator communicator) {
        this.app = app;
        this.communicator = communicator;

        var pane = new BorderPane();
        this.scene = new Scene(pane,640,480);

        communicator.setWindow(this);

        //Create a scroll pane to allow scrolling messages
        var scroller = new ScrollPane();

        //Fit the scroll pane to the width so we only scroll vertically
        scroller.setFitToWidth(true);

        //Create a TextFlow to hold the messages - we'll call this messages
        messages = new TextFlow();
        messages.getStyleClass().add("messages");

        //Set the scroll pane to hold the messages, as that's what we want to scroll
        scroller.setContent(messages);

        //Set the Center of the BorderPane to hold the messages
        pane.setCenter(scroller);

        //When new messages arrive, scroll to the bottom
        messages.getChildren().addListener((ListChangeListener<Node>) ((change) -> {
            messages.layout();
            scroller.layout();
            scroller.setVvalue(1.0f);
        }));

        //Add a Horizontal HBbox to hold the input and send buttons, on the Bottom
        var horizontalPane = new HBox();
        pane.setBottom(horizontalPane);

        //Add our input text field
        var text = new TextField();
        text.setPromptText("Enter message");
        HBox.setHgrow(text, Priority.ALWAYS);

        //If we press enter, send the message
        text.setOnKeyPressed((e) -> {
            if (e.getCode() != KeyCode.ENTER) return;
            sendCurrentMessage(text.getText());
            text.clear();
            text.requestFocus();
        });

        //Add a button to send as well
        var button = new Button("Send");
        HBox.setHgrow(button, Priority.NEVER);
        button.setOnAction((e) -> {
            sendCurrentMessage(text.getText());
            text.clear();
            text.requestFocus();
        });

        //Add the text and button the the horizontal box
        horizontalPane.getChildren().add(text);
        horizontalPane.getChildren().add(button);

        //Set the stylesheet
        String css = this.getClass().getResource("/chat.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    /**
     * Handle an incoming message from the Communicator
     * @param message The message that has been received, in the form User:Message
     */
    public void receiveMessage(String message) {

        //Split incoming message into <nick>:<message>
        var components = message.split(":",2);
        if(components.length < 2) return;

        //Get the username and message separately
        var username = components[0];
        var text = components[1];

        //Format the current time
        var currentTime = formatter.format(LocalDateTime.now());

        //Add the time, then the nick, then the message
        var time = new Text("[" + currentTime + "] ");
        var nick = new Text(username + ": ");
        var msg = new Text(text);
        var newline = new Text ("\n");

        //Apply the timestamp style to the time
        time.getStyleClass().add("timestamp");

        //Format the nickname based on who sent it
        if(!username.equals(app.getUsername())) {
            nick.getStyleClass().add("nickname");
            Utility.playAudio("incoming.mp3");
        } else {
            nick.getStyleClass().add("my_nickname");
        }

        //Add the whole set to the messages
        messages.getChildren().addAll(time,nick,msg,newline);
    }

    /**
     * Send an outgoing message from the ChatWindow
     * @param text The text of the message to send to the Communicator
     */
    private void sendCurrentMessage(String text) {
        //Ignore the empty message
        if(text.isEmpty()) return;

        //Send to the communicator
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
