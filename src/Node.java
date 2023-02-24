public class Node {
    Node top;
    Node bottom;
    Node left;
    Node right;
    char symbol;
    boolean explored;
    boolean correct;

    public char getSymbol() {
        return symbol;
    }

    public Node getTop() {
        return top;
    }

    public Node getBottom() {
        return bottom;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setBottom(Node bottom) {
        this.bottom = bottom;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Node(Node top, Node bottom, Node left, Node right, char symbol, boolean explored, boolean correct) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.symbol = symbol;
        this.explored = explored;
        this.correct = correct;
    }
}
