/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ronanbyrne
 */
public class InstrumentPaneController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private VBox buttonPane;

    //togglegroup for buttons
    @FXML
    private ToggleGroup group;

    private static final HashMap<String, Color> instruments = new HashMap<String, Color>() {
        {
            put("Piano", Color.PINK); //1
            put("Harpsichord", Color.BLACK); //7
            put("Marimba", Color.GREEN); //13
            put("Church Organ", Color.BLUE); //20
            put("Accordion", Color.PURPLE); //22
            put("Guitar", Color.ORANGE); //26
            put("Violin", Color.DARKGRAY); //41
            put("French Horn", Color.BROWN); //61
        }
    };

    private static final HashMap<Color, Integer> channelNums = new HashMap<Color, Integer>() {
        {
            put(Color.PINK, 0); //1
            put(Color.BLACK, 1); //7
            put(Color.GREEN, 2); //13
            put(Color.BLUE, 3); //20
            put(Color.PURPLE, 4); //22
            put(Color.ORANGE, 5); //26
            put(Color.DARKGRAY, 6); //41
            put(Color.BROWN, 7);
        }
    };

    /**
     * gets the current instrument from radiobutton menu and returns its
     * corresponding color
     *
     * @return Text of instrument
     */
    protected String getInstrument() {
        RadioButton s = (RadioButton) group.getSelectedToggle();
        String instName = s.getText();
        return instName;
    }

    protected Integer getChannelNums(Note i) {
        return channelNums.get(i.instrument);
    }

    public Color getInstrumentColor() {
        return instruments.get(getInstrument());
    }
}
