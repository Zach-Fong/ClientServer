package ca.cmpt213.web.controller;
import ca.cmpt213.web.model.Tokimon;
import com.google.gson.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

//controller class for tokimon
@RestController
public class TokimonController {
    private long count = setCount();
    private List<Tokimon> allTokimon = new ArrayList<Tokimon>();

    @GetMapping("/api/tokimon/all")
    public List<Tokimon> getPeople(HttpServletResponse response){
        System.out.println("endpoint for getting all tokimon");
        toTokimon();
        response.setStatus(200);
        return allTokimon;
    }

    @GetMapping("/api/tokimon/{id}")
    public Tokimon getTokimon(@PathVariable long id, HttpServletResponse response){
        for(Tokimon toki: allTokimon){
            if(toki.getId() == id){
                response.setStatus(200);
                writeToJSON();
                return toki;
            }
        }
        response.setStatus(404, "ID not valid");
        return null;
    }

    @PostMapping("/api/tokimon/add")
    public void addTokimon(@RequestBody Tokimon tokimon, HttpServletResponse response){
        tokimon.setId(count);
        count++;
        allTokimon.add(tokimon);
        response.setStatus(201);
        writeToJSON();
    }

    @PostMapping("/api/tokimon/change/{id}")
    public void changeID(@RequestParam(value = "name", defaultValue = "") String name,
                         @RequestParam(value = "weight", defaultValue = "-1") double weight,
                         @RequestParam(value = "height", defaultValue = "-1") double height,
                         @RequestParam(value = "ability", defaultValue = "") String ability,
                         @RequestParam(value = "strength", defaultValue = "-1") double strength,
                         @RequestParam (value = "colour", defaultValue = "") String colour,
                         @PathVariable long id,
                         HttpServletResponse response){
        Tokimon toki = new Tokimon();
        for(Tokimon temp: allTokimon){
            if(temp.getId() == id){
                toki = temp;
                break;
            }
        }
        if(toki.getId() == -1){
            response.setStatus(404, "ID not valid");
            return;
        }
        response.setStatus(201);
        if(!name.equals("")){
            toki.setName(name);
        }
        if(weight != -1){
            toki.setWeight(weight);
        }
        if(height != -1){
            toki.setHeight(height);
        }
        if(!ability.equals("")){
            toki.setAbility(ability);
        }
        if(strength != -1){
            toki.setStrength(strength);
        }
        if(!colour.equals("")){
            toki.setColour(colour);
        }
        writeToJSON();
    }

    @DeleteMapping("/api/tokimon/{id}")
    public void deletedID(@PathVariable long id, HttpServletResponse response){
        for(Tokimon toki: allTokimon){
            if(toki.getId() == id){
                allTokimon.remove(toki);
                response.setStatus(204);
                writeToJSON();
                return;
            }
        }
        response.setStatus(404, "ID not valid");
    }

    public void writeToJSON(){
        String all = "[";
        for(Tokimon toki: allTokimon){
            all += "{\"id\":" + toki.getId() +
                    ",\"name\":\"" + toki.getName() +
                    "\",\"weight\":" + toki.getWeight() +
                    ",\"height\":" + toki.getHeight() +
                    ",\"ability\":\"" + toki.getAbility() +
                    "\",\"strength\":" + toki.getStrength() +
                    ",\"colour\":\"" + toki.getColour()+ "\"},";
        }
        all = all.substring(0, all.length()-1);
        if(all.length() == 0){
            all += "[";
        }
        all += "]";
        String filepath = "./data/tokimon.json";
        try{
            Formatter f = new Formatter(filepath);
            f.format(all);
            f.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public long setCount(){
        toTokimon();
        long max = 0;
        for(Tokimon toki: allTokimon){
            if(toki.getId() > max){
                max = toki.getId();
            }
        }
        max += 1;
        return max;
    }

    public void toTokimon(){
        try {
            File file = new File("./data/tokimon.json");
            JsonParser parser = new JsonParser();
            JsonArray objArr = (JsonArray) parser.parse(new FileReader(file));
            List<Tokimon> allTemp = new ArrayList<Tokimon>();
            allTokimon = allTemp;
            for (Object temp : objArr) {
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
        catch (Exception e){
            System.out.println("empty");
        }
    }
}
