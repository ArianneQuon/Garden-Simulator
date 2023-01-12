package ui;

import model.Garden;
import model.Plant;
import persistence.GardenReader;
import persistence.GardenWriter;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Gardener {
    private Garden myGarden;
    private Garden allFullyGrown;
    private List<String> fullyGrown;
    private List<String> allPlants;
    private Scanner inputA;
    private Scanner inputB;
    private GardenWriter gardenWriter;
    private GardenWriter gardenWriterGrown;
    private GardenReader gardenReader;
    private GardenReader gardenReaderGrown;
    private static final String STORE_GARDEN = "./data/garden.json";
    private static final String STORE_GARDEN_GROWN = "./data/gardengrown.json";

    // Represents and contains the gardening application
    public Gardener() throws FileNotFoundException {
        gardeningTime();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void gardeningTime() {
        boolean keepGardening = true;
        String choice = null;

        initGarden();

        System.out.println("Welcome to the garden!");

        while (keepGardening) {
            gardeningActions();
            choice = inputA.next();
            choice = choice.toLowerCase();


            if (choice.equals("leave")) {

                keepGardening = false;
            } else {
                doSomeGardening(choice);
            }

        }
        System.out.println("Leaving the garden. Till next time!");
    }

    // EFFECTS: initializes the program
    public void initGarden() {
        myGarden = new Garden();
        allFullyGrown = myGarden.allFullyGrown();
        fullyGrown = allFullyGrown.listFullyGrown();
        allPlants = myGarden.listPlantTypes();

        inputA = new Scanner(System.in);
        inputB = new Scanner(System.in);

        gardenWriter = new GardenWriter(STORE_GARDEN);
        gardenWriterGrown = new GardenWriter(STORE_GARDEN_GROWN);
        gardenReader = new GardenReader(STORE_GARDEN);
        gardenReaderGrown = new GardenReader(STORE_GARDEN_GROWN);
    }

    // EFFECTS: displays the gardening choices
    public void gardeningActions() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("\tplant -> select a seed to plant in the garden");
        System.out.println("\tpot -> select a fully grown plant to remove and pot from the garden");
        System.out.println("\tlist -> list the current plants within the garden");
        System.out.println("\tweather -> check the weather forecast");
        System.out.println("\tsave -> save the garden to continue gardening later");
        System.out.println("\tload -> return from where you left off");
        System.out.println("\tleave -> leave the garden");

    }

    // MODIFIES: this
    // EFFECTS performs the users gardening choice
    private void doSomeGardening(String choice) {
        if (choice.equals("plant")) {
            plantPlant();
        } else if (choice.equals("pot")) {
            potPlant();
        } else if (choice.equals("list")) {
            listGardenPlants();
        } else if (choice.equals("weather")) {
            weatherForecast();
        } else if (choice.equals("save")) {
            saveGarden();
        } else if (choice.equals("load")) {
            loadGarden();
        } else {
            System.out.println("Unable to perform the action. Try again");
        }

    }

    // MODIFIES: this
    // EFFECTS: performs the action of planting a seed
    private void plantPlant() {
        System.out.println("\nChoose a colour, then specify the type of plant you want (colour >enter< plant): ");
        System.out.println("\tRose");
        System.out.println("\tHyacinth");
        System.out.println("\tCarnation");
        System.out.println("\tTulip");
        System.out.println("\tGardenia");
        System.out.println("\tCamellia");

        String plantColourChoice = inputA.next();
        String plantChoice = inputB.next();

        Plant newPlant = new Plant(plantColourChoice, plantChoice);

        allPlants.add(newPlant.getColour() + newPlant.getType());

        myGarden.plantSeed(newPlant);

        System.out.println("Planted " + newPlant.getColour() + " " + newPlant.getType() + "!");

    }

    // REQUIRES: the grown garden is not empty
    // MODIFIES: this
    // EFFECTS: performs the action of potting a plant
    private void potPlant() {
        System.out.println("Here is a list of the fully grown plants in your garden:");

        System.out.println(allFullyGrown.listFullyGrown());

        if (allFullyGrown.gardenSize() == 0) {
            System.out.println("No plants to pot! Try again later.");
        } else {
            Plant p = allFullyGrown.getPlant(allFullyGrown.gardenSize() - 1);
            p.setFullyGrown();
            System.out.println("Which plant would you like to pot? (colour >enter< plant)");
            String potColourChoice = inputA.next();
            String potChoice = inputB.next();

            Plant oldPlant = new Plant(potColourChoice, potChoice);
            oldPlant.setFullyGrown();

            if (p.equals(oldPlant)) {
                myGarden.potSeed(p);
                allPlants.remove(p.getColour() + " " + p.getType());
                fullyGrown.remove(p.getColour() + " " + p.getType());
                allFullyGrown.potSeed(p);

                System.out.println("Potted " + potColourChoice + " " + potChoice + "!");
            } else {
                System.out.println("Could not pot that plant.");
            }
        }
    }

    // EFFECTS: returns the list of plants in the garden
    private void listGardenPlants() {
        List<String> allPlants = myGarden.listPlantTypes();
        if (allPlants.isEmpty()) {
            System.out.println("No plants in the garden yet! Let's start gardening.");
        } else {
            System.out.println(allPlants);
        }
    }

    // MODIFIES: this
    // EFFECTS: gives the weather forecast and grows this plant in the garden
    private void weatherForecast() {
        Plant growPlant = myGarden.getPlant(myGarden.gardenSize() - 1);

        growPlant.setFullyGrown();
        fullyGrown.add(growPlant.getColour() + " " + growPlant.getType());
        allFullyGrown.plantSeed(growPlant);

        String currentWeather = myGarden.generateWeather();

        System.out.println(currentWeather);
    }

    /*
    This method references code from JsonSerializationDemo
    Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    */
    // EFFECTS: saves the current state of the garden to file
    private void saveGarden() {
        try {
            gardenWriter.open();
            gardenWriterGrown.open();
            gardenWriter.write(myGarden);
            gardenWriterGrown.write(allFullyGrown);
            gardenWriter.close();
            gardenWriterGrown.close();
            System.out.println("Saved your garden to  " + STORE_GARDEN);
        } catch (FileNotFoundException e) {
            System.out.println("Error reading from file: " + STORE_GARDEN);
        }
    }

    /*
    This method references code from JsonSerializationDemo
    Link: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    */
    // EFFECTS: loads the garden from file
    private void loadGarden() {
        try {
            myGarden = gardenReader.read();
            allFullyGrown = gardenReaderGrown.read();
            System.out.println("Loaded your garden from " + STORE_GARDEN);

        } catch (IOException e) {
            System.out.println("Unable to read from file: " + STORE_GARDEN);
        }
    }

}
