import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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

public class app {
    static boolean flag = true;
    static int count = 0;
    static ArrayList<pairs> exploreNodes = new ArrayList<>();

    public static void main(String[] args) {
        ArrayList<String> path = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();
        int size = 0;
        try {
            File maze = new File("maze.txt");
            Scanner filesc = new Scanner(maze);
            size = filesc.nextInt();
            filesc.nextLine();
            while (filesc.hasNextLine())
                path.add(filesc.nextLine());
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        print(size, path);
        createNode(size, path, nodes);
        int start = getStartNode(nodes);
        DFS(nodes.get(start));
        if (!flag) {
            System.out.println("\nFinal path: ");
            printAns(size, nodes);
            System.out.println("\nExplored path: ");
            printAns(size, nodes);
            System.out.println("Number of states: " + count);
            printStates(size, nodes);
        } else {
            System.out.println("\nThere is no path!");
            System.out.println("Number of states: " + (count - 1));
        }
//        for (pairs exploreNode : exploreNodes) {
//            System.out.println(exploreNode.getFirst() + " " + exploreNode.getSecond());
//        }
    }

    public static void print(int size, ArrayList<String> path) {
        System.out.println(size);
        for (int i = 0; i < size; i++)
            System.out.println(path.get(i));
    }

    public static void createNode(int size, ArrayList<String> path, ArrayList<Node> nodes) {
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
//        for(int i=0;i< nodes.size();i++)
//        {
//            if(nodes.get(i).getTop()==null)
//                System.out.println(nodes.get(i).getTop());
//            else
//                System.out.println(nodes.get(i).getBottom().getSymbol());
//        }
    }

    public static int getStartNode(ArrayList<Node> nodes) {
        for (int i = 0; i < nodes.size(); i++)
            if (nodes.get(i).getSymbol() == 'S')
                return i;
        return -1;
    }

    public static void DFS(Node check) {

        if (!check.isExplored()) {
            count += 1;
            exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
            check.setExplored(true);
            if (check.getSymbol() != 'G' && flag) {
                check.setCorrect(true);
                if (check.getRight() != null && check.getRight().getSymbol() != '#' && flag)
                    DFS(check.getRight());
                if (check.getBottom() != null && check.getBottom().getSymbol() != '#' && flag)
                    DFS(check.getBottom());
                if (check.getLeft() != null && check.getLeft().getSymbol() != '#' && flag)
                    DFS(check.getLeft());
                if (check.getTop() != null && check.getTop().getSymbol() != '#' && flag)
                    DFS(check.getTop());
                if (flag) {
                    count += 1;
                    exploreNodes.add(new pairs(check.getFirst(), check.getSecond()));
                    check.setCorrect(false);
                }
            } else {
                flag = false;
                check.setCorrect(true);
            }
        }
    }

    public static void printAns(int size, ArrayList<Node> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).isCorrect() || nodes.get(i).isExplored())
                System.out.print("C"); //∵
            else
                System.out.print(nodes.get(i).getSymbol());
            if ((i - (size - 1)) % size == 0)
                System.out.println();
        }
    }

    public static void printStates(int size, ArrayList<Node> nodes) {
        for (int k = 0; k < count; k++) {
            System.out.println("\nState " + k + ":");
            for (int i = 0; i < nodes.size(); i++) {
                if (((i - (size - 1)) % size == exploreNodes.get(k).getFirst())) // i * size + j the index in the arraylist
                    System.out.print("X");
                else if (nodes.get(i).isCorrect() || nodes.get(i).isExplored())
                    System.out.print("C"); //∵
                else
                    System.out.print(nodes.get(i).getSymbol());
                if ((i - (size - 1)) % size == 0)
                    System.out.println();
            }
        }
    }
}