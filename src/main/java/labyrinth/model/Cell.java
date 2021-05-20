package labyrinth.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@lombok.Data
@lombok.RequiredArgsConstructor
public class Cell {
    private int row;
    private int col;
    private final double width = 50;
    private Boolean[] walls = {true,true,true,true};
    private Boolean visited = false;
    private boolean invalidCell = false;

    public Cell(boolean invalidCell){
        this.invalidCell = invalidCell;
    }

    public Cell(int row, int col){
        this.row = row;
        this.col = col;
    }

}
