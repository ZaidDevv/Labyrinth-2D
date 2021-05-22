// CHECKSTYLE:OFF

package labyrinth.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import labyrinth.jaxb.GameResult;
import labyrinth.jaxb.ResultsHandler;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


public class LeaderboardController {
    @FXML
    private TableView tvLeaderboard;

    @FXML
    private TableColumn<GameResult, String> name;

    @FXML
    private TableColumn<GameResult, String> startDate;

    @FXML
    private TableColumn<GameResult, Integer> steps;

    @FXML
    private TableColumn<GameResult, Double> score;

    @FXML
    private TableColumn<GameResult, Boolean> outcome;


    @FXML
    public void initialize() throws IOException {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        outcome.setCellValueFactory(new PropertyValueFactory<>("outcome"));
        score.setCellValueFactory(columnData -> {
            double beforeFormattingScore = columnData.getValue().getFinalScore();
            columnData.getValue().setFinalScore(withBigDecimal(beforeFormattingScore,3));
             return new ReadOnlyObjectWrapper<Double>(columnData.getValue().getFinalScore());
        });
        List<GameResult> gameResultsList = new ResultsHandler().getGameResultsList();
        ObservableList<GameResult> observableList = FXCollections.observableArrayList(sortedGameResultsTop10(gameResultsList));
        tvLeaderboard.setItems(observableList);

    }

    public List<GameResult> sortedGameResultsTop10(List<GameResult> unsortedList){
        return unsortedList.stream()
                .sorted((a,b) -> Double.compare(b.getFinalScore(), a.getFinalScore()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private double withBigDecimal(double value, int places) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public void handleHomeBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

}