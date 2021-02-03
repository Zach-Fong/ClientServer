package ca.cmpt213.client;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.OutputStreamWriter;
import java.net.*;

//contains the main menu and its handlers
public class Main extends Application {
    private String abilityAddResult = "";
    private String ability = "";
    private String colourAddResult = "";
    private String colourEditResult = "";
    private Scene main;
    public static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{

        mainStage = primaryStage;

        //add elements
        Button addButton = new Button("Add Tokimon");;
        TextField nameAdd = new TextField();
        nameAdd.setPromptText("Enter Tokimon Name");
        TextField weightAdd = new TextField();
        weightAdd.setPromptText("Enter Tokimon Weight");
        TextField heightAdd = new TextField();
        heightAdd.setPromptText("Enter Tokimon Height");
        ListView<String> abilityAdd = new ListView<String>();
        abilityAdd.getItems().addAll("Fly", "Fire", "Water", "Electric", "Ice");
        abilityAdd.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TextField strengthAdd = new TextField();
        strengthAdd.setPromptText("Enter Tokimon Strength");
        ListView<String> colourAdd = new ListView<String>();
        colourAdd.getItems().addAll("Red", "Orange", "Yellow", "Green", "Blue");
        colourAdd.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Label addLabel = new Label();
        VBox addBox = new VBox(5, addButton, nameAdd, weightAdd, heightAdd, abilityAdd, strengthAdd, colourAdd, addLabel);

        //delete elements
        Button delButton = new Button("Delete Tokimon");
        TextField delText = new TextField();
        delText.setPromptText("Enter Tokimon ID");
        HBox delBox = new HBox(20, delButton, delText);

        //edit elements
        Button editButton = new Button("Edit Tokimon");
        TextField editText = new TextField();
        editText.setPromptText("Enter Tokimon ID");
        TextField name = new TextField();
        name.setPromptText("Enter Tokimon Name");
        TextField weight = new TextField();
        weight.setPromptText("Enter Tokimon Weight");
        TextField height = new TextField();
        height.setPromptText("Enter Tokimon Height");
        ListView<String> abilityEdit = new ListView<String>();
        abilityEdit.getItems().addAll("Fly", "Fire", "Water", "Electric", "Ice", "No Change");
        abilityEdit.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TextField strength = new TextField();
        strength.setPromptText("Enter Tokimon Strength");
        ListView<String> colourEdit = new ListView<String>();
        colourEdit.getItems().addAll("Red", "Orange", "Yellow", "Green", "Blue", "No Change");
        colourEdit.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Label editLabel = new Label();
        VBox editBox = new VBox(5, editButton, editText, name, weight, height, abilityEdit, strength, colourEdit, editLabel);

        //show elements
        Button showButton = new Button("Show All Tokimon");

        HBox rowOne = new HBox(50, addBox, editBox);
        HBox rowTwo= new HBox(50, delBox);
        VBox allBox = new VBox(50, rowOne, rowTwo, showButton);
        allBox.setPadding(new Insets(50,50,50,50));

        primaryStage.setTitle("Tokimon Menu");

        primaryStage.setScene(main = new Scene(allBox, 500, 800));
        primaryStage.show();

        showButton.setOnAction(new ShowAllTokimonHandler());

        abilityAdd.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                String current = change.getList().get(change.getList().size()-1);
                abilityAddResult = current;
            }
        });

        abilityEdit.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                String current = change.getList().get(change.getList().size()-1);
                ability = current;
                if(current.equals("No Change")){
                    current = "";
                }
            }
        });

        colourAdd.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                colourAddResult = change.getList().get(change.getList().size()-1);
            }
        });

        colourEdit.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> change) {
                colourEditResult = change.getList().get(change.getList().size()-1);
                if(colourEditResult.equals("No Change") || change.getList().size() == 0){
                    colourEditResult = "";
                }
            }
        });

        delButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try{
                    long id = Long.parseLong(delText.getText());
                    URL url = new URL("http://localhost:8080/api/tokimon/" + id);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("DELETE");

                    connection.connect();
                    System.out.println(connection.getResponseCode());
                    connection.disconnect();
                }
                catch (Exception e){
                    System.out.println("Error");
                }
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!nameAdd.getText().isEmpty() && !weightAdd.getText().isEmpty() && !heightAdd.getText().isEmpty() && !abilityAddResult.equals("") && !strengthAdd.getText().isEmpty() && !colourAddResult.equals("")){
                    if(Double.parseDouble(weightAdd.getText()) < 0 || Double.parseDouble(heightAdd.getText()) < 0 ||Double.parseDouble(strengthAdd.getText()) < 0 || Double.parseDouble(strengthAdd.getText()) > 100){
                        addLabel.setText("Invalid input value");
                        return;
                    }
                    addLabel.setText("");
                    try{
                        String name = nameAdd.getText();
                        double weight = Double.parseDouble(weightAdd.getText());
                        double height = Double.parseDouble(heightAdd.getText());
                        double strength = Double.parseDouble(strengthAdd.getText());
                        String colour = colourAddResult;

                        URL url = new URL("http://localhost:8080/api/tokimon/add");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");

                        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                        wr.write("{\"name\":\"" + name +
                                "\",\"weight\":" + weight +
                                ",\"height\":" + height +
                                ",\"ability\":\"" + abilityAddResult +
                                "\",\"strength\":" + strength +
                                ",\"colour\":\"" + colour + "\"}");
                        wr.flush();
                        wr.close();

                        connection.connect();
                        System.out.println(connection.getResponseCode());
                        connection.disconnect();
                    }
                    catch (Exception e){
                        System.out.println("Error with input");
                    }
                }
                else{
                    addLabel.setText("Missing or Incorrect Values");
                }
            }
        });

        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String all = "?";
                if(!editText.getText().isEmpty()){
                    try{
                        if(!name.getText().isEmpty()){
                            all += "name=" + name.getText() + "&";
                        }
                        if(!weight.getText().isEmpty()){
                            if(Double.parseDouble(weight.getText()) < 0){
                                editLabel.setText("Negative Value");
                                return;
                            }
                            all += "weight=" + Double.parseDouble(weight.getText()) + "&";
                        }
                        if(!height.getText().isEmpty()){
                            if(Double.parseDouble(height.getText()) < 0){
                                editLabel.setText("Negative Value");
                                return;
                            }
                            all += "height=" + Double.parseDouble(height.getText()) + "&";
                        }
                        if(!ability.equals("")){
                            all += "ability=" + ability + "&";
                        }
                        if(!strength.getText().isEmpty()){
                            if(Double.parseDouble(strength.getText()) < 0 || Double.parseDouble(strength.getText()) > 100){
                                editLabel.setText("Invalid value");
                                return;
                            }
                            all += "strength=" + Double.parseDouble(strength.getText()) + "&";
                        }
                        if(!colourEditResult.equals("")){
                            all += "colour=" + colourEditResult + "&";
                        }
                        all = all.substring(0, all.length()-1);
                    }
                    catch(Exception e){
                        System.out.println("Error First");
                        return;
                    }
                    try{
                        editLabel.setText("");
                        URL url = new URL("http://localhost:8080/api/tokimon/change/" + editText.getText() + all);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(false);
                        connection.setRequestMethod("POST");

                        connection.connect();
                        System.out.println(connection.getResponseCode());
                        connection.disconnect();
                    }
                    catch (Exception e){
                        System.out.println("Error second");
                    }

                }
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void hideShow(Scene newScene){
        Stage newStage = new Stage();
        newStage.setTitle("Tokimon Viewer");

        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.initOwner(mainStage);

        newStage.setScene(newScene);
        newStage.show();
    }
    public Stage getMainStage(){
        return mainStage;
    }

    public Scene getMain(){
        return main;
    }
}
