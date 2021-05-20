
package labyrinth.jaxb;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import labyrinth.model.GameBoardModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement(name = "position")
@XmlAccessorType(XmlAccessType.FIELD)
public class LeaderboardResults {
    @XmlElement(name = "result")
    private List<GameResult> gameResultsList;
}
