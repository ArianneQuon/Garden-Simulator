package persistence;
/*
This class references code from JsonSerializationDemo
Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
 */

import model.Garden;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Represents a writer that saves JSON representation of garden to file
public class GardenWriter {
    private static final int TAB = 4;
    private PrintWriter gardenWriter;
    private String destination;

    // EFFECTS: constructs the writer to save the destination to
    public GardenWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file
    // cannot be opened to write to
    public void open() throws FileNotFoundException {
        gardenWriter = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of garden to file
    public void write(Garden g) {
        JSONObject jsonGarden = g.thisToJson();
        saveToFile(jsonGarden.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes gardenWriter
    public void close() {
        gardenWriter.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String s) {
        gardenWriter.print(s);
    }
}
