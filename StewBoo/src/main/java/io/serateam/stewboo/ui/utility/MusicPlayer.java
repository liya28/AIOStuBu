package io.serateam.stewboo.ui.utility;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class MusicPlayer
{
    /**
     * Plays the audio according to the URL path provided. <p>
     * It is recommended that the URL path is obtained
     * from {@link io.serateam.stewboo.ui.SharedVariables}
     * @param path contains the Java URL path to the audio file.
     */
    public static void playMusic(URL path)
    {
        if(path == null)
        {
            System.err.println("Error: Audio path is null");
            return;
        }
        try
        {
            MediaPlayer mp = new MediaPlayer(new Media(path.toExternalForm()));
            mp.play();
        }
        catch(MediaException ex)
        {
            System.err.println("Error playing audio: " + ex.getMessage());
        }
    }
}
