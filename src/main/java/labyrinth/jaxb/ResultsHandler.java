package labyrinth.jaxb;
import jakarta.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class that governs the structure of JAXB output
 */

public class ResultsHandler {
    /**
     * {@code List} that holds all the {@code GameResult} objects
     */
    private LeaderboardResults resultList = new LeaderboardResults();

    /**
     * Parameterless constructor that Initializes {@code resultList} and handles JAXB Output file creation
     */
    public ResultsHandler(){
        File file = new File("gameresult.xml");
        if(file.exists()) {
            try {

                this.resultList = JAXBHelper.fromXML(LeaderboardResults.class, new FileInputStream("gameresult.xml"));
            }
            catch (JAXBException | FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        else{
            try {
                if (file.createNewFile()) {

                } else {

                }
            } catch (IOException ioException) {

            }
            resultList.setGameResultsList(new ArrayList<>());
        }
    }

    /**
     * @param name Player's name
     * @param steps Amount of moves player has made
     * @param finalScore Player's score
     * @param outcome Result of the game (Solved/Given up)
     * @param timeStampBegin date time of starting the game
     */
    public void commitResult(String name, int steps, double finalScore, boolean outcome,String timeStampBegin){
        List<GameResult> results = resultList.getGameResultsList();
        results.add(new GameResult(steps,finalScore,outcome,name,timeStampBegin));
        resultList.setGameResultsList(results);

        try {
            JAXBHelper.toXML(resultList, new FileOutputStream("gameresult.xml"));
        } catch (JAXBException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * @return list of {@code GameResult}
     */
    public List<GameResult> getGameResultsList() {
        return resultList.getGameResultsList();
    }
}
