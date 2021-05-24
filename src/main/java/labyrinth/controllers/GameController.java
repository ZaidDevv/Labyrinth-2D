// CHECKSTYLE:OFF

package labyrinth.controllers;
import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import labyrinth.jaxb.GameResult;
import labyrinth.jaxb.ResultsHandler;
import labyrinth.model.Cell;
import labyrinth.model.GameBoardModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class GameController {
    @FXML
    private GridPane board;

    @FXML
    private BorderPane border;

    @FXML
    private JFXButton resetButton;

    @FXML
    Label timerui;

    @FXML
    Label finalscoreUI;

    private final Cell referenceCell = new Cell();
    private DoubleProperty timeTaken = new SimpleDoubleProperty();
    private long startTime;
    private GameBoardModel model = new GameBoardModel();
    private Circle piece;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
    private GameResult finalResult = new GameResult();
    private static final Logger logger = LogManager.getLogger("Main");

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            timeTaken.set((now - startTime) / 1000000000.0);
            if(!board.isFocused()){
                board.requestFocus();
            }
            if(model.hasFinished()){
                border.requestFocus();
                timer.stop();
            }
        }
    };

    @FXML
    public void initialize(){
        logger.debug("Game is Initializing");
        model = new GameBoardModel();
        initGrid();
        drawWalls();
        initGoalState();
        createGamePiece();
        startTime = System.nanoTime();
        initTimer(timer);
        board.requestFocus();
        logger.debug("Finished Initialization");
    }

    private void initTimer(AnimationTimer timer){
        timerui.textProperty().bind(timeTaken.asString("%.2f"));
        timer.start();
    }


    private void createGamePiece(){
        piece = new Circle((referenceCell.getWidth()/2) - 3);
        piece.setFill(Color.BLUE);
        piece.setCenterX(referenceCell.getWidth()/2);
        piece.setCenterY(referenceCell.getWidth()/2);
        piece.getStyleClass().add("game-piece");
        Pane pane = (Pane) getNodeByRowColumnIndex(0, 0);
        pane.getChildren().add(piece);
    }


    private void initGoalState(){
        Circle goal = new Circle((referenceCell.getWidth()/2) - 3);
        goal.setFill(Color.TRANSPARENT);
        goal.setStrokeWidth(2);
        goal.setStroke(Color.BLACK);
        goal.setCenterX(referenceCell.getWidth()/2);
        goal.setCenterY(referenceCell.getWidth()/2);
        Pane pane = (Pane) getNodeByRowColumnIndex(model.getGoalCell().getRow(), model.getGoalCell().getCol());
        pane.getChildren().add(goal);
    }




    private void initGrid() {
        board.getStyleClass().add("game-grid");
        for (int i = 0; i < model.BOARD_SIZE; ++i) {
            for (int j = 0; j < model.BOARD_SIZE;++j) {
                var cell = createCell();
                board.add(cell, j, i);
            }
        }
    }

    private Pane createCell(){
        var cell = new Pane();
        cell.getStyleClass().add("game-grid-cell");
        return cell;

    }

    private Node getNodeByRowColumnIndex (final int row, final int column) {
        Node result = null;
        ObservableList<Node> children = board.getChildren();
        for (Node node : children) {
            if(node == null){
                continue;
            }
            if(GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row){
                result = node;
                break;
            }
        }

        return result;
    }

    private void drawWalls(){
        Cell[][] boardState = model.getBoard();
        double width = referenceCell.getWidth();
        for(int i = 0; i < model.BOARD_SIZE;i++){
            for(int j = 0 ; j < model.BOARD_SIZE; j++){
                Boolean[] walls = boardState[i][j].getWalls();
                Pane pane = (Pane) getNodeByRowColumnIndex(i,j);
                    if (walls[0]) {
                        Line l = new Line(0,0,width,0);
                        l.setStrokeWidth(6);
                        l.setStartX(0);
                        l.setEndX(width);
                        l.setStartY(0);
                        l.setEndY(0);
                        pane.getChildren().add(l);

                    }

                    if (walls[1]) {
                        Line l = new Line(width,0,width,width);
                        l.setStrokeWidth(6);
                        pane.getChildren().add(l);
                    }
                    if (walls[2]) {
                        Line l = new Line(0,width,width,width);
                        l.setStrokeWidth(6);
                        pane.getChildren().add(l);
                    }

                    if (walls[3]) {
                        Line l = new Line(0,0,0,width);
                        l.setStrokeWidth(6);
                        pane.getChildren().add(l);

                }

            }
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        KeyCode key = keyEvent.getCode();
        Pane pane = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(),model.getGameCell().getCol());
        switch (key) {
            case A -> {
                if (model.canMoveLeft(model.getGameCell())) {
                    model.moveLeft();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(), model.getGameCell().getCol());
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
            }
            case D -> {
                if (model.canMoveRight(model.getGameCell())) {
                    model.moveRight();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(), model.getGameCell().getCol());
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
            }
            case W -> {
                if (model.canMoveUp(model.getGameCell())) {
                    model.moveUp();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(), model.getGameCell().getCol());
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
            }
            case S -> {
                if (model.canMoveDown(model.getGameCell())) {
                    model.moveDown();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(), model.getGameCell().getCol());
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
            }
        }
        logger.debug("Piece has moved to {},{}",model.getGameCell().getRow(),model.getGameCell().getCol());
        if(model.hasFinished()){
            logger.info("Congratulations you won!");
            finalResult.setOutcome(true);
            displayScore();
        }

    }

    public void resetGameUI(){
        ObservableList<Node> boardChildren = board.getChildren();
        for(Node paneChild : boardChildren) {
            Pane currentPane = (Pane) paneChild;
            ObservableList<Node> paneChildren = currentPane.getChildren();
            for(int i = 0 ; i < paneChildren.size(); i++){
                Node currentNode = paneChildren.get(i);
                if(currentNode instanceof Line || currentNode instanceof Circle){
                    paneChildren.remove(paneChildren.get(i));
                    i--;
                }
            }
        }
        finalscoreUI.setVisible(false);
    }
    @FXML
    public void handleResetButton(ActionEvent actionEvent){
        logger.debug("Resetting Game...");
        startTime = System.nanoTime();
        Pane pane = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(), model.getGameCell().getCol());
        pane.getStyleClass().remove("visited-cell");
        resetGameUI();
        initialize();
    }

    public void displayScore(){
        finalscoreUI.setText("SCORE : " + Math.round(model.getFinalScore()));
        logger.info("Final Score is {}",model.getFinalScore());
        finalscoreUI.setVisible(true);
    }

    @FXML
    public void handleExitButton(ActionEvent actionEvent){
        Platform.exit();
    }

    @FXML
    public void handleLeaderboardsButton(ActionEvent actionEvent) throws IOException {
        logger.info("Loading Leaderboards...");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        GameResult result = (GameResult) stage.getUserData();
        finalResult.setName(result.getName());
        createResult();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Leaderboards.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void createResult() {

        new ResultsHandler().commitResult(finalResult.getName(), model.getMovesCount(),model.getFinalScore(),finalResult.isOutcome(), ZonedDateTime.now().format(dateTimeFormatter));

    }

}
