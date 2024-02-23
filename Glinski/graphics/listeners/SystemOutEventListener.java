package graphics.listeners;

import java.io.File;

import graphics.models.HexagonCell;


public class SystemOutEventListener implements EventListener {



    @Override
    public void onClick(int row, char col) {
//        HexagonCell targetCell = findCell(row, col);
//        if (targetCell != null) {
//            targetCell.setText(text);
//            targetCell.setBackGroundColor(backGroundColor);
//            targetCell.setTextColor(textColor);
//        } else {
//            System.err.printf("cant find cel with row=%d, col=%c", row, col);
//        }
    }

    @Override
    public void onLoad(File file) {
        System.out.println(file);
    }

    @Override
    public void onSave(File file) {
        System.out.println(file);
    }

    @Override
    public void onNewGame() {
        System.out.println("new game");
    }
}