package Game;

import graphics.Application;
import graphics.listeners.EventListener;
import graphics.models.StringColor;
import util.PieceName;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private Board board;
    private Piece piece;
    private Application application;
    private List<Cell> available_cells;
    private boolean isSelected;
    private boolean done;
    private Cell selected;
    private Color currentTurn;
    private List<StringColor> rp;
    private StringColor[] removed_pieces;
    private Piece WhiteKing;
    private Piece BlackKing;
    private boolean finished;
    private int WithoutCapture;
    private final File game;
    public GameManager() {
        board = new Board();
        board.cells = new Cell[12][108];
        application = new Application();
        this.application.registerEventListener(new GameEventListener(this));
        game = new File("Chess");
        if (game.exists()) {
            Load(game);
        }
        else {
            setPieces();
            currentTurn = Color.WHITE;
            application.setMessage("White's Turn!");
            rp = new ArrayList<>();
            application.showMessagePopup("Enjoy Your Game ^-^");
        }
    }

    public void setPieces() {
        board.cells = new Cell[12][108];
        for (int i = 1; i <= 11; i++) {
            for (int j = 'a'; j <= (int) 'k'; j++) {
                 if (isInBoard(i, j)) {
                    if (i == 7 && j < (int) 'k' && j > (int) 'a') {
                        piece = new Piece(PieceName.BLACK_PAWN, Color.BLACK, i, j);
                        board.cells[i][j] = new Cell(i, (char) j, piece);
                    } else if (i == 5 - Math.abs(j - (int) 'f')) {
                        piece = new Piece(PieceName.WHITE_PAWN, Color.WHITE, i, j);
                        board.cells[i][j] = new Cell(i, (char) j, piece);
                    } else if (j == (int) 'c' || j == (int) 'i') {
                        if (i == 8) {
                            piece = new Piece(PieceName.BLACK_ROCK, Color.BLACK, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        } else if (i == 1) {
                            piece = new Piece(PieceName.WHITE_ROCK, Color.WHITE, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        }
                    } else if (j == (int) 'd' || j == (int) 'h') {
                        if (i == 9) {
                            piece = new Piece(PieceName.BLACK_KNIGHT, Color.BLACK, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        } else if (i == 1) {
                            piece = new Piece(PieceName.WHITE_KNIGHT, Color.WHITE, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        }
                    } else if (j == (int) 'e') {
                        if (i == 10) {
                            piece = new Piece(PieceName.BLACK_QUEEN, Color.BLACK, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        } else if (i == 1) {
                            piece = new Piece(PieceName.WHITE_QUEEN, Color.WHITE, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        }
                    }
                     else if (j == (int) 'g') {
                        if (i == 10) {
                            piece = new Piece(PieceName.BLACK_KING, Color.BLACK, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                            BlackKing = piece;
                        }
                         else if (i == 1) {
                            piece = new Piece(PieceName.WHITE_KING, Color.WHITE, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                            WhiteKing = piece;
                        }
                    }
                    else if (j == (int) 'f') {
                        if (i >= 9) {
                            piece = new Piece(PieceName.BLACK_BISHOP, Color.BLACK, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        } else if (i <= 3) {
                            piece = new Piece(PieceName.WHITE_BISHOP, Color.WHITE, i, j);
                            board.cells[i][j] = new Cell(i, (char) j, piece);
                        }
                    }
                    if (board.getCells()[i][j] == null) {
                        board.getCells()[i][j] = new Cell(i, (char) j, null);
                    }
                }
            }
        }
        set();
    }
    public void validMoves (int row, int col) {
        available_cells = new ArrayList<>();
        Piece selectedPiece = board.getCells()[row][col].getPiece();
        //-------------------WHITE_PAWN---------------------
        if (selectedPiece.getPieceName().equals(PieceName.WHITE_PAWN)) {
            if (isInBoard(row + 1, col)) {
                if (board.getCells()[row + 1][col].getPiece() == null) {
                    available_cells.add(new Cell(row + 1, (char) col, null));
                    if (row == 5 - Math.abs(col - (int) 'f')) {
                        if (board.getCells()[row + 2][col].getPiece() == null) {
                            available_cells.add(new Cell(row + 2, (char) col, null));
                        }
                    }
                }
            }
            int x = 0;
            if (col < (int)'f') {
                x = 1;
            }
            if (isInBoard(row + x, col + 1)) {
                if (board.getCells()[row + x][col + 1].getPiece() != null) {
                    if (board.getCells()[row + x][col + 1].getPiece().getColor() == Color.BLACK) {
                        available_cells.add(board.getCells()[row + x][col + 1]);
                    }
                }
            }
            x = 0;
            if (col > (int)'f') {
                x = 1;
            }
            if (isInBoard(row + x, col - 1)) {
                if (board.getCells()[row + x][col - 1].getPiece() != null) {
                    if (board.getCells()[row + x][col - 1].getPiece().getColor() == Color.BLACK) {
                        available_cells.add(board.getCells()[row + x][col - 1]);
                    }
                }
            }
        }
        //-------------------BLACK_PAWN---------------------
        if (selectedPiece.getPieceName().equals(PieceName.BLACK_PAWN)) {
            if (isInBoard(row - 1, col)) {
                if (board.getCells()[row - 1][col].getPiece() == null) {
                    available_cells.add(new Cell(row - 1, (char) col, null));
                    if (row == 7) {
                        if (board.getCells()[row - 2][col].getPiece() == null) {
                            available_cells.add(new Cell(row - 2, (char) col, null));
                        }
                    }
                }
            }
            int x = 0;
            if (col < (int)'f') {
                x = 1;
            }
            if (isInBoard(row + x - 1, col + 1)) {
                if (board.getCells()[row + x - 1][col + 1].getPiece() != null) {
                    if (board.getCells()[row + x - 1][col + 1].getPiece().getColor() == Color.WHITE) {
                        available_cells.add(board.getCells()[row + x - 1][col + 1]);
                    }
                }
            }
            x = 0;
            if (col > (int)'f') {
                x = 1;
            }
            if (isInBoard(row + x - 1, col - 1)) {
                if (board.getCells()[row + x - 1][col - 1].getPiece() != null) {
                    if (board.getCells()[row + x - 1][col - 1].getPiece().getColor() == Color.WHITE) {
                        available_cells.add(board.getCells()[row + x - 1][col - 1]);
                    }
                }
            }
        }
        //-------------------BLACK_ROCK---------------------
        if (selectedPiece.getPieceName().equals(PieceName.BLACK_ROCK)) {
            moves(Color.WHITE, row, col, 1, 0);
            moves(Color.WHITE, row, col, -1, 0);
            moves(Color.WHITE, row, col, 0, -1);
            moves(Color.WHITE, row, col, 0, 1);
            moves(Color.WHITE, row, col, -1, 1);
            moves(Color.WHITE, row, col, -1, -1);
        }
        //-------------------WHITE_ROCK---------------------
        if (selectedPiece.getPieceName().equals(PieceName.WHITE_ROCK)) {
            moves(Color.BLACK, row, col, 1, 0);
            moves(Color.BLACK, row, col, -1, 0);
            moves(Color.BLACK, row, col, 0, -1);
            moves(Color.BLACK, row, col, 0, 1);
            moves(Color.BLACK, row, col, -1, 1);
            moves(Color.BLACK, row, col, -1, -1);
        }
        //-------------------WHITE_BISHOP---------------------
        if (selectedPiece.getPieceName().equals(PieceName.WHITE_BISHOP)) {
            moves(Color.BLACK, row, col, -1, 2);
            moves(Color.BLACK, row, col, -1, -2);
            moves(Color.BLACK, row, col, 1, 1);
            moves(Color.BLACK, row, col, 1, -1);
            moves(Color.BLACK, row, col, -2, 1);
            moves(Color.BLACK, row, col, -2, -1);
        }
        //-------------------BLACK_BISHOP---------------------
        if (selectedPiece.getPieceName().equals(PieceName.BLACK_BISHOP)) {
            moves(Color.WHITE, row, col, -1, 2);
            moves(Color.WHITE, row, col, -1, -2);
            moves(Color.WHITE, row, col, 1, 1);
            moves(Color.WHITE, row, col, 1, -1);
            moves(Color.WHITE, row, col, -2, 1);
            moves(Color.WHITE, row, col, -2, -1);
        }
        //-------------------WHITE_QUEEN---------------------
        if (selectedPiece.getPieceName().equals(PieceName.WHITE_QUEEN)) {
            moves(Color.BLACK, row, col, 1, 0);
            moves(Color.BLACK, row, col, -1, 0);
            moves(Color.BLACK, row, col, 0, -1);
            moves(Color.BLACK, row, col, 0, 1);
            moves(Color.BLACK, row, col, -1, 1);
            moves(Color.BLACK, row, col, -1, -1);
            moves(Color.BLACK, row, col, -1, 2);
            moves(Color.BLACK, row, col, -1, -2);
            moves(Color.BLACK, row, col, 1, 1);
            moves(Color.BLACK, row, col, 1, -1);
            moves(Color.BLACK, row, col, -2, 1);
            moves(Color.BLACK, row, col, -2, -1);
        }
        //-------------------BLACK_QUEEN---------------------
        if (selectedPiece.getPieceName().equals(PieceName.BLACK_QUEEN)) {
            moves(Color.WHITE, row, col, 1, 0);
            moves(Color.WHITE, row, col, -1, 0);
            moves(Color.WHITE, row, col, 0, -1);
            moves(Color.WHITE, row, col, 0, 1);
            moves(Color.WHITE, row, col, -1, 1);
            moves(Color.WHITE, row, col, -1, -1);
            moves(Color.WHITE, row, col, -1, 2);
            moves(Color.WHITE, row, col, -1, -2);
            moves(Color.WHITE, row, col, 1, 1);
            moves(Color.WHITE, row, col, 1, -1);
            moves(Color.WHITE, row, col, -2, 1);
            moves(Color.WHITE, row, col, -2, -1);
        }
        //-------------------WHITE_KING---------------------
        if (selectedPiece.getPieceName().equals(PieceName.WHITE_KING)) {
            validMove(Color.BLACK, row, col, 1, 0);
            validMove(Color.BLACK, row, col, -1, 0);
            int x = 0;
            if (col < (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, 1 + x, 1);
            validMove(Color.BLACK, row, col, x, 1);
            validMove(Color.BLACK, row, col, -1 + x, 1);
            validMove(Color.BLACK, row, col, -2 + x, 1);
            if (!(col < (int) 'f' && col + 2 > (int) 'f') && col < (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, -1 + x, 2);
            x = 0;
            if (col > (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, 1 + x, -1);
            validMove(Color.BLACK, row, col, x, -1);
            validMove(Color.BLACK, row, col, -1 + x, -1);
            validMove(Color.BLACK, row, col, -2 + x, -1);
            if (!(col > (int) 'f' && col - 2 < (int) 'f') && col > (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, -1 + x, -2);
        }
        //-------------------BLACK_KING---------------------
        if (selectedPiece.getPieceName().equals(PieceName.BLACK_KING)) {
            validMove(Color.WHITE, row, col, 1, 0);
            validMove(Color.WHITE, row, col, -1, 0);
            int x = 0;
            if (col < (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, 1 + x, 1);
            validMove(Color.WHITE, row, col, x, 1);
            validMove(Color.WHITE, row, col, -1 + x, 1);
            validMove(Color.WHITE, row, col, -2 + x, 1);
            if (!(col < (int) 'f' && col + 2 > (int) 'f') && col < (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, -1 + x, 2);
            x = 0;
            if (col > (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, 1 + x, -1);
            validMove(Color.WHITE, row, col, x, -1);
            validMove(Color.WHITE, row, col, -1 + x, -1);
            validMove(Color.WHITE, row, col, -2 + x, -1);
            if (!(col > (int) 'f' && col - 2 < (int) 'f') && col > (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, -1 + x, -2);
        }
        //-------------------WHITE_KNIGHT---------------------
        if (selectedPiece.getPieceName().equals(PieceName.WHITE_KNIGHT)) {
            int x = 0;
            if (col < (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, 2 + x, 1);
            validMove(Color.BLACK, row, col, -3 + x, 1);
            if (!(col < (int) 'f' && col + 2 > (int) 'f') && col < (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, 1 + x, 2);
            validMove(Color.BLACK, row, col, -3 + x, 2);
            if (!(col < (int) 'f' && col + 3 > (int) 'f') && col < (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, -1 + x, 3);
            validMove(Color.BLACK, row, col, -2 + x, 3);
            x = 0;
            if (col > (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, 2 + x, -1);
            validMove(Color.BLACK, row, col, -3 + x, -1);
            if (!(col > (int) 'f' && col - 2 < (int) 'f') && col > (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, 1 + x, -2);
            validMove(Color.BLACK, row, col, -3 + x, -2);
            if (!(col > (int) 'f' && col - 3 < (int) 'f') && col > (int) 'f') {
                x++;
            }
            validMove(Color.BLACK, row, col, -1 + x, -3);
            validMove(Color.BLACK, row, col, -2 + x, -3);
        }
        //-------------------BLACK_KNIGHT---------------------
        if (selectedPiece.getPieceName().equals(PieceName.BLACK_KNIGHT)) {
            int x = 0;
            if (col < (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, 2 + x, 1);
            validMove(Color.WHITE, row, col, -3 + x, 1);
            if (!(col < (int) 'f' && col + 2 > (int) 'f') && col < (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, 1 + x, 2);
            validMove(Color.WHITE, row, col, -3 + x, 2);
            if (!(col < (int) 'f' && col + 3 > (int) 'f') && col < (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, -1 + x, 3);
            validMove(Color.WHITE, row, col, -2 + x, 3);
            x = 0;
            if (col > (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, 2 + x, -1);
            validMove(Color.WHITE, row, col, -3 + x, -1);
            if (!(col > (int) 'f' && col - 2 < (int) 'f') && col > (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, 1 + x, -2);
            validMove(Color.WHITE, row, col, -3 + x, -2);
            if (!(col > (int) 'f' && col - 3 < (int) 'f') && col > (int) 'f') {
                x++;
            }
            validMove(Color.WHITE, row, col, -1 + x, -3);
            validMove(Color.WHITE, row, col, -2 + x, -3);
        }
    }

    public void clicked(int row, int col) {
        if (!finished) {
            if (isSelected) {
                isSelected = false;
                Piece piece1 = selected.getPiece();
                SetCellProperties(piece1.getRow(), (char)piece1.getCol(), piece1.getPieceName(), null, piece1.getColor());
                for (Cell available_cell : available_cells) {
                    int r = available_cell.getRow();
                    char c = available_cell.getCol();
                    Piece p = board.getCells()[r][c].getPiece();
                    if (p != null) {
                        SetCellProperties(r, c, p.getPieceName(), null, p.getColor());
                    } else {
                        SetCellProperties(r, c, null, null, null);
                    }
                }
                move(row, col);
                if (CheckMate(currentTurn) || !hasValidMove()) {
                    finished = true;
                    if (currentTurn == Color.WHITE) {
                        application.showMessagePopup("Black Won!");
                    }
                    else {
                        application.showMessagePopup("White Won!");
                    }
                    NewGame();
                }
                if (WithoutCapture == 50 || Draw()) {
                    finished = true;
                    application.showMessagePopup("DRAW!!");
                    NewGame();
                }
                Save(game);
            }
            if (!done && board.getCells()[row][col].getPiece() != null) {
                Piece p = board.getCells()[row][col].getPiece();
                if (p.getColor() == currentTurn) {
                    SetCellProperties(row, (char)col, p.getPieceName(), Color.RED, p.getColor());
                    selected = board.getCells()[row][col];
                    isSelected = true;
                    done = false;
                    validMoves(row, col);
                    for (Cell available_cell : available_cells) {
                       SetCellProperties(available_cell.getRow(), available_cell.getCol(), "\u2022", null, Color.red);
                    }
                }
            } else {
                done = false;
            }
        }
    }
    public static boolean isInBoard(int row, int col) {
        return row <= 11 - Math.abs(col - (int) 'f') && row >= 1 && col >= (int) 'a' && col <= (int) 'k';
    }
    public void moves(Color color, int row, int col, int i, int j) {
        if (j == 0) {
                if (isInBoard(row + i, col)) {
                    validMove(color, row, col, i, 0);
                    if (board.getCells()[row + i][col].getPiece() == null) {
                        moves(color, row + i, col, i, j);
                    }
                }
        }
        else if (j > 0) {
            if (col < (int)'f' && col + j > (int)'f'){
                int x = i + j - 1;
                if (isInBoard(row + x, col + j)) {
                    validMove(color, row, col, x, j);
                    if (board.getCells()[row + x][col + j].getPiece() == null) {
                        moves(color, row + x, col + j, i, j);
                    }
                }
            }
            else {
                int x = i;
                if (col + j <= (int) 'f') {
                    x += j;
                }
                if (isInBoard(row + x, col + j)) {
                    validMove(color, row, col, x, j);
                    if (board.getCells()[row + x][col + j].getPiece() == null) {
                        moves(color, row + x, col + j, i, j);
                    }
                }
            }
            }
        else {
            if (col > (int)'f' && col + j < (int)'f'){
                int x = i - j - 1;
                if (isInBoard(row + x, col + j)) {
                    validMove(color, row, col, x, j);
                    if (board.getCells()[row + x][col + j].getPiece() == null) {
                        moves(color, row + x, col + j, i, j);
                    }
                }
            }
            else {
                int x = i;
                if (col + j >= (int) 'f') {
                    x -= j;
                }
                if (isInBoard(row + x, col + j)) {
                    validMove(color, row, col, x, j);
                    if (board.getCells()[row + x][col + j].getPiece() == null) {
                        moves(color, row + x, col + j, i, j);
                    }
                }
            }
            }
    }
    public void validMove(Color color, int row, int col, int i, int j) {
        if (isInBoard(row + i, col + j)) {
            if (board.getCells()[row + i][col + j].getPiece() == null) {
                available_cells.add(board.getCells()[row + i][col + j]);
            } else if (board.getCells()[row + i][col + j].getPiece().getColor() == color) {
                available_cells.add(board.getCells()[row + i][col + j]);
            }
        }
    }
    public boolean isValidMove(int row, int col) {
            for (Cell available_cell : available_cells) {
                if (available_cell.getRow() == row && available_cell.getCol() == (char)col) {
                    return true;
                }
            }
        return false;
    }
    public void move(int row, int col) { if (isValidMove(row, col)) {
        int row1 = selected.getRow();
        char col1 = selected.getCol();
        Piece piece1 = selected.getPiece();
        Piece piece2 = board.getCells()[row][col].getPiece();
        board.getCells()[row1][col1] = new Cell (row1, col1, null);
        board.getCells()[row][col] = new Cell (row, (char)col, piece1);
        if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
            BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row, col);
        }
        else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
            WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row, col);
        }
        if (Check(piece1.getColor())) {
            board.getCells()[row1][col1] = new Cell (row1, col1, piece1);
            board.getCells()[row][col] = new Cell (row, (char)col, piece2);
            if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
                BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row1, col1);
            }
            else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
                WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row1, col1);
            }
        }
        else {
            WithoutCapture++;
            done = true;
            if (piece2 != null) {
                WithoutCapture = 0;
                rp.add(new StringColor(piece2.getPieceName(), piece2.getColor()));
                removed_pieces = new StringColor[rp.size()];
                for (int i = 0; i < rp.size(); i++) {
                    removed_pieces[i] = rp.get(i);
                }
            }
            board.getCells()[row][col].setPiece(new Piece(selected.getPiece().getPieceName(), selected.getPiece().getColor(), row, col));
            board.getCells()[row1][col1].setPiece(null);
            SetCellProperties(row1, col1, null, null, null);
            SetCellProperties(row, (char) col, piece1.getPieceName(), null, piece1.getColor());
            if (removed_pieces != null) {
                application.setRemovedPieces(removed_pieces);
            }
            if (selected.getPiece().getPieceName().equals(PieceName.BLACK_PAWN)) {
                if (isEndOfBoard(row, col, Color.BLACK)) {
                    String pieceName = application.showPromotionPopup();
                    String PN = "";
                    if (pieceName.equals("Queen")) {
                        PN = PieceName.BLACK_QUEEN;
                    }
                    else if (pieceName.equals("Rook")) {
                        PN = PieceName.BLACK_ROCK;
                    }
                    else if (pieceName.equals("Bishop")) {
                        PN = PieceName.BLACK_BISHOP;
                    }
                    else if (pieceName.equals("Knight")) {
                        PN = PieceName.BLACK_KNIGHT;
                    }
                    Piece p = new Piece(PN, Color.BLACK, row, col);
                    board.getCells()[row][col] = new Cell(row, (char)col, p);
                    SetCellProperties(row, (char)col, PN, null, Color.BLACK);
                }
            }
            else if (selected.getPiece().getPieceName().equals(PieceName.WHITE_PAWN)) {
                if (isEndOfBoard(row, col, Color.WHITE)) {
                    String pieceName = application.showPromotionPopup();
                    String PN = "";
                    if (pieceName.equals("Queen")) {
                        PN = PieceName.WHITE_QUEEN;
                    }
                    else if (pieceName.equals("Rook")) {
                        PN = PieceName.WHITE_ROCK;
                    }
                    else if (pieceName.equals("Bishop")) {
                        PN = PieceName.WHITE_BISHOP;
                    }
                    else if (pieceName.equals("Knight")) {
                        PN = PieceName.WHITE_KNIGHT;
                    }
                    Piece p = new Piece(PN, Color.WHITE, row, col);
                    board.getCells()[row][col] = new Cell(row, (char)col, p);
                    SetCellProperties(row, (char)col, PN, null, Color.WHITE);
                }
            }
            if (piece1.getColor() == Color.WHITE) {
                currentTurn = Color.BLACK;
                application.setMessage("Black's Turn!");
            } else {
                currentTurn = Color.WHITE;
                application.setMessage("White's Turn!");
            }
        }
    }

    }
    public void set() {
        for (int row = 1; row <= 11; row++){
            for (int col = 97; col < 108; col++) {
                if (board.getCells()[row][col] != null) {
                    if (board.getCells()[row][col].getPiece() != null) {
                        Piece p = board.getCells()[row][col].getPiece();
                        SetCellProperties(row, (char) col, p.getPieceName(), null, p.getColor());
                    }
                    else {
                        SetCellProperties(row, (char) col, null, null, null);
                    }
                }
            }
        }
    }
    public boolean Check(Color color) {
        Piece p;
        Color color2;
        if (color == Color.WHITE) {
            p = WhiteKing;
            color2 = Color.BLACK;
        }
        else {
            p = BlackKing;
            color2 = Color.WHITE;
        }
        for (int row = 1; row <= 11; row++) {
            for (int col = 'a'; col <= (int)'k'; col++) {
                if (isInBoard(row, col)) {
                    if (board.getCells()[row][col].getPiece() != null) {
                        if (board.getCells()[row][col].getPiece().getColor() == color2) {
                            validMoves(row, col);
                            for (Cell cell : available_cells) {
                                if (cell.getRow() == p.getRow() && cell.getCol() == p.getCol()) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean CheckMate(Color color) {
        if (Check(color)) {
            for (int row = 1; row <= 11; row++) {
                for (int col = 'a'; col <= (int)'k'; col++) {
                    if (isInBoard(row, col)) {
                        if (board.getCells()[row][col].getPiece() != null) {
                            if (board.getCells()[row][col].getPiece().getColor() == color) {
                                validMoves(row, col);
                                for (Cell cell: available_cells) {
                                    int row2 = cell.getRow();
                                    int col2 = cell.getCol();
                                    Piece piece1 = board.getCells()[row][col].getPiece();
                                    Piece piece2 = board.getCells()[row2][col2].getPiece();
                                    board.getCells()[row][col] = new Cell (row, (char)col, null);
                                    board.getCells()[row2][col2] = new Cell (row2, (char)col2, piece1);
                                    if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
                                        BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row2, col2);
                                    }
                                    else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
                                        WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row2, col2);
                                    }
                                    if (!(Check(color))) {
                                        board.getCells()[row][col] = new Cell (row, (char)col, piece1);
                                        board.getCells()[row2][col2] = new Cell (row2, (char)col2, piece2);
                                        if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
                                            BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row, col);
                                        }
                                        else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
                                            WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row, col);
                                        }
                                        return false;
                                    }
                                    else {
                                        board.getCells()[row][col] = new Cell (row, (char)col, piece1);
                                        board.getCells()[row2][col2] = new Cell (row2, (char)col2, piece2);
                                        if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
                                            BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row, col);
                                        }
                                        else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
                                            WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row, col);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    public boolean isEndOfBoard(int row , int col, Color color) {
        if (color == Color.WHITE) {
            if (row == 11 - Math.abs(col - (int) 'f')) {
                return true;
            }
        }
        else {
            if (row == 1 ) {
                return true;
            }
        }
        return false;
    }
    public boolean hasValidMove() {
        for (int row = 1; row <= 11; row++) {
            for (int col = 'a'; col <= (int)'k'; col++) {
                if (isInBoard(row, col)) {
                    if (board.getCells()[row][col].getPiece() != null) {
                        if (board.getCells()[row][col].getPiece().getColor() == currentTurn) {
                            validMoves(row, col);
                            if (available_cells.size() != 0) {
                                for (Cell cell: available_cells) {
                                    int row2 = cell.getRow();
                                    int col2 = cell.getCol();
                                    Piece piece1 = board.getCells()[row][col].getPiece();
                                    Piece piece2 = board.getCells()[row2][col2].getPiece();
                                    board.getCells()[row][col] = new Cell (row, (char)col, null);
                                    board.getCells()[row2][col2] = new Cell (row2, (char)col2, piece1);
                                    if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
                                        BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row2, col2);
                                    }
                                    else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
                                        WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row2, col2);
                                    }
                                    if (!(Check(currentTurn))) {
                                        board.getCells()[row][col] = new Cell (row, (char)col, piece1);
                                        board.getCells()[row2][col2] = new Cell (row2, (char)col2, piece2);
                                        if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
                                            BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row, col);
                                        }
                                        else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
                                            WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row, col);
                                        }
                                        return true;
                                    }
                                    else {
                                        board.getCells()[row][col] = new Cell (row, (char)col, piece1);
                                        board.getCells()[row2][col2] = new Cell (row2, (char)col2, piece2);
                                        if (piece1.getPieceName().equals(PieceName.BLACK_KING)) {
                                            BlackKing = new Piece(PieceName.BLACK_KING, Color.BLACK, row, col);
                                        }
                                        else if (piece1.getPieceName().equals(PieceName.WHITE_KING)) {
                                            WhiteKing = new Piece(PieceName.WHITE_KING, Color.WHITE, row, col);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public void Save(File file) {
        try {
            PrintStream data = new PrintStream(file);
            if (currentTurn == Color.BLACK) {
                data.println("black");
            }
            else {
                data.println("white");
            }
            data.println(WithoutCapture);
            data.println(WhiteKing.getRow());
            data.println(WhiteKing.getCol());
            data.println(BlackKing.getRow());
            data.println(BlackKing.getCol());
            for (int row = 1; row <= 11; row++) {
                for (int col = 'a'; col <= (int) 'k'; col++) {
                    if (isInBoard(row, col)) {
                        Piece p = board.getCells()[row][col].getPiece();
                        if (p == null) {
                            data.println("null");
                            data.println("null");
                            data.println(row);
                            data.println(col);
                        }
                        else {
                            data.println(p.getPieceName());
                            if (p.getColor() == Color.BLACK) {
                                data.println("black");
                            }
                            else {
                                data.println("white");
                            }
                            data.println(row);
                            data.println(col);
                        }
                    }
                }
            }
            if (removed_pieces != null) {
            for (StringColor piece: removed_pieces) {
                data.println(piece.getText());
                if (piece.getColor() == Color.BLACK) {
                    data.println("black");
                } else {
                    data.println("white");
                }
            }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public void Load(File file) {
        int x = 1;
        int row = 0;
        int col = 0;
        String PieceName = "";
        Color color = null;
        rp = new ArrayList<>();
        try {
            FileInputStream data = new FileInputStream(file);
            Scanner sc = new Scanner(data);
            while(sc.hasNextLine()) {
                if (x == 1) {
                    if (sc.nextLine().equals("black")) {
                        currentTurn = Color.BLACK;
                    }
                    else {
                        currentTurn = Color.WHITE;
                    }
                }
                else if (x == 2) {
                    WithoutCapture = Integer.parseInt(sc.nextLine());
                }
                else if (x == 3) {
                    row = Integer.parseInt(sc.nextLine());
                }
                else if (x == 4) {
                    col = Integer.parseInt(sc.nextLine());
                    WhiteKing = new Piece(util.PieceName.WHITE_KING, Color.WHITE, row, col);
                }
                else if (x == 5) {
                    row = Integer.parseInt(sc.nextLine());
                }
                else if (x == 6) {
                    col = Integer.parseInt((sc.nextLine()));
                    BlackKing = new Piece(util.PieceName.BLACK_KING, Color.BLACK, row, col);
                }
                else if (x >= 7 && x <= 370) {
                    if (x % 4 == 3) {
                        String pn = sc.nextLine();
                        if (pn.equals("null")) {
                            PieceName = null;
                        }
                        else {
                            PieceName = pn;
                        }
                    }
                    else if (x % 4 == 0) {
                        String c = sc.nextLine();
                        if (!c.equals("null")) {
                            if (c.equals("black")) {
                                color = Color.BLACK;
                            }
                            else {
                                color = Color.WHITE;
                            }
                        }
                    }
                    else if (x % 4 == 1) {
                        row = Integer.parseInt(sc.nextLine());
                    }
                    else {
                        col = Integer.parseInt(sc.nextLine());
                        if (PieceName == null) {
                            board.getCells()[row][col] = new Cell(row, (char)col, null);
                        }
                        else {
                            Piece p = new Piece(PieceName, color, row, col);
                            board.getCells()[row][col] = new Cell(row, (char)col, p);
                        }
                    }
                }
                else {
                    if (x % 2 == 1) {
                        PieceName = sc.nextLine();
                    }
                    else {
                        String c = sc.nextLine();
                        if (c.equals("black")) {
                            color = Color.BLACK;
                        }
                        else {
                            color = Color.WHITE;
                        }
                        StringColor removedPiece = new StringColor(PieceName, color);
                        rp.add(removedPiece);
                    }
                }
                x++;
            }
            set();
            removed_pieces = new StringColor[rp.size()];
            if (removed_pieces != null) {
                for (int i = 0; i < rp.size(); i++) {
                    removed_pieces[i] = rp.get(i);
                }
                application.setRemovedPieces(removed_pieces);
            }
            if (currentTurn == Color.BLACK) {
                application.setMessage("Black's Turn!");
            }
            else {
                application.setMessage("White's Turn!");
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        Save(game);
    }
    public void NewGame() {
        board = new Board();
        setPieces();
        currentTurn = Color.WHITE;
        application.setMessage("White's Turn!");
        rp = new ArrayList<>();
        removed_pieces = new StringColor[] {};
        application.setRemovedPieces(removed_pieces);
        application.showMessagePopup("Enjoy Your Game ^-^");
        Save(game);
    }
    public void SetCellProperties(int row, char col, String text, Color backGroundColor, Color textColor) {
        if (col == 'j') {
            application.setCellProperties(row, 'k', text, backGroundColor, textColor);
        }
        else if (col == 'k') {
            application.setCellProperties(row, 'l', text, backGroundColor, textColor);
        }
        else {
            application.setCellProperties(row, col, text, backGroundColor, textColor);
        }
    }
    public void OnClick(int row, char col) {
        if (col == 'k') {
            clicked(row, 'j');
        }
        else if (col == 'l') {
            clicked(row, 'k');
        }
        else {
            clicked(row, col);
        }
    }
    public boolean Draw() {
        int pieces = 0;
        for (int row = 1; row <= 11; row++) {
            for (int col = 'a'; col <= (int)'k'; col++) {
                if (isInBoard(row , col)) {
                    if (board.getCells()[row][col].getPiece() != null) {
                        pieces++;
                    }
                }
            }
        }
        return pieces == 2;
    }
}
