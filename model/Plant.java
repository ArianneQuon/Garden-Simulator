package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Objects;

// Represents a plant with a type and colour
public class Plant implements Writable {
    private String type;
    private String colour;
    private boolean fullyGrown;

    // EFFECTS: constructs a plant with this colour and this type
    public Plant(String plantColour, String typeOfPlant) {
        this.type = typeOfPlant;
        this.colour = plantColour;
        fullyGrown = false;

    }

    //getters
    public String getType() {
        return type;
    }

    public String getColour() {
        return colour;
    }

    public boolean isFullyGrown() {
        return fullyGrown;
    }

    // setters

    // MODIFIES: this
    // EFFECTS: sets fullyGrown to true
    public void setFullyGrown() {
        fullyGrown = true;
    }

    // MODIFIES: this
    // EFFECTS: sets the plant's colour
    public void setPlantColour(String col) {
        this.colour = col;
    }

    // MODIFIES: this
    // EFFECTS: set the plant's type
    public void setPlantType(String plantType) {
        this.type = plantType;
    }

    // REQUIRES: the garden is not empty
    // EFFECTS: returns true if the garden contains a plant with the stated colour and type
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Plant) {
            Plant comparePlant = (Plant) obj;

            return (this.colour.equals(comparePlant.colour) && this.type.equals(comparePlant.type));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, colour);
    }

    /*
        This method references code from JsonSerializationDemo
        Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
        */
    @Override
    public JSONObject thisToJson() {
        JSONObject jsonPlant = new JSONObject();
        jsonPlant.put("type", type);
        jsonPlant.put("colour", colour);
        return jsonPlant;
    }
}
