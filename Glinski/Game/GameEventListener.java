package Game;

import graphics.listeners.EventListener;

import java.awt.*;
import java.io.File;
import java.util.List;

public class GameEventListener implements EventListener {
    GameManager gameManager;
    public GameEventListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @Override
    public void onClick(int row, char col){
        gameManager.OnClick(row, col);
    }
    @Override
    public void onLoad(File file){
        gameManager.Load(file);
    }
    @Override
    public void onSave(File file){
        gameManager.Save(file);
    }
    @Override
    public void onNewGame(){
        gameManager.NewGame();
    }
}
