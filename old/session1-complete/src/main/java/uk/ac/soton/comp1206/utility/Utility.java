package uk.ac.soton.comp1206.utility;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A utility class for quick and handy static functions
 *
 * We will be adding to this later, but you can add things that are handy here too!
 */
public class Utility {

    private static final Logger logger = LogManager.getLogger(Utility.class);
    private static boolean audioEnabled = true;

    public static void playAudio(String file) {
        String toPlay = Utility.class.getResource("/" + file).toExternalForm();
        logger.info("Playing audio: " + toPlay);

        try {
            Media play = new Media(toPlay);
            MediaPlayer mediaPlayer = new MediaPlayer(play);
            mediaPlayer.play();
        } catch (Exception e) {
            audioEnabled = false;
            e.printStackTrace();
            logger.error("Unable to play audio file, disabling audio");
        }
    }

}
