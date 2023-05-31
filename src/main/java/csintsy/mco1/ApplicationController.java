package csintsy.mco1;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    private MazeBotApplication mazeBotApp;
    public void setMazeBotApp(MazeBotApplication mazeBotApp) {this.mazeBotApp = mazeBotApp;}

    @FXML
    private CheckBox showStatesCheckBox;
    private boolean showStates = false;
    @FXML
    private Slider speedSlider;
    private int speed = 1;
    @FXML
    private Text exploredCount;
    @FXML
    private TilePane mazeTile;
    @FXML
    private Text pathStatus;
    private ArrayList<ArrayList<String>> states;
    private int mazeSize;
    private int currentState = 0;
    private boolean solving = false;
    private boolean solvable;
    public void setSolvable(boolean value) {solvable = value;}

    public void updateMaze() {
        if (currentState == states.size() + 1) {
            if (!solvable) pathStatus.setText("No path found!");
            else pathStatus.setText("Goal found.");
            generateMaze(mazeBotApp.getMazeSolver().getExplored());
        }
        else if (solvable && currentState == states.size() + 2) {
            generateMaze(mazeBotApp.getMazeSolver().getFinalPath());
            pathStatus.setText("Path created!");
        }
        else if (currentState == 0) {
            pathStatus.setText("");
            generateMaze(mazeBotApp.getMazeSolver().getInitialMaze());
        }
        else if (currentState == -1)
            currentState = 0;
        else {
            generateMaze(states.get(currentState - 1));
            pathStatus.setText("");
        }
        updateExplored(currentState);

    }
    public void nextState(ActionEvent e) {
        if (states == null)
            states = mazeBotApp.getMazeSolver().getStates();

        int buffer = 0;
        if (solvable) buffer = 1;
        if (currentState > states.size() + buffer) return;
        currentState ++;
        updateMaze();
        if (e != null) return;
        if (solving && showStates && currentState <= states.size() + buffer) {
            Task<Void> timer = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Thread.sleep(1000 / speed);
                    } catch (InterruptedException e) {e.printStackTrace();}
                    return null;
                }
            };
            timer.setOnSucceeded(event -> nextState(null));
            new Thread(timer).start();
        }
    }
    public void previousState(ActionEvent e) {
        if (states == null)
            states = mazeBotApp.getMazeSolver().getStates();
        if (currentState == -1)
            return;
        currentState --;
        updateMaze();
    }

    public void solve(ActionEvent e) {
        solving = !solving;
        if (states == null)
            states = mazeBotApp.getMazeSolver().getStates();
        if (!showStates) {
            updateExplored(states.size());
            currentState = states.size() + 1;
            if (solvable) currentState ++;
            updateMaze();
            return;
        }
        nextState(null);


    }
    public void setShowStates() {
        showStates = showStatesCheckBox.isSelected();
    }
    public void setSpeed(ActionEvent e) {speed = (int) speedSlider.getValue();}
    private float calculateSize(int mazeSize) {return (float) (750.0 / mazeSize);}


    public void generateMaze(ArrayList<String> maze){
        mazeTile.getChildren().clear();
        mazeTile.setPrefColumns(mazeSize);
        mazeTile.setPrefRows(mazeSize);
        float tileSize = calculateSize(mazeSize);
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                char mazeNode = maze.get(i).charAt(j);
                String color = "#434C5E";
                switch (mazeNode) {
                    case 'S' -> color = "#A3BE8C";
                    case 'G' -> color = "#BF616A";
                    case 'H' -> color = "#B48EAD";
                    case '.' -> color = "#ECEFF4";
                    case 'C' -> color = "#EBCB8B";
                }
                Pane pane = new Pane();
                pane.setPrefSize(tileSize, tileSize);
                pane.setMinSize(tileSize, tileSize);
                pane.setMaxSize(tileSize, tileSize);
                BackgroundFill backgroundColor = new BackgroundFill(
                        Color.valueOf(color),
                        new CornerRadii(0),
                        new Insets(0)
                );
                pane.setBackground(new Background(backgroundColor));
                mazeTile.getChildren().add(pane);
            }
        }
    }
    public void updateExplored(int x) {
        if (x == -1 || x > states.size()) return;
        exploredCount.setText(String.valueOf(x));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) throws NullPointerException {
        try {
            speedSlider.valueProperty().addListener((observableValue, number, t1) -> speed = (int) speedSlider.getValue());
        }catch (NullPointerException n) {n.printStackTrace();}
    }
    public void setSize(int size) {mazeSize = size;}
}

