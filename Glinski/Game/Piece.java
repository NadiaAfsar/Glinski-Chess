package Game;

import java.awt.*;

public class Piece {

    private final String pieceName;
    private final Color color;
    private final int row;
    private final int col;
    public Piece (String pieceName, Color color, int row, int col){
        this.pieceName = pieceName;
        this.color = color;
        this.row = row;
        this.col = col;
    }
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Color getColor() {
        return color;
    }
    public String getPieceName() {
        return pieceName;
    }

}
