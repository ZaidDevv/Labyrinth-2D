package labyrinth.model;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class GameBoardModel {
    public final int BOARD_SIZE = 12;
    private Cell[][] board = new Cell[BOARD_SIZE][BOARD_SIZE];
    private Stack<Cell> cellStack;
    private Cell gameCell;
    private Cell goalCell;
    private int steps;
    private DoubleProperty finalScore;
    boolean outcome;


    public DoubleProperty getFinalScore(){
        return finalScore;
    }
    public int getSteps(){
        return steps;
    }

    public void setOutcome(boolean outcome) {
        this.outcome = outcome;
    }

    public boolean isOutcome() {
        return outcome;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public GameBoardModel(){
        setOutcome(false);
        steps = 0;
        finalScore = new SimpleDoubleProperty();
        cellStack = new Stack<>();
        initializeBoard();
        gameCell = board[0][0];
        generateMaze();
    }


    public void initializeBoard(){
        for(int i = 0 ; i < BOARD_SIZE;i++){
            for(int j = 0 ; j < BOARD_SIZE;j++){
                board[i][j] = new Cell(i,j);
            }
        }
        goalCell = board[BOARD_SIZE-1][BOARD_SIZE-1];
    }

    public Cell getGoalCell() {
        return goalCell;
    }

    public Cell getCell(int i, int j){
        return board[i][j];
    }

    public Cell getGameCell() {
        return gameCell;
    }

    public boolean canMoveUp(Cell cell){
        if(cell.getWalls()[0]){
            return false;
        }
        else if (cell.getRow() - 1 < 0){
            return false;
        }
        return true;
    }

    public boolean canMoveRight(Cell cell){
        if(cell.getWalls()[1]){
            return false;
        }
        else if (cell.getCol() + 1 > BOARD_SIZE - 1){
            return false;
        }
        return true;
    }
    public boolean canMoveDown(Cell cell){
        if(cell.getWalls()[2]){
            return false;
        }
        else if (cell.getRow() + 1 > BOARD_SIZE - 1){
            return false;
        }
        return true;
    }
    public boolean canMoveLeft(Cell cell){
        if(cell.getWalls()[3]){
            return false;
        }
        else return cell.getCol() - 1 >= 0;
    }

    public void moveUp(){
        gameCell = board[gameCell.getRow() - 1][gameCell.getCol()];
        steps++;
    }

    public void moveRight(){
        gameCell = board[gameCell.getRow()][gameCell.getCol() + 1];
        steps++;
    }
    public void moveDown(){
        gameCell = board[gameCell.getRow() + 1][gameCell.getCol()];
        steps++;
    }
    public void moveLeft(){
        gameCell = board[gameCell.getRow()][gameCell.getCol() - 1];
        steps++;
    }

    public Cell checkNeighbours(Cell cell){
        List<Cell> neighbours = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();
        if(checkIndex(row - 1,col)){
            if(!board[row-1][col].getVisited()){
                neighbours.add(board[row-1][col]);
            }
        }
        if(checkIndex(row,col + 1)){
            if(!board[row][col+1].getVisited()){
                neighbours.add(board[row][col+1]);
            }
        }
        if(checkIndex(row + 1,col)){
            if(!board[row + 1][col].getVisited()){
                neighbours.add(board[row + 1][col]);
            }
        }
        if(checkIndex(row,col - 1)){
            if(!board[row][col - 1].getVisited()){
                neighbours.add(board[row][col - 1]);
            }
        }

        Random r = new Random();
        if(!neighbours.isEmpty()) {
            return neighbours.get(r.nextInt(neighbours.size()));
        }

        return new Cell(true);
    }

    public Boolean checkIndex(int i,int j){
        if(i < 0 || j < 0 || i > BOARD_SIZE - 1 || j > BOARD_SIZE - 1){
            return false;
        }
        return true;
    }

    public void generateMaze(){
        Cell currentCell = board[0][0];
        currentCell.setVisited(true);
        cellStack.push(currentCell);
        while(!cellStack.empty()){
            currentCell = cellStack.pop();
            Cell randomCell = checkNeighbours(currentCell);
            if(!randomCell.isInvalidCell()){
                cellStack.push(currentCell);
                if(currentCell.getRow() == randomCell.getRow() + 1){
                    board[randomCell.getRow()][randomCell.getCol()].getWalls()[2] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[0] = false;
                }
                else if (currentCell.getRow() == randomCell.getRow() - 1){
                    board[randomCell.getRow()][randomCell.getCol()].getWalls()[0] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[2] = false;
                }

                else if(currentCell.getCol() == randomCell.getCol() + 1){
                    board[randomCell.getRow()][randomCell.getCol()].getWalls()[1] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[3] = false;
                }
                else if(currentCell.getCol() == randomCell.getCol() - 1){
                    board[randomCell.getRow()][randomCell.getCol()].getWalls()[3] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[1] = false;
                }
                randomCell.setVisited(true);
                currentCell = randomCell;
                cellStack.push(currentCell);
            }
        }
    }

    public boolean hasFinished(){
        if(gameCell.getRow() == goalCell.getRow() && gameCell.getCol() == goalCell.getCol()){
            setOutcome(true);
            return true;
        }
        return false;
    }

}
