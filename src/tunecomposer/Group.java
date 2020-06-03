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
 * @author ronanbyrne
 */
public class Group extends Rectangle implements Tune {

    public boolean isSelected;
    public boolean extend = false;
    private ArrayList<Tune> elementsOfGroup;

    public Group() {
        this.isSelected = false;
        elementsOfGroup = new ArrayList();
        this.setStroke(Color.DARKRED);
        this.setStrokeWidth(2);

    }

    public Group(double startX, double startY, double width, double height) {
        super(startX, startY, width, height);
        this.isSelected = false;
        elementsOfGroup = new ArrayList();
        this.setStroke(Color.DARKRED);
        this.setStrokeWidth(2);
    }
    
    public Group(double startX, double startY, double width, double height, ArrayList <Tune> elements) {
        super(startX, startY, width, height);
        this.isSelected = false;
        elementsOfGroup = elements;
        this.setStroke(Color.DARKRED);
        this.setStrokeWidth(2);
    }

    public void addElement(Tune t) {
        elementsOfGroup.add(t);
    }

    @Override
    public void select() {
        isSelected = true;
    }

    @Override
    public void unselect() {
        isSelected = false;
    }

    @Override
    public String toString() {
        return "Group Coords: (" + this.getX() + ", " + this.getY() + ") " + this.isSelected;
    }

    @Override
    public void handleOnPressed(MouseEvent event) {
        System.out.println("Group. handleOnPressed");
    }

    @Override
    public void setExtend(boolean b) {
        this.extend = b;
    }

    @Override
    public void configureTune() {
        setFill(Color.TRANSPARENT);
        getStrokeDashArray().addAll(3.0, 7.0, 3.0, 7.0);
        setStrokeWidth(2);
        if (this.isSelected) {
            setStroke(Color.RED);

        } else {
            setStroke(Color.DARKRED);
        }
    }

    @Override
    public void setSelected(boolean b) {
        isSelected = b;
    }

    @Override
    public ArrayList<Note> getNotesInTune() {
        ArrayList<Note> returnVal = new ArrayList();
        for (Tune t : this.elementsOfGroup) {
            returnVal.addAll(t.getNotesInTune());
        }
        return returnVal;
    }

    public ArrayList<Tune> getElementsInTune() {
        return elementsOfGroup;
    }

    @Override
    public void handleOnDragEvent(MouseEvent event, double deltaX, double deltaY) {
        this.setTranslateX(deltaX);
        this.setTranslateY(deltaY);
        for (Tune t : elementsOfGroup) {
            t.handleOnDragEvent(event, deltaX, deltaY);
        }
    }

    @Override
    public void handleOnResizeEvent(MouseEvent event, double clickAdjust) {
        this.setWidth(event.getX() + this.getWidth() + clickAdjust);
        for (Tune t : elementsOfGroup) {
            t.handleOnResizeEvent(event, clickAdjust);
        }
    }

    @Override
    public void setTranslate() {
        double x = this.getTranslateX();
        double y = this.getTranslateY();
        this.setX(this.getX() + x);
        this.setY(Math.floor((this.getY() + y) / 10) * 10 + 1);
        this.setTranslateX(0);
        this.setTranslateY(0);

        for (Tune t : elementsOfGroup) {
            t.setTranslate();
        }
    }

    @Override
    public Tune getCopy() {
        ArrayList <Tune> copyElements=new ArrayList();
        for(Tune t:this.getElementsInTune()){
            copyElements.add(t.getCopy());
            t.configureTune();
        }
        Tune newTune=new Group(this.getX(), this.getY(), this.getWidth(), this.getHeight(), copyElements);
        newTune.configureTune();
        return newTune;
    }

    @Override
    public void addToScreen(CompositionPaneController c) {
        for(Tune t: this.elementsOfGroup){
            t.addToScreen(c);
        }
        c.CompPane.getChildren().add(this);
    }

    @Override
    public void removeFromScreen(CompositionPaneController c) {
        c.CompPane.getChildren().remove(this);
        for(Tune t: this.elementsOfGroup){
            t.removeFromScreen(c);
        }
    }

}
