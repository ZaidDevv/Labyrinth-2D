import labyrinth.model.GameBoardModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



public class GameBoardModelTest {
    private static GameBoardModel model;
    @BeforeAll
    public void constructModel(){
        model = new GameBoardModel();
    }

}
