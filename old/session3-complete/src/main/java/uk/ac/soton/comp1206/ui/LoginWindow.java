package uk.ac.soton.comp1206.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.App;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Displays the Login Window, to collect the username and then start the chat
 */
public class LoginWindow implements Initializable {

    private static final Logger logger = LogManager.getLogger(LoginWindow.class);
    private final App app;

    Scene scene = null;
    Parent root = null;

    @FXML
    private TextField username;

    /**
     * Create a new Login Window, linked to the main app. This should get the username of the user.
     * @param app the main app
     */
    public LoginWindow(App app) {
        this.app = app;

        try {
            //Load the FXML from login.fxml
            var loader = new FXMLLoader(getClass().getResource("/login.fxml"));

            //Make this class the controller
            loader.setController(this);

            //Load the FXML
            root = loader.load();
        } catch (Exception e) {
            logger.error("Unable to read file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        //Use the FXML as the root of our Scene Graph
        scene = new Scene(root);
    }

    /**
     * Get the scene contained inside the Login Window
     * @return login window scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Handle what happens when the user presses the login button
     * @param event button clicked
     */
    @FXML protected void handleLogin(ActionEvent event) {
        String user = username.getText();
        if(user.isBlank()) return;
        app.setUsername(user);
        app.openChat();
    }

    /**
     * Handle what happens when the user presses enter on the username field
     * @param event key pressed
     */
    @FXML protected void handleEnter(KeyEvent event) {
        if(event.getCode() != KeyCode.ENTER) return;
        handleLogin(null);
    }

    /**
     * Initialise the Login Window - called by JavaFX when the window is ready
     * @param url
     * @param bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        username.requestFocus();
    }
}
