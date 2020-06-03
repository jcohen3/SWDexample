/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author cohenj3
 */
public class Note extends Rectangle implements Tune {

    //variables for the note, such as whether it's selected, or what instrument it is
    public boolean isSelected;
    public Color instrument;
    public boolean extend = false;

    /**
     * honestly don't really know why this exists but something else didn't work
     * unless I had it so
     */
    public Note() {
        super();
    }

    /**
     * note constructor...probably should be changed from taking color
     *
     * @param startX x-coord
     * @param startY y-coord
     * @param width width of note
     * @param height height of note (always 8)
     * @param instr Color of the instrument
     */
    public Note(double startX, double startY, double width, double height, Color instr) {
        super(startX, startY, width, 8);
        this.setStrokeWidth(2);
        this.isSelected = false;
        this.instrument = instr;
        this.setArcHeight(5 * Math.sqrt(2));
        this.setArcWidth(5 * Math.sqrt(2));
    }

    public Color getInstrument() {
        return this.instrument;
    }

    public void select() {
        this.isSelected = true;
    }

    /**
     * prints note
     *
     * @return note in string form
     */
    @Override
    public String toString() {
        return "Note Coords: (" + this.getX() + ", " + this.getY() + "), selected: " + this.isSelected + ".";
    }

    /**
     * configure notes visuals based on whether it's selected
     */
    public void configureTune() {
        setFill(instrument);
        setOpacity(.55);
        if (this.isSelected) {
            setStroke(Color.RED);
        } else {
            setStroke(Color.BLACK);
        }
    }

    /**
     * unselect note
     */
    public void unselect() {
        this.isSelected = false;
    }

    public void handleOnPressed(MouseEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setExtend(boolean b) {
        this.extend = b;
    }

    @Override
    public void setSelected(boolean b) {
        isSelected = b;
    }

    @Override
    public ArrayList<Note> getNotesInTune() {
        ArrayList<Note> arr = new ArrayList();
        arr.add(this);
        return arr;
    }

    public ArrayList<Tune> getElementsInTune() {
        return null;
    }

    @Override
    public void handleOnDragEvent(MouseEvent event, double deltaX, double deltaY) {
        // for all selected items, move them
        this.setTranslateX(deltaX);
        this.setTranslateY(deltaY);
    }

    @Override
    public void handleOnResizeEvent(MouseEvent event, double clickAdjust) {
        this.setWidth(event.getX() + this.getWidth() + clickAdjust);
    }

    @Override
    public void setTranslate() {
        //gets how far each Tune was moved
        double x = this.getTranslateX();
        double y = this.getTranslateY();

        //move X-coord to final drag location, move Y to fit to lines
        this.setX(this.getX() + x);
        this.setY(Math.floor((this.getY() + y) / 10) * 10 + 1);
        this.setTranslateX(0);
        this.setTranslateY(0);

    }

    @Override
    public Tune getCopy() {
        Tune t = new Note(this.getX(), this.getY(), this.getWidth(), this.getHeight(), this.getInstrument());
        t.configureTune();
        return t;
    }

    @Override
    public void addToScreen(CompositionPaneController c) {
        c.CompPane.getChildren().add(this);
    }

    @Override
    public void removeFromScreen(CompositionPaneController c) {
        c.CompPane.getChildren().remove(this);
    }
}
