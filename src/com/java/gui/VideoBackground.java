package com.java.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class VideoBackground {
    public static JFXPanel create(String filepath) {
        JFXPanel fxPanel = new JFXPanel();
        Platform.runLater(() -> {
            MediaPlayer player = new MediaPlayer(new Media(new File(filepath).toURI().toString()));
            player.setCycleCount(MediaPlayer.INDEFINITE);
            MediaView view = new MediaView(player);
            view.setPreserveRatio(true);
            StackPane root = new StackPane(view);
            Scene scene = new Scene(root);
            fxPanel.setScene(scene);
            player.play();
        });
        return fxPanel;
    }
}
