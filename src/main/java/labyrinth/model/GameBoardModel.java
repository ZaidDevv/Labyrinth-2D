package labyrinth.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Model class that handles the implementation and logic of the game
 */
public class GameBoardModel{
    public final int BOARD_SIZE = 12;
    private final Cell[][] board = new Cell[BOARD_SIZE][BOARD_SIZE];
    private final Stack<Cell> cellStack;
    private Cell gameCell;
    private Cell goalCell;
    private int movesCount;
    private double finalScore;

    /**
     * Parameterless Constructor
     */

    public GameBoardModel(){
        cellStack = new Stack<>();
        initializeBoard(board);
        gameCell = board[0][0];
        generateMaze();
    }

    /**
     * @return Current count of valid moves
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * @return 2D Array of {@code Cell} that represents the game's state
     */
    public Cell[][] getBoard() {
        return board;
    }

    /**
     *  Initializes the {@code board} with {@code Cell} Objects according to {@code BOARD_SIZE}
     *
     * @param board 2D Array of Cells that represents the game's state
     */
    public void initializeBoard(Cell[][] board){
        for(int i = 0 ; i < BOARD_SIZE;i++){
            for(int j = 0 ; j < BOARD_SIZE;j++){
                board[i][j] = new Cell(i,j);
            }
        }
        goalCell = board[BOARD_SIZE-1][BOARD_SIZE-1];
    }

    /**
     * @return Goal game state
     */

    public Cell getGoalCell() {
        return goalCell;
    }

    /**
     * @return Stores current game state
     */

    public Cell getGameCell() {
        return gameCell;
    }

    /**
     * @return game's final score
     */
    public double getFinalScore(){
        return finalScore;
    }

    /**
     * determines whether moving upwards is valid.
     * @param cell
     * @return {@code true} if the move is legal {@code false} otherwise
     */
    public boolean canMoveUp(Cell cell){
        if(cell.getWalls()[0]){
            return false;
        }
        else if (cell.getRow() - 1 < 0){
            return false;
        }
        return true;
    }
    /**
     * determines whether moving right is valid.
     * @param cell
     * @return {@code true} if the move is legal {@code false} otherwise
     */
    public boolean canMoveRight(Cell cell){
        if(cell.getWalls()[1]){
            return false;
        }
        else if (cell.getCol() + 1 > BOARD_SIZE - 1){
            return false;
        }
        return true;
    }
    /**
    * determines whether moving downwards is valid.
     * @param cell
     * @return {@code true} if the move is legal {@code false} otherwise
     */
    public boolean canMoveDown(Cell cell){
        if(cell.getWalls()[2]){
            return false;
        }
        else if (cell.getRow() + 1 > BOARD_SIZE - 1){
            return false;
        }
        return true;
    }
    /**
     * determines whether moving left is valid.
     * @param cell The {@code Cell} containing the {@code piece}
     * @return {@code true} if the move is legal {@code false} otherwise
     */
    public boolean canMoveLeft(Cell cell){
        if(cell.getWalls()[3]){
            return false;
        }
        else return cell.getCol() - 1 >= 0;
    }

    /**
     * Changes the {@code gameCell} to the corresponding direction of the move
     */

    public void moveUp(){
        gameCell = board[gameCell.getRow() - 1][gameCell.getCol()];
        movesCount++;
    }

    /**
     * Changes the {@code gameCell} to the corresponding direction of the move
     */

    public void moveRight(){
        gameCell = board[gameCell.getRow()][gameCell.getCol() + 1];
        movesCount++;
    }

    /**
     * Changes the {@code gameCell} to the corresponding direction of the move
     */

    public void moveDown(){
        gameCell = board[gameCell.getRow() + 1][gameCell.getCol()];
        movesCount++;
    }

    /**
     * Changes the {@code gameCell} to the corresponding direction of the move
     */

    public void moveLeft(){
        gameCell = board[gameCell.getRow()][gameCell.getCol() - 1];
        movesCount++;
    }

    /**
     *  Checks all the unvisited neighbours and selects a random one
     *
     * @param cell The {@code Cell} containing the {@code piece}
     * @return a random unvisited cell from the list of passed @param cell neighbours
     */
    public Cell checkNeighbours(Cell cell){
        List<Cell> neighbours = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();
        if(checkIndex(row - 1,col) && !board[row-1][col].getVisited() ){
            neighbours.add(board[row-1][col]);
        }
        if(checkIndex(row,col + 1) && !board[row][col+1].getVisited()){
            neighbours.add(board[row][col+1]);
        }
        if(checkIndex(row + 1,col) && !board[row + 1][col].getVisited()){
                neighbours.add(board[row + 1][col]);
        }
        if(checkIndex(row,col - 1) && !board[row][col - 1].getVisited()){
            neighbours.add(board[row][col - 1]);
        }

        Random r = new Random();
        if(!neighbours.isEmpty()) {
            return neighbours.get(r.nextInt(neighbours.size()));
        }

        return new Cell(true);
    }

    /**
     * Checks if the indices are valid within {@code BOARD_SIZE}
     * @param i Cell's Row
     * @param j Cell's Column
     * @return {@code true} if it is within {@code BOARD_SIZE} {@code false} otherwise
     */
    public Boolean checkIndex(int i,int j){
        if(i < 0 || j < 0 || i > BOARD_SIZE - 1 || j > BOARD_SIZE - 1){
            return false;
        }
        return true;
    }

    /**
     *  Iterative deepening algorithm responsible for modifying the walls of each cell to structure the labyrinth
     */
    public void generateMaze(){
        Cell currentCell = board[0][0];
        currentCell.setVisited(true);
        cellStack.push(currentCell);
        while(!cellStack.empty()){
            currentCell = cellStack.pop();
            Cell nextCell = checkNeighbours(currentCell);
            if(!nextCell.isInvalidCell()){
                cellStack.push(currentCell);
                if(currentCell.getRow() == nextCell.getRow() + 1){
                    board[nextCell.getRow()][nextCell.getCol()].getWalls()[2] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[0] = false;
                }
                else if (currentCell.getRow() == nextCell.getRow() - 1){
                    board[nextCell.getRow()][nextCell.getCol()].getWalls()[0] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[2] = false;
                }

                else if(currentCell.getCol() == nextCell.getCol() + 1){
                    board[nextCell.getRow()][nextCell.getCol()].getWalls()[1] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[3] = false;
                }
                else if(currentCell.getCol() == nextCell.getCol() - 1){
                    board[nextCell.getRow()][nextCell.getCol()].getWalls()[3] = false;
                    board[currentCell.getRow()][currentCell.getCol()].getWalls()[1] = false;
                }
                nextCell.setVisited(true);
                currentCell = nextCell;
                cellStack.push(currentCell);
            }
        }
    }

    /**
     * Determines whether the game has reached its end state
     * @return {@code true} if the player completed the maze {@code false} otherwise.
     */
    public boolean hasFinished(){
        if(gameCell.getRow() == goalCell.getRow() && gameCell.getCol() == goalCell.getCol()){
            finalScore = calculateScore(movesCount);
            return true;
        }
        return false;
    }

    /**
     * Calculates the player's final score
     * @param steps amount of moves that the player has made in order to complete the game
     * @return the game's final score
     */
    public double calculateScore(int steps){
        return ((1.0/steps) * 5000);
    }

}
