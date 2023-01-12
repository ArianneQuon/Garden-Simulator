package persistence;
/*
This class references code from JsonSerializationDemo
Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

import model.Garden;
import model.Plant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader reading JSON data about garden on file
public class GardenReader {
    private String source;

    // EFFECTS: constructs reader that reads from the source file
    public GardenReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads garden from file and returns it; throws
    // IOException attempt to read data from file gives an error
    public Garden read() throws IOException {
        String gardenData = readFile(source);
        JSONObject gardenObject = new JSONObject(gardenData);
        return parseGarden(gardenObject);

    }

    // EFFECTS: reads source file as string and returns it; throws IOException if
    // attempt to rad from file gives an error
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();

    }

    // EFFECTS: parses garden from JSON object and returns it
    private Garden parseGarden(JSONObject jsonPlant) {
        Garden g = new Garden();
        addPlants(g, jsonPlant);
        return g;
        
    }

    // MODIFIES: g
    // EFFECTS: parses plants from JSON object and adds them to the garden
    private void addPlants(Garden g, JSONObject jsonPlant) {
        JSONArray jsonArray = jsonPlant.getJSONArray("plants");

        for (Object p : jsonArray) {
            JSONObject nextPlant = (JSONObject) p;
            addPlant(g, nextPlant);

        }
    }

    // MODIFIES: g
    // EFFECTS: parses plant from JSON object and adds it to the garden
    private void addPlant(Garden g, JSONObject jsonPlant) {
        String type = jsonPlant.getString("type");
        String colour = jsonPlant.getString("colour");
        Plant p = new Plant(colour, type);
        g.plantSeed(p);

    }

}

