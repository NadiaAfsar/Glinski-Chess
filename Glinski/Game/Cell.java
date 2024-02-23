package Game;

public class Cell {
    private final int row;
    private final char col;
    private Piece piece;
    public Cell (int row, char col, Piece piece){
        this.row = row;
        this.col = col;
        this.piece = piece;
    }
    public int getRow() {
        return row;
    }
    public char getCol() {
        return col;
    }
    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
