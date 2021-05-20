package labyrinth.controllers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import labyrinth.jaxb.GameResult;

import javax.swing.border.Border;
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
    BorderPane homeBP;

    GameResult result = new GameResult();

    @FXML
    public void initialize(){
        File file = new File(getClass().getClassLoader().getResource("assets/Labyrinth.mp4").getPath());
        if(file != null) {
            initMediaPlayer(file);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                homeBP.requestFocus();
            }
        });
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
        if(playerName.getText().isEmpty()){
            playerNameMissing.setText("Please Don't leave the name field blank!");
        }
        else if (!playerName.getText().isEmpty()){
            sendResultName(playerName.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();


            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
                stage.setUserData(result);
                Scene scene = new Scene(root);
                stage.show();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void sendResultName(String name){
        result.setName(playerName.getText());
    }


}
