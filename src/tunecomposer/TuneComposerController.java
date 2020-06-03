/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javax.sound.midi.InvalidMidiDataException;

/**
 * Ronan Byrne, Jesse Cohen, Tristan Chung, Josh Shin; Ronan discussed some code
 * ideas with Connor Young when working on the project
 *
 * @author byrnerp
 */
public class TuneComposerController implements Initializable {

    @FXML
    CompositionPaneController compositionPaneController;

    @FXML
    MenuItem redo;

    @FXML
    MenuItem undo;

    private ArrayList<Double> OriginalWidth = new ArrayList();
    /**
     * Play notes at maximum volume.
     */
    private static final int VOLUME = 127;

    /**
     * Constructs a new ScalePlayer application.
     */
    public TuneComposerController() {
//        compositionPaneController.player = new MidiPlayer(100, 60);
    }

    public TuneComposerController(int res, int bpm) {
//        this.player = new MidiPlayer(res, bpm);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        undo.disableProperty().bind(compositionPaneController.canUndo.not());
        redo.disableProperty().bind(compositionPaneController.canRedo.not());
    }

    public void handleUndoAction(ActionEvent event) {
        //Remove Everything from the screen
        for (Tune t : compositionPaneController.allTunes) {
            t.removeFromScreen(compositionPaneController);
        }

        ArrayList<Tune> poppedAction = compositionPaneController.stack.pop();
        
        compositionPaneController.logActionForPossibleRedo();
        System.out.println(compositionPaneController.redoStack);
        
        compositionPaneController.allTunes=poppedAction;
        
        for (Tune t : compositionPaneController.allTunes) {
            t.addToScreen(compositionPaneController);
            
            t.setOnMousePressed(compositionPaneController.TuneOnMousePressed);
            t.setOnMouseDragged(compositionPaneController.TuneOnDragged);
            t.setOnMouseReleased(compositionPaneController.TuneOnReleased);
            
            t.configureTune();
        }
        compositionPaneController.updateGestures();
    }
    
    public void handleRedoAction(ActionEvent event) {
        for(Tune t: compositionPaneController.allTunes){
            t.removeFromScreen(compositionPaneController);
        }
        
        ArrayList<Tune> poppedAction = compositionPaneController.redoStack.pop();
        
        compositionPaneController.logAction();
        
        compositionPaneController.allTunes = poppedAction;
        
        for (Tune t: compositionPaneController.allTunes){
            t.addToScreen(compositionPaneController);
            
            t.setOnMousePressed(compositionPaneController.TuneOnMousePressed);
            t.setOnMouseDragged(compositionPaneController.TuneOnDragged);
            t.setOnMouseReleased(compositionPaneController.TuneOnReleased);
            
            t.configureTune();
        }
        compositionPaneController.updateGestures();
    }

    public void handleGroupAction(ActionEvent event) {
        compositionPaneController.logAction();
        double xCoord = compositionPaneController.getMinX(compositionPaneController.selector.selected);
        double yCoord = compositionPaneController.getMinY(compositionPaneController.selector.selected);
        double width = compositionPaneController.getMaxX(compositionPaneController.selector.selected) - xCoord;
        double height = compositionPaneController.getMaxY(compositionPaneController.selector.selected) - yCoord;
        Group newGroup = new Group(xCoord, yCoord, width, height);

        newGroup.setOnMousePressed(compositionPaneController.TuneOnMousePressed);
        newGroup.setOnMouseDragged(compositionPaneController.TuneOnDragged);
        newGroup.setOnMouseReleased(compositionPaneController.TuneOnReleased);

        for (Tune i : compositionPaneController.selector.selected) {
            newGroup.addElement(i);
            compositionPaneController.allTunes.remove(i);
            //compositionPaneController.CompPane.getChildren().remove(i);
        }
        compositionPaneController.selector.clear(); //unselecting selected notes
        compositionPaneController.allTunes.add(newGroup);   //adds group to allTunes
        compositionPaneController.selector.add(newGroup);   //selects group

        newGroup.configureTune();
        compositionPaneController.CompPane.getChildren().add(newGroup); //adds the group to the screen
        //addGroupElementsToScreen(newGroup);
    }



    public void removeGroupElementsFromScreen(Tune i) {
        for (Tune t : i.getElementsInTune()) {
            if (t instanceof Note) {
                compositionPaneController.CompPane.getChildren().remove((Note) t);
            } else {
                compositionPaneController.CompPane.getChildren().remove((Group) t);
                removeGroupElementsFromScreen(t);
            }
        }
    }

    public void handleUnGroupAction(ActionEvent event) {
        compositionPaneController.logAction(); // CHECK IF THIS WORKS!!!!!!
        if (compositionPaneController.selector.selected.size() > 1) {
            System.out.println("Select only one group");
        } else if (compositionPaneController.selector.selected.get(0) instanceof Note) {
            System.out.println("Please select a group");
        } else {
            //Remove selected group
            Group selectedGroup = (Group) compositionPaneController.selector.selected.get(0);
            compositionPaneController.selector.selected.remove(selectedGroup);

            //Remove dotted lines
            compositionPaneController.CompPane.getChildren().remove(selectedGroup);
            compositionPaneController.allTunes.remove(selectedGroup);

            for (Tune i : selectedGroup.getElementsInTune()) {
                compositionPaneController.allTunes.add(i);
                compositionPaneController.selector.selected.add(i);
                i.setSelected(true);
                i.configureTune();
            }
        }
    }

    /**
     * When the user clicks the "Play scale" button, show a dialog to get the
     * starting note and then play the scale.
     *
     * @param event the button click event
     */
    @FXML
    protected void handlePlayScaleButtonAction(ActionEvent event) throws InterruptedException, InvalidMidiDataException {
        compositionPaneController.deleteLine();
        compositionPaneController.player.stop();
        compositionPaneController.player.setUpPlayer();
        compositionPaneController.player.clear();
        for (Tune i : compositionPaneController.allTunes) {
            for (Note j : i.getNotesInTune()) {
                compositionPaneController.player.addNote(127 - ((int) j.getY() / 10), VOLUME, (int) j.getX(), (int) j.getWidth(), compositionPaneController.instrumentPaneController.getChannelNums(j), 0);
            }
        }
        compositionPaneController.player.play();
        compositionPaneController.addVerticalLine();
    }

    /**
     * When the user clicks the "Stop playing" button, stop playing the scale.
     *
     * @param event the button click event
     */
    @FXML
    protected void handleStopPlayingButtonAction(ActionEvent event) {
        compositionPaneController.player.stop();
        compositionPaneController.pathT.stop();
        compositionPaneController.CompPane.getChildren().remove(compositionPaneController.verticalLine);
    }

    /**
     * When the user clicks the "Exit" menu item, exit the program.
     *
     * @param event the menu selection event
     */
    @FXML
    protected void handleExitMenuItemAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * handles selecting all notes
     */
    @FXML
    protected void handleSelectAllAction() {
        compositionPaneController.allTunes = compositionPaneController.selector.selectAll(compositionPaneController.allTunes);
    }

    
    //FIX
    /**
     * special case for deleting all notes, in edit menu
     */
    @FXML
    protected void handleDeleteSelectedAction() {
        compositionPaneController.logAction();
        while (!compositionPaneController.selector.selected.isEmpty()) {
            Tune i = compositionPaneController.selector.selected.get(0);
            if (i instanceof Group) {
                ArrayList<Tune> arr = i.getElementsInTune();
                for (int k = 0; k < arr.size(); k++) {
                    compositionPaneController.selector.selected.add(arr.get(k));
                }
            }
            compositionPaneController.allTunes.remove(i);
            compositionPaneController.selector.selected.remove(i);
            compositionPaneController.CompPane.getChildren().remove(i);
        }
        compositionPaneController.selector.clear();
    }
}
