package uk.ac.soton.comp1206;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.ui.ChatWindow;
import uk.ac.soton.comp1206.ui.LoginWindow;
import uk.ac.soton.comp1206.utility.Utility;

import java.net.URL;

/**
 * Our Chat application main class. This will be responsible for co-ordinating the application and handling the GUI.
 */
public class App extends Application {

    private static final Logger logger = LogManager.getLogger(App.class);
    private Communicator communicator;
    private Stage stage;
    private String username;

    /**
     * Launch the JavaFX application
     * @param args
     */
    public static void main(String[] args) {
        logger.info("Starting client");
        launch();
    }

    /**
     * Start the Java FX process - prepare and display the first window
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        this.stage = stage;
        communicator = new Communicator("ws://discord.ecs.soton.ac.uk:9500");
        //communicator = new Communicator("ws://discord.ecs.soton.ac.uk:9501"); use this for private chat

        stage.setTitle("ECS Instant Messenger (EIM)");
        stage.setOnCloseRequest(ev -> {
            shutdown();
        });

        //Open the login window
        openLogin();

        //For now, lets just hardcode our username (you can change it here to something better!
        setUsername("Guest");

        //Open the chat window - later, we'll do this only after the login has been done
        openChat();
    }

    /**
     * Display the login window
     */
    public void openLogin() {
        logger.info("Opening login window");
        var window = new LoginWindow(this);
        stage.setScene(window.getScene());

        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Display the chat window
     */
    public void openChat() {
        logger.info("Opening chat window");

        var window = new ChatWindow(this,communicator);

        stage.setScene(window.getScene());

        stage.show();
        stage.centerOnScreen();

        //Play a sound when the chat window appears
        Utility.playAudio("connected.mp3");
    }

    /**
     * Shutdown the application
     */
    public void shutdown() {
        logger.info("Shutting down");
        System.exit(0);
    }

    /**
     * Set the username from the login window
     * @param username
     */
    public void setUsername(String username) {
        logger.info("Username set to: " + username);
        this.username = username;
    }

    /**
     * Get the currently logged in user name
     * @return
     */
    public String getUsername() {
        return this.username;
    }
}
