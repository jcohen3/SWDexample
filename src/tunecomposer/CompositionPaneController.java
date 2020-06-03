/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tunecomposer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ronanbyrne
 */
public class CompositionPaneController {

    @FXML
    InstrumentPaneController instrumentPaneController;
    //pane where all the Tunes are
    @FXML
    protected AnchorPane CompPane;

    //lines in separate pane, clicking on lines was no good
    @FXML
    private AnchorPane LinePane;

    @FXML
    private Rectangle highlight;

    @FXML
    protected Line verticalLine = new Line(0, 0, 0, 3000);

    protected PathTransition pathT = new PathTransition();

    private boolean isScreenDragged;
    private double maxXCoordinate = 0;
    public ArrayList<Integer> Bounds = new ArrayList(Arrays.asList(0, 0, 2000, 1280));

    //new selecting object
    protected Selector selector = new Selector();

    /**
     * Represents the number of pitch steps for do, re, mi, fa, so, la, ti, do.
     * Source: https://en.wikipedia.org/wiki/Solfège
     */
    static class Delta {

        double mouseX, mouseY, screenX, screenY, extendX;
    };

    protected Delta dragContext = new Delta();

    /**
     * One midi player is used throughout, so we can stop a scale that is
     * currently playing.
     */
    protected MidiPlayer player = new MidiPlayer(100, 60);
    public ArrayList<Tune> allTunes = new ArrayList();

    private boolean flag = false;

    protected Stack<ArrayList<Tune>> stack = new Stack<>();
    protected Stack<ArrayList<Tune>> redoStack = new Stack<>();

    //whether undo/redo can be performed
    protected BooleanProperty canUndo = new SimpleBooleanProperty(false);
    protected BooleanProperty canRedo = new SimpleBooleanProperty(false);
    
    private boolean isFirstDragEvent = true;

    /**
     * adds vertical line that represents time handles its movement and removal
     */
    protected void addVerticalLine() {
        getMaxX(allTunes);
        verticalLine.setStroke(Color.RED);
        if (!allTunes.isEmpty()) {
            CompPane.getChildren().add(verticalLine);
        }
        Path path = new Path();
        path.getElements().add(new MoveTo(0, 0));
        path.getElements().add(new LineTo(maxXCoordinate + player.getResolution() - 100, 0));
        pathT.setInterpolator(Interpolator.LINEAR);
        pathT.setDuration(Duration.seconds((maxXCoordinate + player.getResolution() - 100) / player.getResolution()));
        pathT.setPath(path);
        pathT.setNode(verticalLine);
        pathT.setOnFinished((e) -> deleteLine());
        pathT.play();
    }

    /**
     * stops the vertical line animation and removes the line from the screen.
     */
    protected void deleteLine() {
        pathT.stop();
        CompPane.getChildren().remove(verticalLine);
    }

    /**
     * handles when the board is clicked
     *
     * @param mouseClick a mouse click event
     */
    @FXML
    protected void handleScreenPressed(MouseEvent mouseClick) {
        deleteLine();
        player.stop();
        player.clear();
        isScreenDragged = false;
        highlight = new Rectangle(mouseClick.getX(), mouseClick.getY(), 0, 0);
        dragContext.screenX = mouseClick.getX();
        dragContext.screenY = mouseClick.getY();
        CompPane.getChildren().add(highlight);
    }

    @FXML
    protected void handleScreenDragged(MouseEvent event) {
        player.stop();
        player.clear();
        deleteLine();
        highlight.setFill(Color.TRANSPARENT);
        highlight.setStroke(Color.GREEN);
        highlight.setOpacity(.9);
        isScreenDragged = true;
        if (event.getX() > dragContext.screenX) {
            highlight.setWidth(event.getX() - dragContext.screenX);
        } else {
            highlight.setX(event.getX());
            highlight.setWidth(dragContext.screenX - event.getX());
        }
        if (event.getY() > dragContext.screenY) {
            highlight.setHeight(event.getY() - dragContext.screenY);
        } else {
            highlight.setHeight(dragContext.screenY - event.getY());
            highlight.setY(event.getY());
        }
    }

    @FXML
    protected void handleScreenReleased(MouseEvent event) {
        if(!isScreenDragged){
            logAction();
        }
        selector.clear();
        checkIfWithinDragBox();
        CompPane.getChildren().remove(highlight);
        double xCoordinate = event.getX();
        double yCoordinate = event.getY();
        if (!isScreenDragged) {
            Node target = (Node) event.getTarget();
            if (!(target instanceof Tune)) {
                Note newNote = new Note(xCoordinate, fitToLines(yCoordinate), 100, 10, instrumentPaneController.getInstrumentColor());

                //add handlers for all new Tunes added
                newNote.setOnMousePressed(TuneOnMousePressed);
                newNote.setOnMouseDragged(TuneOnDragged);
                newNote.setOnMouseReleased(TuneOnReleased);

                //figuring out whether to only select added Tune or add to currently selected
                if (event.isControlDown()) {
                    selector.add(newNote);
                } else {
                    selector.clear();
                    selector.add(newNote);
                }
                //adding added Note to allTunes
                allTunes.add(newNote);
                newNote.configureTune();
                CompPane.getChildren().add(newNote);
            }
        }
    }

    /**
     * adds horizontal lines to represent different pitches.
     */
    @FXML
    protected void addLines() {
        for (int i = 0; i < 1280; i += 10) {
            Line line1 = new Line(0, i, 2000, i);
            line1.setStroke(Color.GRAY);
            LinePane.getChildren().add(line1);
        }
    }

    protected void checkIfWithinDragBox() {
        for (Tune i : allTunes) {
            if ((i.getX() > highlight.getX() && i.getX() < (highlight.getX() + highlight.getWidth()))
                    && i.getY() > highlight.getY() && i.getY() < (highlight.getY() + highlight.getHeight())) {
                selector.add(i);
            }
        }
    }

    /**
     * finding max x coordinate...probably will need to be changed after doing
     * Tune extending?
     *
     * @param Tunes
     */
    protected double getMaxX(ArrayList<Tune> tunes) {
        maxXCoordinate = 0;
        for (Tune i : tunes) {
            if ((i.getX() + i.getWidth()) > maxXCoordinate) {
                maxXCoordinate = i.getX() + i.getWidth();
            }
        }
        return maxXCoordinate;
    }

    protected double getMinX(ArrayList<Tune> t) {
        double minX = 9999999;
        for (Tune i : t) {
            if (minX > i.getX()) {
                minX = i.getX();
            }
        }
        return minX;
    }

    protected double getMaxY(ArrayList<Tune> t) {
        double maxY = 0;
        for (Tune i : t) {
            if (maxY < (i.getY() + i.getHeight())) {
                maxY = i.getY() + i.getHeight();
            }
        }
        return maxY;
    }

    protected double getMinY(ArrayList<Tune> t) {
        double minY = 9999999;
        for (Tune i : t) {
            if (minY > i.getY()) {
                minY = i.getY();
            }
        }
        return minY;
    }

    /**
     * handler for when Tune is pressed
     */
    EventHandler<MouseEvent> TuneOnMousePressed = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            deleteLine();
            Tune clicked = (Tune) event.getSource();

            //figuring out where the mouse was initially clicked
            dragContext.extendX = event.getX();
            dragContext.mouseX = clicked.getTranslateX() - event.getSceneX();
            dragContext.mouseY = clicked.getTranslateY() - event.getSceneY();

            //ctrl held down
            if (event.isControlDown()) {
                selector.add(clicked);
            }

            //if Tune is not selected, deselect all and select clicked Tune
            if (!selector.contains(clicked)) {
                selector.clear();
                selector.add(clicked);
            }
            event.consume();
        }
    };

    /**
     * handler for when Tune is being dragged
     */
    EventHandler<MouseEvent> TuneOnDragged = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(isFirstDragEvent){
                logAction();
            }
            isFirstDragEvent=false;
            Tune clicked = (Tune) event.getSource();
            double deltaX = event.getSceneX() + dragContext.mouseX;
            double deltaY = event.getSceneY() + dragContext.mouseY;

            double minX = selector.getXBoundsMin(deltaX);
            double maxX = selector.getXBoundsMax(deltaX);
            double minY = selector.getYBoundsMin(deltaY);
            double maxY = selector.getYBoundsMax(deltaY);

            if (selector.checkBoundsX(minX, maxX) && selector.checkBoundsY(minY, maxY)) {
                if (((event.getX() > (clicked.getX() + (clicked.getWidth() - 10))) && (event.getX() < (clicked.getX() + clicked.getWidth() + 8))) || flag) {
                    double minWidth = getMinWidthSelected();
                    if (minWidth > 10) {
                        double clickedAdjust = 0 - clicked.getX() - clicked.getWidth();
                        for (Tune t : selector.selected) {
                            t.handleOnResizeEvent(event, clickedAdjust);
                        }
                    }
                    flag = true;
                } else {
                    if (flag == false) {
                        for (Tune t : selector.selected) {
                            t.handleOnDragEvent(event, deltaX, deltaY);
                        }
                    }
                }
            }
        }
    };

    private double getMinWidthSelected() {
        double minWidth = 999999999;
        for (Tune t : selector.selected) {
            if (minWidth > t.getWidth()) {
                minWidth = t.getWidth();
            }
        }
        return minWidth;
    }

    /**
     * handler for when Tune is released (fitting to lines)
     */
    EventHandler<MouseEvent> TuneOnReleased = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            for (Tune i : selector.selected) {
                //gets how far each Tune was moved
                i.setTranslate();
                i.setExtend(false);
            }
            event.consume();
            flag = false;
            isFirstDragEvent=true;
        }
    };

    /**
     *
     * @param yPosition some y-coordinate input
     * @return y-coordinate but adjusted to fit
     */
    public double fitToLines(double yPosition) {
        return Math.floor(yPosition / 10) * 10 + 1;
    }

    /**
     * logging actions - consists of pushing current board to the undo stack,
     * and updating booleans
     */
    public void logAction() {
        ArrayList<Tune> arrayListClone = new ArrayList<>();
        for(Tune t:allTunes){
            arrayListClone.add(t.getCopy());
        }
        stack.push(arrayListClone);
        redoStack.clear();
        updateGestures();
    }
    
    public void logActionForPossibleRedo(){
        ArrayList<Tune> arrayListClone = new ArrayList<>();
        for(Tune t:allTunes){
            arrayListClone.add(t.getCopy());
        }
        redoStack.push(arrayListClone);
        updateGestures();
    }

    /**
     * updates whether Undo/Redo can be performed - undo can be performed if
     * stack is not empty, redo can be performed if redoStack is not empty
     */
    public void updateGestures() {
        canUndo.set(!stack.isEmpty());
        canRedo.set(!redoStack.isEmpty());
    }
}
