package ca.cmpt213.client;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//contains a popup scene to show all tokimon
public class ShowAllTokimonHandler implements EventHandler<ActionEvent> {

    List<Tokimon> allTokimon = new ArrayList<Tokimon>();
    Stage newStage = new Stage();

    @Override
    public void handle(ActionEvent actionEvent) {

        List<String> allNames = new ArrayList<String>();
        List<Long> allID = new ArrayList<Long>();

        try{
            List<Button> buttons = new ArrayList<Button>();
            URL url = new URL("http://localhost:8080/api/tokimon/all");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String output;
            output = br.readLine();
            toTokimon(output);
            System.out.println(connection.getResponseCode());
            HBox buttonBox = new HBox();
            Label label = new Label();

            double max = 0;
            for(Tokimon toki: allTokimon){
                if(toki.getHeight() > max){
                    max = toki.getHeight();
                }
            }

            for(Tokimon toki: allTokimon){
                allNames.add(toki.getName());
                allID.add(toki.getId());
                Button temp = new Button("Name: " + toki.getName() + " ID: " + toki.getId());
                buttons.add(temp);
                if(toki.getColour().equals("Red")){
                    temp.setStyle("-fx-background-color: red");
                }
                else if(toki.getColour().equals("Orange")){
                    temp.setStyle("-fx-background-color: orange");
                }
                else if(toki.getColour().equals("Yellow")){
                    temp.setStyle("-fx-background-color: yellow");
                }
                else if(toki.getColour().equals("Green")){
                    temp.setStyle("-fx-background-color: green");
                }
                else{
                    temp.setStyle("-fx-background-color: blue");
                }
                temp.setMinHeight((toki.getHeight()/max)*200);
                buttonBox.getChildren().add(temp);
                temp.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        Button current = (Button) actionEvent.getSource();
                        int index = 0;
                        for(int a=0; a<buttons.size(); a++){
                            if(current == buttons.get(a)){
                                current = buttons.get(a);
                                index = a;
                                break;
                            }
                        }
                        String text = "ID: " + allTokimon.get(index).getId() + "\nName: " + allTokimon.get(index).getName() + "\nWeight: " + allTokimon.get(index).getWeight() + "\nHeight: " + allTokimon.get(index).getHeight() + " \nAbility: " + allTokimon.get(index).getAbility() + "\nStrength: " + allTokimon.get(index).getStrength() + "\nColour: " + allTokimon.get(index).getColour();
                        label.setText(text);
                    }
                });
            }
            Label info = new Label("Shown based on height, click on the Tokimon to view stats");
            VBox allData = new VBox(50, buttonBox, info, label);
            ScrollPane sp = new ScrollPane();
            sp.setContent(allData);
            Scene showAll = new Scene(sp, 1200, 500);
            Main.hideShow(showAll);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error ONE");
        }
    }

    public void toTokimon(String string){
        JsonParser parser = new JsonParser();
        JsonArray objArr = (JsonArray) parser.parse(string);
        List<Tokimon> allTemp = new ArrayList<Tokimon>();
        allTokimon = allTemp;
        for(Object temp: objArr){
            JsonObject toki = (JsonObject) temp;
            Tokimon tokimon = new Tokimon(toki.get("name").getAsString(),
                    toki.get("weight").getAsDouble(),
                    toki.get("height").getAsDouble(),
                    toki.get("ability").getAsString(),
                    toki.get("strength").getAsDouble(),
                    toki.get("colour").getAsString());
            tokimon.setId(toki.get("id").getAsLong());
            allTemp.add(tokimon);
        }
    }
}
