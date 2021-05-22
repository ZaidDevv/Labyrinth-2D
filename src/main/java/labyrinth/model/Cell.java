package labyrinth.model;

@lombok.Data
@lombok.RequiredArgsConstructor
public class Cell {
    /**
     * Row of the cell within {@code board}
     */
    private int row;
    /**
     * column of the cell within {@code board}
     */
    private int col;
    /**
     * Width of each cell
     */
    private final double width = 50;
    /**
     * walls surrounding the cell in the following order (top, right, bottom, left)
     */
    private Boolean[] walls = {true,true,true,true};
    /**
     * indicates if the cell been visited by our IDFS or not.
     */
    private Boolean visited = false;
    /**
     * an invalid cell that doesn't conform to the rules
     */
    private boolean invalidCell = false;

    /**
     *
     * @param invalidCell {@code true} if IDFS reaches a dead-end
     */
    public Cell(boolean invalidCell){
        this.invalidCell = invalidCell;
    }

    /**
     *
     * @param row row of the cell within {@code board}
     * @param col column of the cell within {@code board}
     */
    public Cell(int row, int col){
        this.row = row;
        this.col = col;
    }

}
