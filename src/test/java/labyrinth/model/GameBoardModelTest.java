package labyrinth.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardModelTest {
    private static GameBoardModel testModel;
    private static Cell testCell;
    private static Cell testBaseGameCell;

    @BeforeAll
    static void init(){
        testModel = new GameBoardModel();
        testCell = new Cell(0,0);
        testBaseGameCell = new Cell(0,0);
    }


    @Test
    void initializeBoard() {
        testModel.initializeBoard(testModel.getBoard());
        assertNotEquals(testModel.getBoard(),new GameBoardModel().getBoard());
        assertEquals(testModel.getGoalCell(),new Cell(testModel.BOARD_SIZE-1,testModel.BOARD_SIZE-1));
        assertEquals(testModel.getBoard().length,testModel.BOARD_SIZE);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            Cell c = testModel.getBoard()[testModel.BOARD_SIZE][testModel.BOARD_SIZE-1];
        });
        assertFalse(Arrays.deepEquals(testModel.getBoard(), new GameBoardModel().getBoard()));
    }

    @Test
    void canMoveUp() {
        testCell.setRow(3);
        testCell.setCol(3);
        testCell.setWalls(new Boolean[] {false,true,true,true});
        assertTrue(testModel.canMoveUp(testCell));
        testCell.setRow(0);
        assertFalse(testModel.canMoveUp(testCell));

    }

    @Test
    void canMoveRight() {
        testCell.setRow(testModel.BOARD_SIZE - 1);
        testCell.setCol(testModel.BOARD_SIZE - 1);
        testCell.setWalls(new Boolean[] {true,true,true,true});
        assertFalse(testModel.canMoveRight(testCell));
        testCell.setWalls(new Boolean[] {true,true,true,true});
        testCell.setRow(3);
        testCell.setCol(4);
        assertFalse(testModel.canMoveRight(testCell));
        testCell.setWalls(new Boolean[] {true,false,true,true});
        assertTrue(testModel.canMoveRight(testCell));
    }

    @Test
    void canMoveDown() {
        testCell.setRow(testModel.BOARD_SIZE - 1);
        testCell.setWalls(new Boolean[] {true,true,false,true});
        assertFalse(testModel.canMoveDown(testCell));
        testCell.setWalls(new Boolean[] {true,true,true,true});
        testCell.setRow(2);
        assertFalse(testModel.canMoveDown(testCell));
        testCell.setWalls(new Boolean[] {true,true,false,true});
        assertTrue(testModel.canMoveDown(testCell));
    }

    @Test
    void canMoveLeft() {
        testCell.setCol(0);
        testCell.setWalls(new Boolean[] {true,true,true,false});
        assertFalse(testModel.canMoveLeft(testCell));
        testCell.setWalls(new Boolean[] {true,true,true,true});
        testCell.setCol(1);
        assertFalse(testModel.canMoveLeft(testCell));
        testCell.setWalls(new Boolean[] {true,true,true,false});
        assertTrue(testModel.canMoveLeft(testCell));
    }

    @Test
    void moveUp() {
        testBaseGameCell.setRow(0);
        testModel.setGameCell(testBaseGameCell);
        testModel.moveUp();
        assertEquals(testModel.getGameCell(),testBaseGameCell);
        testBaseGameCell.setRow(3);
        testModel.setGameCell(testBaseGameCell);
        testModel.moveUp();
        assertNotEquals(testBaseGameCell,testModel.getGameCell());

    }

    @Test
    void moveRight() {
        testBaseGameCell.setCol(2);
        testModel.setGameCell(testBaseGameCell);
        testModel.moveRight();
        assertNotEquals(testModel.getGameCell(),testBaseGameCell);
        testBaseGameCell.setCol(testModel.BOARD_SIZE-1);
        testModel.setGameCell(testBaseGameCell);
        testModel.moveRight();
        assertEquals(testBaseGameCell,testModel.getGameCell());
    }

    @Test
    void moveDown() {
        testModel.setGameCell(testBaseGameCell);
        testModel.moveDown();
        assertNotEquals(testModel.getGameCell(),testBaseGameCell);
        testBaseGameCell.setRow(testModel.BOARD_SIZE-1);
        testModel.setGameCell(testBaseGameCell);
        testModel.moveDown();
        assertEquals(testBaseGameCell,testModel.getGameCell());
    }

    @Test
    void moveLeft() {
        testBaseGameCell.setCol(0);
        testModel.setGameCell(testBaseGameCell);
        testModel.moveLeft();
        assertEquals(testModel.getGameCell(),testBaseGameCell);
        testBaseGameCell.setCol(testModel.BOARD_SIZE-1);
        testModel.setGameCell(testBaseGameCell);
        testModel.moveLeft();
        assertNotEquals(testBaseGameCell,testModel.getGameCell());
    }

    @Test
    void checkIndex() {
        assertFalse(testModel.checkIndex(testModel.BOARD_SIZE,testModel.BOARD_SIZE));
        assertTrue(testModel.checkIndex(testModel.BOARD_SIZE-1,testModel.BOARD_SIZE-1));
        assertFalse(testModel.checkIndex(testModel.BOARD_SIZE-1,testModel.BOARD_SIZE));
        assertFalse(testModel.checkIndex(testModel.BOARD_SIZE,testModel.BOARD_SIZE-1));
    }
    @Test
    void generateMaze() {
        GameBoardModel testMazeModel = new GameBoardModel();
        testMazeModel.generateMaze();
        assertFalse(Arrays.deepEquals(testModel.getBoard(), testMazeModel.getBoard()));
    }

    @Test
    void hasFinished() {
        testBaseGameCell.setRow(testModel.getGoalCell().getRow());
        testBaseGameCell.setCol(testModel.getGoalCell().getCol());
        testModel.setGameCell(testBaseGameCell);
        System.out.println(testModel.getGameCell().getCol());
        assertTrue(testModel.hasFinished());
        testModel.setGameCell(new Cell (testModel.BOARD_SIZE-2, testModel.BOARD_SIZE-1));
        assertFalse(testModel.hasFinished());
    }

    @Test
    void calculateScore() {
        testModel.calculateScore(50);
        assertEquals(testModel.calculateScore(50),((1.0/50) * 5000));
        assertEquals(testModel.calculateScore(0),0);
        assertEquals(testModel.calculateScore(-1),0);
        assertEquals(testModel.calculateScore(Integer.MIN_VALUE),0);
    }
}