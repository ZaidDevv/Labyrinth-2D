package labyrinth.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


/**
 * Class that stores a {@code List} of {@code GameResult}'s.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement(name = "position")
@XmlAccessorType(XmlAccessType.FIELD)
public class LeaderboardResults {
    /**
     * The List that contains {@code GameResult}'s.
     */
    @XmlElement(name = "result")
    private List<GameResult> gameResultsList;
}
