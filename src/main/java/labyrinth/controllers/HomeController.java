// CHECKSTYLE:OFF

package labyrinth.controllers;
import com.jfoenix.controls.JFXButton;
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
import javafx.stage.Stage;
import labyrinth.jaxb.GameResult;
import java.io.IOException;

public class HomeController {
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                homeBP.requestFocus();
            }
        });
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
