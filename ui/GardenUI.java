package ui;

/*
This class was developed mainly through reading documentation from:
https://docs.oracle.com/javase/tutorial/uiswing/TOC.html
 */

import model.Event;
import model.EventLog;
import model.Garden;
import model.Plant;
import persistence.GardenReader;
import persistence.GardenWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


// Represents application's main window frame in which the garden is shown
public class GardenUI extends JFrame {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 1200;
    private static final int BUTTON_WIDTH = 300;
    private static final int BUTTON_HEIGHT = 90;


    private JFrame frame;
    private JPanel startPanel;
    private JPanel loadPanel;
    private JPanel titlePanel;
    private JPanel homePageArt;
    private JButton start;
    private JButton load;
    private JLabel title;
    private JLabel art;

    private JFrame inGarden;

    private JButton plantButton;
    private JButton growButton;
    private JButton weatherButton;
    private JButton potButton;
    private JButton leaveButton;
    private JButton saveButton;

    private JPanel optionPanel;
    private JLabel gardenLabel;
    private JLabel grownLabel;
    private JPanel planter;
    private JPanel grownPlanter;

    private Garden garden;
    private Garden grownGarden;
    private List<JLabel> currentGarden;
    private List<JLabel> currentGrownGarden;
    private JLabel currentPlant;
    private Plant thisPlant;

    private GardenWriter gardenWriter;
    private GardenWriter gardenWriterGrown;
    private GardenReader gardenReader;
    private GardenReader gardenReaderGrown;
    private static final String STORE_GARDEN = "./data/garden.json";
    private static final String STORE_GARDEN_GROWN = "./data/gardengrown.json";

    // EFFECTS: constructs the button panel and visual garden state
    public GardenUI() {
        garden = new Garden();
        grownGarden = garden.allFullyGrown();
        currentGarden = new ArrayList<>();
        currentGrownGarden = new ArrayList<>();

        gardenWriter = new GardenWriter(STORE_GARDEN);
        gardenWriterGrown = new GardenWriter(STORE_GARDEN_GROWN);
        gardenReader = new GardenReader(STORE_GARDEN);
        gardenReaderGrown = new GardenReader(STORE_GARDEN_GROWN);

        initializeButtons();
        initializeLabels();
        initializePanels();
        initializeFrame();

        frame.setVisible(true);

    }

    // EFFECTS: initializes buttons
    private void initializeButtons() {
        start = new JButton();
        start.setBackground(new Color(126, 155, 113));
        start.setFont(new Font("Monospaced", Font.PLAIN, 60));
        start.setForeground(Color.black);
        start.setText("start");

        load = new JButton();
        load.setBackground(new Color(126, 155, 113));
        load.setFont(new Font("Monospaced", Font.PLAIN, 60));
        load.setForeground(Color.black);
        load.setText("load");

        pressStart();
        pressLoad();

    }

    // EFFECTS: initializes labels for initial garden start screen
    private void initializeLabels() {
        ImageIcon gardenLabel = new ImageIcon("data/mygardenlabel.png");
        title = new JLabel(gardenLabel);

        ImageIcon decoration = new ImageIcon("data/homepageart.png");
        art = new JLabel(decoration);

    }

    // EFFECTS: initializes panels, adding label and button components to them
    private void initializePanels() {
        planter = new JPanel(new GridLayout(8, 8, 0, 0));
        planter.setPreferredSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));
        grownPlanter = new JPanel(new GridLayout(8,8,0,0));
        grownPlanter.setPreferredSize(new Dimension(WINDOW_WIDTH / 2, WINDOW_HEIGHT));


        startPanel = new JPanel(new GridBagLayout());
        startPanel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        startPanel.add(start, c);

        loadPanel = new JPanel(new GridBagLayout());
        loadPanel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        c.anchor = GridBagConstraints.CENTER;
        loadPanel.add(load, c);

        titlePanel = new JPanel();
        titlePanel.setPreferredSize(new Dimension(700, 170));
        titlePanel.add(title);

        homePageArt = new JPanel();
        homePageArt.add(art);

    }

    // EFFECTS: initializes the main JFrame, adding the panels to it
    private void initializeFrame() {
        frame = new JFrame("My Garden");
        frame.setBackground(Color.white);
        frame.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(startPanel, BorderLayout.LINE_START);
        frame.add(loadPanel, BorderLayout.LINE_END);
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(homePageArt, BorderLayout.CENTER);

    }

    // EFFECTS: registers the pressing of the start button and opens a new JFrame
    private void pressStart() {
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                inGarden = new JFrame("In the Garden");
                inGarden.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
                inGarden.setVisible(true);

                createOptionsAndLabels(inGarden);

            }
        });
    }

    // EFFECTS: registers the pressing of the load button and opens saved file into a new JFrame
    private void pressLoad() {
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                inGarden = new JFrame("In the Garden");
                inGarden.setLayout(new BorderLayout());
                inGarden.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

                createOptionsAndLabels(inGarden);

                loadGarden();
                loadPlants(garden);
                loadGrownPlants(grownGarden);
                createOptionsAndLabels(inGarden);

                System.out.println(garden.listPlantTypes());
                System.out.println(grownGarden.listPlantTypes());

                inGarden.revalidate();
                inGarden.repaint();
                inGarden.pack();
                inGarden.setVisible(true);

            }

        });
    }


    // EFFECTS: loads the garden from file
    private void loadGarden() {
        try {
            garden = gardenReader.read();
            grownGarden = gardenReaderGrown.read();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load garden.");
        }
    }

    // MODIFIES: this
    // EFFECTS: looks at plants in the garden and loads them onto this
    private void loadPlants(Garden garden) {
        List<Plant> plants = garden.listPlants();
        for (Plant p : plants) {
            processPlant(p);
        }
    }

    // MODIFIES: this
    // EFFECTS: looks at plants in the garden and loads them onto this
    private void loadGrownPlants(Garden garden) {
        List<Plant> plants = grownGarden.listPlants();
        for (Plant p : plants) {
            processPlant(p);
            movePlant(p);
        }
    }


    // EFFECTS: sets the font and font colour of b
    private void setFontAndColour(JButton b) {
        b.setFont(new Font("Monospaced", Font.PLAIN, 20));
        b.setForeground(Color.black);
        b.setBackground(new Color(126, 155, 113));
    }


    // EFFECTS: creates the option buttons for the new JFrame
    private void createOptionsAndLabels(JFrame j) {
        plantButton = new JButton("plant");
        setFontAndColour(plantButton);
        pressPlant();

        growButton = new JButton("grow");
        setFontAndColour(growButton);
        pressGrow();

        weatherButton = new JButton("check the weather");
        setFontAndColour(weatherButton);
        pressWeather();

        potButton = new JButton("pot");
        setFontAndColour(potButton);
        pressPot(currentPlant);

        leaveButton = new JButton("leave");
        setFontAndColour(leaveButton);
        pressLeave();

        saveButton = new JButton("save");
        setFontAndColour(saveButton);
        pressSave();

        createLabels();

    }

    // EFFECTS: creates the labels for the grown and non-grown gardens
    private void createLabels() {
        gardenLabel = new JLabel("GARDEN");
        gardenLabel.setFont(new Font("Monospaced", Font.PLAIN, 30));
        grownLabel = new JLabel("GROWN PLANTS");
        grownLabel.setFont(new Font("Monospaced", Font.PLAIN, 30));

        setOptionPanel();
    }


    // EFFECTS: adds the option buttons and garden labels to this
    private void setOptionPanel() {
        optionPanel = new JPanel();
        GridLayout layout = new GridLayout(4, 2);
        optionPanel.setLayout(layout);

        addButtonsAndLabels();
        addPanels();

    }

    // EFFECTS: adds all buttons and needed labels to this panel
    private void addButtonsAndLabels() {
        optionPanel.add(plantButton);
        optionPanel.add(growButton);
        optionPanel.add(weatherButton);
        optionPanel.add(potButton);
        optionPanel.add(leaveButton);
        optionPanel.add(saveButton);
        optionPanel.add(gardenLabel);
        optionPanel.add(grownLabel);


    }

    // EFFECTS: adds the option panel to this
    private void addPanels() {
        inGarden.add(optionPanel, BorderLayout.NORTH);
        inGarden.add(planter, BorderLayout.LINE_START);
        inGarden.add(grownPlanter, BorderLayout.LINE_END);

    }

    /*
    this method references code from:
    https://www.youtube.com/watch?v=A-R9SrKQmGY&ab_channel=BrandanJones
     */
    // EFFECTS: prompts user to select plant upon pressing plant, returns an error if no colour or type is selected
    private void pressPlant() {
        plantButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String[] colourChoices = {"Blue", "Purple", "Pink"};

                String[] typeChoices = {"Hyacinth", "Tulip", "Gardenia", "Camellia"};

                String colour = (String) JOptionPane.showInputDialog(null,
                        "Choose your plant colour: ", "Colour",
                        JOptionPane.QUESTION_MESSAGE, null, colourChoices, "Select...");
                String type = (String) JOptionPane.showInputDialog(null,
                        "Choose your plant type:", "Type",
                        JOptionPane.QUESTION_MESSAGE, null, typeChoices, "Select....");
                if (colour != null && type != null) {
                    Plant plant = new Plant(colour, type);
                    processPlant(plant);
                    garden.plantSeed(plant);
                } else {
                    JOptionPane.showMessageDialog(null, "Error. Try again.");
                }

            }
        });

    }

    // EFFECTS: uses user input to create plant images
    private void processPlant(Plant p) {
        switch (p.getColour()) {
            case "Blue":
                plantBluePlants(p);
                break;
            case "Purple":
                plantPurplePlants(p);
                break;
            case "Pink":
                plantPinkPlants(p);
                break;
        }

        inGarden.revalidate();
        inGarden.repaint();
        inGarden.pack();
        inGarden.setVisible(true);

    }

    // EFFECTS: helper for planting blue plants
    private void plantBluePlants(Plant p) {
        switch (p.getType()) {
            case "Hyacinth":
                ImageIcon blueH = new ImageIcon("data/blue-hyacinth.png");
                JLabel blueHyacinth = new JLabel(blueH);
                addToGarden(blueHyacinth);
                break;
            case "Gardenia":
                ImageIcon blueG = new ImageIcon("data/blue-gardenia.png");
                JLabel blueGardenia = new JLabel(blueG);
                addToGarden(blueGardenia);
                break;
            case "Tulip":
                ImageIcon blueT = new ImageIcon("data/blue-tulip.png");
                JLabel blueTulip = new JLabel(blueT);
                addToGarden(blueTulip);
                break;
            case "Camellia":
                ImageIcon blueC = new ImageIcon("data/blue-camellia.png");
                JLabel blueCamellia = new JLabel(blueC);
                addToGarden(blueCamellia);
                break;
        }
    }

    // EFFECTS: helper for planting purple plants
    private void plantPurplePlants(Plant p) {
        switch (p.getType()) {
            case "Hyacinth":
                ImageIcon purpleH = new ImageIcon("data/purple-hyacinth.png");
                JLabel purpleHyacinth = new JLabel(purpleH);
                addToGarden(purpleHyacinth);
                break;
            case "Gardenia":
                ImageIcon purpleG = new ImageIcon("data/purple-gardenia.png");
                JLabel purpleGardenia = new JLabel(purpleG);
                addToGarden(purpleGardenia);
                break;
            case "Tulip":
                ImageIcon purpleT = new ImageIcon("data/purple-tulip.png");
                JLabel purpleTulip = new JLabel(purpleT);
                addToGarden(purpleTulip);
                break;
            case "Camellia":
                ImageIcon purpleC = new ImageIcon("data/purple-camellia.png");
                JLabel purpleCamellia = new JLabel(purpleC);
                addToGarden(purpleCamellia);
                break;
        }
    }

    // EFFECTS: helper for planting pink plants
    private void plantPinkPlants(Plant p) {
        switch (p.getType()) {
            case "Hyacinth":
                ImageIcon pinkH = new ImageIcon("data/pink-hyacinth.png");
                JLabel pinkHyacinth = new JLabel(pinkH);
                addToGarden(pinkHyacinth);
                break;
            case "Gardenia":
                ImageIcon pinkG = new ImageIcon("data/pink-gardenia.png");
                JLabel pinkGardenia = new JLabel(pinkG);
                addToGarden(pinkGardenia);
                break;
            case "Tulip":
                ImageIcon pinkT = new ImageIcon("data/pink-tulip.png");
                JLabel pinkTulip = new JLabel(pinkT);
                addToGarden(pinkTulip);
                break;
            case "Camellia":
                ImageIcon pinkC = new ImageIcon("data/pink-camellia.png");
                JLabel pinkCamellia = new JLabel(pinkC);
                addToGarden(pinkCamellia);
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds this j to the garden
    private void addToGarden(JLabel j) {
        planter.add(j);
        currentGarden.add(j);

    }


    // EFFECTS: exits the program
    private void pressLeave() {
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printLog();
                System.exit(0);

            }

        });
    }

    // EFFECTS: prints the EventLog to the console
    private void printLog() {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.getDate() + ": " + event.getDescription());
        }
    }

    // EFFECTS: saves garden to file
    private void pressSave() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: moves this to new JPanel
    private void pressGrow() {
        growButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentGarden.size() > 0) {
                    movePlant(thisPlant);
                    thisPlant.setFullyGrown();

                    garden.potSeed(thisPlant);
                    grownGarden.plantSeed(thisPlant);

                    System.out.println(grownGarden.listPlantTypes());

                    inGarden.revalidate();
                    inGarden.repaint();
                    inGarden.pack();
                    inGarden.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "No plants to grow!");
                }
            }
        });

    }

    // EFFECTS: moves plant from garden to grownGarden
    private void movePlant(Plant plant) {
        currentPlant = currentGarden.get(currentGarden.size() - 1);
        thisPlant = garden.getPlant(garden.gardenSize() - 1);

        currentGarden.remove(currentPlant);
        planter.remove(currentPlant);
        grownPlanter.add(currentPlant);

        currentGrownGarden.add(currentPlant);

        inGarden.revalidate();
        inGarden.repaint();
        inGarden.pack();
        inGarden.setVisible(true);
    }

    // EFFECTS: generates and displays a weather forecast
    private void pressWeather() {
        weatherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentWeather = garden.generateWeather();
                JOptionPane.showMessageDialog(null, currentWeather);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: removes plant from this
    private void pressPot(JLabel plant) {
        potButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentGrownGarden.size() > 0) {
                    currentPlant = currentGrownGarden.get(currentGrownGarden.size() - 1);
                    thisPlant = grownGarden.getPlant(grownGarden.gardenSize() - 1);
                    if (currentPlant.getParent() == grownPlanter) {
                        grownPlanter.remove(currentPlant);
                        currentGrownGarden.remove(currentPlant);
                        grownGarden.potSeed(thisPlant);
                        System.out.println(grownGarden.listPlantTypes());

                    } else {
                        JOptionPane.showMessageDialog(null, "Could not pot that plant!");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Nothing to pot!");
                }

                inGarden.revalidate();
                inGarden.repaint();
                inGarden.pack();
                inGarden.setVisible(true);
            }
        });

    }

    // EFFECTS: saves p to inputFile
    private void save() {
        try {
            gardenWriter.open();
            gardenWriterGrown.open();
            gardenWriter.write(garden);
            gardenWriterGrown.write(grownGarden);
            gardenWriter.close();
            gardenWriterGrown.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Could not save the garden.");
        }

    }


    // EFFECTS: initializes the garden GUI
    public static void main(String[] args) {
        new GardenUI();
    }

}
