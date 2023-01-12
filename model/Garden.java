package model;



import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Represents a garden with plants
public class Garden {
    private List<Plant> garden;

    // EFFECTS: constructs an empty garden
    public Garden() {
        garden = new ArrayList<>();
    }

    // EFFECTS: returns an immutable version of the garden
    public List<Plant> listPlants() {
        return Collections.unmodifiableList(garden);
    }

    // REQUIRES: the garden is not empty
    // EFFECTS: returns a list of the plants currently planted in the garden
    public List<String> listPlantTypes() {
        List<String> myGarden = new ArrayList<>();
        for (Plant p : garden) {
            myGarden.add(p.getColour() + " " + p.getType());
        }
        return myGarden;
    }

    // MODIFIES: this
    // EFFECTS: adds this p to the list of fully grown plants
    public List<String> listFullyGrown() {
        List<String> myGrownPlants = new ArrayList<>();

        for (int i = 0; i < allFullyGrown().gardenSize(); i++) {
            Plant p = allFullyGrown().getPlant(i);
            myGrownPlants.add(p.getColour() + " " + p.getType());

        }

        return myGrownPlants;
    }


    // EFFECTS: returns the garden of fully grown plants
    public Garden allFullyGrown() {
        Garden grownPlants = new Garden();
        for (Plant p : garden) {
            if (p.isFullyGrown()) {
                grownPlants.plantSeed(p);

            }
        }
        return grownPlants;
    }

    // REQUIRES: the garden is not empty
    // EFFECTS: retrieves the plant at position i
    public Plant getPlant(int i) {
        return garden.get(i);

    }


    // MODIFIES: this
    // EFFECTS: plants the selected seed into the garden
    public void plantSeed(Plant p) {
        garden.add(p);
        EventLog.getInstance().logEvent(new Event("Planted/moved a plant."));
    }

    // REQUIRES: the garden is not empty
    // EFFECTS: returns true if the garden contains the stated plant
    public boolean containsPlant(Plant p) {
        return garden.contains(p);
    }


    // REQUIRES: the garden is not empty
    // MODIFIES: this
    // EFFECTS: removes the selected plant from the garden
    public void potSeed(Plant p) {
        if (garden.contains(p)) {
            garden.remove(p);
            EventLog.getInstance().logEvent(new Event("Potted a plant from either regular or grown garden."));
        }

    }

    // MODIFIES: this
    // EFFECTS: returns the number of plants in the garden
    public int gardenSize() {
        return garden.size();

    }

    /*
    weatherForecast() code referenced from stack overflow
    https://stackoverflow.com/questions/6726963/random-string-from-string-array-list
     */

    // EFFECTS: returns a randomly generated weather forecast
    public String generateWeather() {
        List<String> weatherStates = new ArrayList<>();
        weatherStates.add("Sunny. Better wear some sunscreen!");
        weatherStates.add("Cloudy. Not too bad!");
        weatherStates.add("Windy. Brrrr.");
        weatherStates.add("Light showers. Guess you can skip watering today!");
        weatherStates.add("Heavy Rain. Got an umbrella?");

        int index = new Random().nextInt(weatherStates.size());
        String weather = weatherStates.get(index);
        EventLog.getInstance().logEvent(new Event("Gave the weather forecast."));
        return weather;

    }

    /*
    This method references code from JsonSerializationDemo
    Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    */
    public JSONObject thisToJson() {
        JSONObject json = new JSONObject();

        json.put("plants", plantsToJson());
        return json;
    }

    /*
    This method references code from JsonSerializationDemo
    Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
        1*/
    // EFFECTS: returns the plants in this garden as a JSON array
    private JSONArray plantsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Plant p : garden) {
            jsonArray.put(p.thisToJson());
        }
        return jsonArray;

    }
}





