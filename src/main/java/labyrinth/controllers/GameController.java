package labyrinth.controllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import labyrinth.model.Cell;
import labyrinth.model.GameBoardModel;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


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

    final Cell referenceCell = new Cell();
    DoubleProperty timeTaken = new SimpleDoubleProperty();
    private long startTime = System.nanoTime();
    GameBoardModel model;
    private Circle piece;


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
        model = new GameBoardModel();
        initGrid();
        drawWalls();
        initGoalState();
        createGamePiece();
        initTimer(timer);
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
        Pane pane = (Pane) getNodeByRowColumnIndex(0, 0,board);
        pane.getChildren().add(piece);
    }


    private void initGoalState(){
        Circle goal = new Circle((referenceCell.getWidth()/2) - 3);
        goal.setFill(Color.TRANSPARENT);
        goal.setStrokeWidth(2);
        goal.setStroke(Color.BLACK);
        goal.setCenterX(referenceCell.getWidth()/2);
        goal.setCenterY(referenceCell.getWidth()/2);
        Pane pane = (Pane) getNodeByRowColumnIndex(model.getGoalCell().getRow(), model.getGoalCell().getCol(),board);
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

    private Node getNodeByRowColumnIndex (final int row, final int column, GridPane grid) {
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
                Pane pane = (Pane) getNodeByRowColumnIndex(i,j,board);
                    if (walls[0] == true) {
                        Line l = new Line();
                        l.setStrokeWidth(6);
                        l.setStartX(0);
                        l.setEndX(width);
                        l.setStartY(0);
                        l.setEndY(0);
                        pane.getChildren().add(l);

                    }

                    if (walls[1] == true) {
                        Line l = new Line();
                        l.setStrokeWidth(6);
                        l.setStartX(width);
                        l.setEndX(width);
                        l.setStartY(0);
                        l.setEndY(width);
                        pane.getChildren().add(l);
                    }
                    if (walls[2] == true) {
                        Line l = new Line();
                        l.setStrokeWidth(6);
                        l.setStartX(0);
                        l.setEndX(width);
                        l.setStartY(width);
                        l.setEndY(width);
                        pane.getChildren().add(l);
                    }

                    if (walls[3] == true) {
                        Line l = new Line();
                        l.setStrokeWidth(6);
                        l.setStartX(0);
                        l.setEndX(0);
                        l.setStartY(0);
                        l.setEndY(width);
                        pane.getChildren().add(l);

                }

            }
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        KeyCode key = keyEvent.getCode();
        Pane pane = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(),model.getGameCell().getCol(),board);
        switch(key){
            case A: {
                if(model.canMoveLeft(model.getGameCell())){
                    model.moveLeft();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(),model.getGameCell().getCol(),board);
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
            }
            break;
            case D: {
                if(model.canMoveRight(model.getGameCell())){
                    model.moveRight();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(),model.getGameCell().getCol(),board);
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
                break;
            }
            case W: {
                if(model.canMoveUp(model.getGameCell())){
                    model.moveUp();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(),model.getGameCell().getCol(),board);
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
                break;
            }
            case S: {
                if(model.canMoveDown(model.getGameCell())){
                    model.moveDown();
                    Pane paneNext = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(),model.getGameCell().getCol(),board);
                    paneNext.getChildren().add(piece);
                    pane.getStyleClass().remove("visited-cell");
                    paneNext.getStyleClass().add("visited-cell");
                }
                break;
            }
        }



        if(model.hasFinished()){
            System.out.println("Congratulations You Win!");
            model.getFinalScore().set((1/timeTaken.get() + 1/model.getSteps()) * 4000);
            displayScore();
        }

    }

    public void resetGame(){
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
    }
    @FXML
    public void handleOnButtonAction(ActionEvent actionEvent) {
        startTime = System.nanoTime();
        Pane pane = (Pane) getNodeByRowColumnIndex(model.getGameCell().getRow(), model.getGameCell().getCol(), board);
        pane.getStyleClass().remove("visited-cell");
        resetGame();
        initialize();
    }

    public void displayScore(){
        finalscoreUI.setText(String.valueOf(String.valueOf(Math.round(model.getFinalScore().getValue()))));
    }
    @FXML
    public void handleExitButton(ActionEvent actionEvent){
        Platform.exit();
    }

}
