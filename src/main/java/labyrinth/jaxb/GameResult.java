package labyrinth.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Contains all the necessary data to be displayed on the leaderboard
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class GameResult {
    /**
     * Stores the number of moves made by the player
     */
    private int steps;
    /**
     * Game's final score, {@code 0.0} if the game hasn't finished
     */
    private double finalScore;
    /**
     * Result of the game (Finished / Given up)
     */
    private boolean outcome;
    /**
     * Player's Name
     */
    private String name;
    /**
     * The date and time when the game was started
     */
    private String startDateTime;

}
