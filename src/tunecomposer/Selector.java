/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.paint.Color;

/**
 *
 * @author 12065
 */
public class Selector {

    //storing selected
    ArrayList<Tune> selected = new ArrayList<>();
    ArrayList<Tune> inGroup = new ArrayList<>();
    boolean isGroup = false;

    //add to selected
    public void add(Tune x) {
        x.setSelected(true);
        x.setStrokeWidth(2);
        x.setStroke(Color.RED);
        selected.add(x);
    }

    public void addToGroup(Tune x) {
        inGroup.add(x);
    }

    //remove from selected
    public void remove(Tune x) {
        x.setStroke(Color.BLACK);
        selected.remove(x);
    }

    //clear all selected...not sure if this works exactly how it should
    public void clear() {
        while (!selected.isEmpty()) {
            selected.iterator().next().unselect();
            remove(selected.iterator().next());
        }
        selected.clear();

    }

    //checks whether a Tune is selected
    public boolean contains(Tune x) {
        if (selected.contains(x)) {
            return true;
        } else {
            return false;
        }
    }

    //selects all the Tunes
    public ArrayList<Tune> selectAll(ArrayList<Tune> Tunes) {
        for (Tune i : Tunes) {
            Tunes.iterator().next().select();
            add(i);
        }
        return Tunes;
    }

    public Double getXBoundsMin(double deltaX) {
        ArrayList<Double> anchors = new ArrayList();
        for (Tune i : selected) {
            anchors.add(i.getX() + deltaX);
        }
        return Collections.min(anchors);
    }

    public Double getXBoundsMax(double deltaX) {
        ArrayList<Double> anchors = new ArrayList();
        for (Tune i : selected) {
            anchors.add(i.getX() + deltaX + i.getWidth());
        }
        return Collections.max(anchors);
    }

    public Double getYBoundsMin(double deltaY) {
        ArrayList<Double> anchors = new ArrayList();
        for (Tune i : selected) {
            anchors.add(i.getY() + deltaY);
        }
        return Collections.min(anchors);
    }

    public Double getYBoundsMax(double deltaY) {
        ArrayList<Double> anchors = new ArrayList();
        for (Tune i : selected) {
            anchors.add(i.getY() + deltaY + i.getHeight());
        }
        return Collections.max(anchors);
    }

    public boolean checkBoundsX(double minX, double maxX) {
        return minX > 0 && maxX < 2000;
    }

    public boolean checkBoundsY(double miny, double maxY) {
        return miny > 0 && maxY < 1280;
    }

}
