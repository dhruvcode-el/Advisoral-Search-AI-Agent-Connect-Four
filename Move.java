/**
* A simple record-like class to represent a move in Connect Four (just the column).
*/
public class Move {
    private final int column;
 
    public Move(int column) {
        this.column = column;
    }
 
    public int getColumn() {
        return column;
    }
 
    @Override
    public String toString() {
        return "Move{" +
                "column=" + column +
                '}';
    }
}
