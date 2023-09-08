package unsw.loopmania;

// fx:controller = "unsw.loopmania.MainMenuController"

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Window;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;

/**
 * controller for the main menu.
 */
public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;

    @FXML
    private ToggleGroup Gamemodes;

    @FXML
    private ToggleButton berserkerButton;

    @FXML
    private ToggleButton normalButton;

    @FXML
    private ToggleButton survivalButton;

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    /**
     * facilitates switching to main game upon button click, maps depends on chosen
     * game mode.
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
        if (berserkerButton.selectedProperty().getValue()){
            LoopManiaWorld.setGameMode(GameMode.BERSERKER);
        } else if (normalButton.selectedProperty().getValue()){
            LoopManiaWorld.setGameMode(GameMode.NORMAL);
        } else {
            LoopManiaWorld.setGameMode(GameMode.SURVIVAL);
        }
    }

    @FXML
    public void besrserkerSelected() {
        berserkerButton.setOpacity(2.0);
        survivalButton.setOpacity(0.5);
        normalButton.setOpacity(0.5);
    }

    @FXML
    public void normalSelected() {
        normalButton.setOpacity(2.0);
        survivalButton.setOpacity(0.5);
        berserkerButton.setOpacity(0.5);
    }

    @FXML
    public void survivalSelected() {
        survivalButton.setOpacity(2.0);
        berserkerButton.setOpacity(0.5);
        normalButton.setOpacity(0.5);
    }

    @FXML
    public void closeProgram(){
        Window stage = berserkerButton.getScene().getWindow();
        stage.hide();
    }
}
