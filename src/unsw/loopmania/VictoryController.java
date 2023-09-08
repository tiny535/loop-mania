package unsw.loopmania;

import java.io.IOException;

import javafx.fxml.FXML;

public class VictoryController {

    /**
     * facilitates switching to main menu
     */
    private MenuSwitcher menuSwitcher;

    public void setMainMenuSwitcher(MenuSwitcher menuSwitcher){
        this.menuSwitcher = menuSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        menuSwitcher.switchMenu();
    }
}
