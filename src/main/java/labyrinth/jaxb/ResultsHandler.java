package labyrinth.jaxb;

import jakarta.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResultsHandler {
    private LeaderboardResults resultList = new LeaderboardResults();

    public ResultsHandler(){
        File file = new File("gameresult.xml");
        if(file.exists()) {
            try {

                this.resultList = JAXBHelper.fromXML(LeaderboardResults.class, new FileInputStream("gameresult.xml"));
            }
            catch (JAXBException  ex) {
                ex.printStackTrace();
            }
            catch(FileNotFoundException ex){
                ex.printStackTrace();
            }
        }
        else{
            try {
                file.createNewFile();
            } catch (IOException ioException) {
                System.out.println("Directory Doesn't Exist");
            }
            resultList.setGameResultsList(new ArrayList<>());
        }
    }

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

    public List<GameResult> getGameResultsList() {
        return resultList.getGameResultsList();
    }
}
