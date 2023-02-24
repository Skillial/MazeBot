import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class app {
    static boolean flag = true;
    static int count = 0;

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
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        print(size, path);
        createNode(size, path, nodes);
        int start = getStartNode(nodes);
        DFS(nodes.get(start));
        if (!flag) {
            System.out.println("\nFinal path: ");
            printAns(nodes);
            System.out.println("\nExplored path: ");
            printAns(nodes);
            System.out.println("number of counts: " + count);
        } else {
            System.out.println("\nThere is no path!");
            System.out.println("number of counts: " + (count - 1));
        }
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
                        nodes.add(new Node(null, null, null, null, path.get(i).charAt(j), false, false));
                    else
                        nodes.add(new Node(null, null, nodes.get(j - 1), null, path.get(i).charAt(j), false, false));
                } else {
                    if (j == 0)
                        nodes.add(new Node(nodes.get(i * size - size), null, null, null, path.get(i).charAt(j), false, false));
                    else
                        nodes.add(new Node(nodes.get(i * size + j - size), null, nodes.get(i * size + j - 1), null, path.get(i).charAt(j), false, false));
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
                    check.setCorrect(false);
                }
            } else {
                flag = false;
                check.setCorrect(true);
            }
        }
    }

    public static void printAns(ArrayList<Node> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).isCorrect() || nodes.get(i).isExplored())
                System.out.print("∵");
            else
                System.out.print(nodes.get(i).getSymbol());
            if ((i - 4) % 5 == 0)
                System.out.println();
        }
    }
}
