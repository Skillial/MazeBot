package csintsy.mco1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.scene.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MazeBotApplication extends Application {
    private int mazeSize = 1;
    private ArrayList<String> maze = new ArrayList<>();
    private MazeSolver mazeSolver;
    private FXMLLoader fxmlLoader;
    private ApplicationController controller;

    public MazeSolver getMazeSolver() {return mazeSolver;}
    public void initializeSolver() {
         mazeSolver = new MazeSolver();
         maze = mazeSolver.getMaze();
    }
    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/csintsy.mco1/Main.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        initializeSolver();

        Scene scene = new Scene(root, Color.rgb(46, 52, 64));
        stage.setTitle("MazeBot");
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds(); // Get size of the primary screen
        stage.setMinWidth(960);
        stage.setMinHeight(937);
        stage.setX((screenSize.getWidth() - stage.getMinWidth()) / 2);     // Set window X position to middle of primary screen
        stage.setY((screenSize.getHeight() - stage.getMinHeight()) / 2);   // Set window Y position to middle of primary screen
        //stage.setResizable(false);
        controller.setMazeBotApp(this);
        controller.setSize(maze.size());
        controller.generateMaze(maze);
        controller.setSolvable(mazeSolver.isSolvable());
        stage.getIcons().add(new Image(MazeBotApplication.class.getResourceAsStream("/csintsy.mco1/logo.png")));
        stage.setScene(scene);
        stage.show();                                                   // Show window
    }
    public static void main(String[] args) {launch();}
}

class pairs {
    int first;
    int second;

    public pairs(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }
}

class MazeSolver {
    boolean flag = true;
    int count = 0;
    int mazeSize = 0;
    ArrayList<pairs> exploreNodes = new ArrayList<>();
    private ArrayList<Node> nodes;
    private ArrayList<String> maze;
    private ArrayList<String> initialMaze;

    private boolean solvable = false;
    public ArrayList<String> getInitialMaze() {return initialMaze;}
    public ArrayList<String> getMaze() {
        ArrayList<String> path = new ArrayList<>();
        try {
            File maze = new File("maze.txt");
            Scanner filesc = new Scanner(maze);
            mazeSize = filesc.nextInt();
            filesc.nextLine();
            while (filesc.hasNextLine())
                path.add(filesc.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        initialMaze = (ArrayList<String>) path.clone();
        return path;
    }
    public boolean isSolvable() {return solvable;}
    public MazeSolver() {
        nodes = new ArrayList<>();
        maze = (ArrayList<String>) getMaze().clone();
        createNode(mazeSize, maze, nodes);
        int start = getStartNode(nodes);
        DFS(nodes.get(start));
    }

    public  void createNode(int size, ArrayList<String> path, ArrayList<Node> nodes) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (i == 0) {
                    if (j == 0)
                        nodes.add(new Node(null, null, null, null, path.get(i).charAt(j), false, false, i, j));
                    else
                        nodes.add(new Node(null, null, nodes.get(j - 1), null, path.get(i).charAt(j), false, false, i, j));
                } else {
                    if (j == 0)
                        nodes.add(new Node(nodes.get(i * size - size), null, null, null, path.get(i).charAt(j), false, false, i, j));
                    else
                        nodes.add(new Node(nodes.get(i * size + j - size), null, nodes.get(i * size + j - 1), null, path.get(i).charAt(j), false, false, i, j));
                }
            }
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (j != size - 1)
                    nodes.get(i * size + j).setRight(nodes.get(i * size + j + 1));
                if (i != size - 1)
                    nodes.get(i * size + j).setBottom(nodes.get(i * size + j + size));
            }
    }

    public  int getStartNode(ArrayList<Node> nodes) {
        for (int i = 0; i < nodes.size(); i++)
            if (nodes.get(i).getSymbol() == 'S')
                return i;
        return -1;
    }

    private boolean isValidNode(Node toCheck) {return toCheck != null && toCheck.getSymbol() != '#' && !toCheck.isExplored();}
    public void DFS(Node check) {
        check.setExplored(true);
        if (check.getSymbol() != 'G' && flag) {
            check.setCorrect(true);
            if (isValidNode(check.getRight()) && flag) {
                count += 1;
                exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
                DFS(check.getRight());
            }
            if (isValidNode(check.getBottom()) && flag) {
                count += 1;
                exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
                DFS(check.getBottom());
            }
            if (isValidNode(check.getLeft()) && flag) {
                count += 1;
                exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
                DFS(check.getLeft());
            }
            if (isValidNode(check.getTop()) && flag) {
                count += 1;
                exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
                DFS(check.getTop());
            }
            if (flag) {
                count += 1;
                exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
                check.setCorrect(false);
            }
        } else {
            count += 1;
            exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
            solvable = true;
            flag = false;
            check.setCorrect(true);
        }
    }

    public ArrayList<String> getFinalPath() {
        ArrayList<String> outputPath = new ArrayList<>();
        StringBuilder currentLayer = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            Node currentNode = nodes.get(i);
            if (currentNode.getSymbol() == 'G' || currentNode.getSymbol() == 'S')
                currentLayer.append(currentNode.getSymbol());
            else if (solvable && currentNode.isCorrect())
                currentLayer.append("C");
            else if (!solvable && currentNode.isExplored())
                currentLayer.append("C");
            else
                currentLayer.append(currentNode.getSymbol());
            if ((i - (mazeSize - 1)) % mazeSize == 0) {
                outputPath.add(currentLayer.toString());
                currentLayer = new StringBuilder();
            }
        }
        return outputPath;
    }
    public  ArrayList<String> getExplored() {
        ArrayList<String> outputPath = new ArrayList<>();
        StringBuilder currentLayer = new StringBuilder();
        for (int i = 0; i < nodes.size(); i++) {
            Node currentNode = nodes.get(i);
            if (currentNode.getSymbol() == 'G' || currentNode.getSymbol() == 'S')
                currentLayer.append(currentNode.getSymbol());
            else if (currentNode.isCorrect() || currentNode.isExplored())
                currentLayer.append("C");
            else
                currentLayer.append(currentNode.getSymbol());
            if ((i - (mazeSize - 1)) % mazeSize == 0) {
                outputPath.add(currentLayer.toString());
                currentLayer = new StringBuilder();
            }
        }
        return outputPath;
    }

    public  ArrayList<ArrayList<String>> getStates() {
        ArrayList<ArrayList<String>> states = new ArrayList<>();
        for (int k = 0; k < count; k++) {
            ArrayList<String> currentState = new ArrayList<>();
            StringBuilder currentLayer = new StringBuilder();
            for (int i = 0; i < nodes.size(); i++) {
                Node currentNode = nodes.get(i);
                if(exploreNodes.get(k).getFirst() * mazeSize + exploreNodes.get(k).getSecond() == i)
                    currentLayer.append("H");
                else
                    currentLayer.append(currentNode.getSymbol());
                if ((i - (mazeSize - 1)) % mazeSize == 0) {
                    currentState.add(currentLayer.toString());
                    currentLayer = new StringBuilder();
                }
            }
            states.add(currentState);
        }
        return states;
    }
}