package labyrinth.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class HomeController {
    @FXML
    MediaView mvLogo;
    @FXML
    JFXButton playButton;

    @FXML
    TextField playerName;

    @FXML
    Label playerNameMissing;


    @FXML
    public void initialize(){
        File file = new File("src/main/resources/Labyrinth.mp4");
        if(file != null) {
            initMediaPlayer(file);
        }
    }

    public void initMediaPlayer(File file){
        MediaPlayer mp = new MediaPlayer(new Media(file.toURI().toString()));
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mp.setAutoPlay(true);
                mvLogo.setMediaPlayer(mp);
                mp.cycleCountProperty().set(20);
            }
        },0);
    }

    @FXML
    public void handlePlayButton(ActionEvent actionEvent) throws IOException {
        System.out.println(playerName.getText());
        if(playerName.getText().isEmpty()){
            playerNameMissing.setText("Please Don't leave the name field blank!");
        }
        else if (!playerName.getText().isEmpty()){
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

}
