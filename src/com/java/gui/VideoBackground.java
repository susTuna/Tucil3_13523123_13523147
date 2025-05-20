package com.java.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;

public class VideoBackground {
    private static MediaPlayer activePlayer;
    
    public static JFXPanel create(String filepath) {
        JFXPanel fxPanel = new JFXPanel();
        Platform.runLater(() -> {
            try {
                File file = new File(filepath);
                if (!file.exists()) {
                    System.err.println("Video file not found: " + file.getAbsolutePath());
                    return;
                }
                
                Media media = new Media(file.toURI().toString());
                activePlayer = new MediaPlayer(media);
                activePlayer.setCycleCount(MediaPlayer.INDEFINITE);
                activePlayer.setMute(true); // Mute the player immediately
                
                MediaView view = new MediaView(activePlayer);
                
                // Don't preserve ratio so it fills the panel
                view.setPreserveRatio(false);
                
                StackPane root = new StackPane(view);
                Scene scene = new Scene(root);
                
                // Make background transparent so it doesn't block other components
                root.setStyle("-fx-background-color: transparent;");
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
                
                // Bind the MediaView size to the panel size
                view.fitWidthProperty().bind(root.widthProperty());
                view.fitHeightProperty().bind(root.heightProperty());
                
                fxPanel.setScene(scene);
                activePlayer.play();
                
                // Double-check muting after player starts
                activePlayer.setOnPlaying(() -> {
                    activePlayer.setMute(true);
                });
            } catch (Exception e) {
                System.err.println("Error loading video: " + e.getMessage());
                e.printStackTrace();
            }
        });
        return fxPanel;
    }
}
