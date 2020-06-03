/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

/**
 *
 * @author ronanbyrne
 */
public interface Tune {

    boolean extend = false;
    boolean isSelected = false;

    public void select();

    public String toString();

    public void unselect();

    public double getX();

    public double getY();

    public double getWidth();

    public double getHeight();

    public void setX(double d);

    public void setY(double d);

    public double getTranslateX();

    public double getTranslateY();

    public void setTranslateX(double d);

    public void setTranslateY(double d);

    public void setExtend(boolean b);

    public void setHeight(double d);

    public void setWidth(double d);

    public void configureTune();

    public void setSelected(boolean b);

    public void setStrokeWidth(double d);

    public void setStroke(Paint value);

    public ArrayList<Note> getNotesInTune();

    public ArrayList<Tune> getElementsInTune();

    public void setTranslate();

    public Tune getCopy();
    
    public void setOnMousePressed(EventHandler<? super MouseEvent> value);
    
    public void setOnMouseDragged(EventHandler<? super MouseEvent> value);
    
    public void setOnMouseReleased(EventHandler<? super MouseEvent> value);

    @FXML
    public void handleOnPressed(MouseEvent event);

    @FXML
    public void handleOnDragEvent(MouseEvent event, double deltaX, double deltaY);

    @FXML
    public void handleOnResizeEvent(MouseEvent event, double clickAdjust);

    public void addToScreen(CompositionPaneController c);
    
    public void removeFromScreen(CompositionPaneController c);
    
    

}
