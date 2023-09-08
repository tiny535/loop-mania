package unsw.loopmania;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * the main application
 * run main method from this class
 */
public class LoopManiaApplication extends Application {

    private static final String MUSIC_SRC = "src/music/";

    /**
     * the controller for the game. Stored as a field so can terminate it when click exit button
     */
    private LoopManiaWorldController mainController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // Load idle music loops - Main Menu, Game Loop, Shop Loop
        String musicFile = MUSIC_SRC + "main_loop.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        String gameMusicFile = MUSIC_SRC + "game_loop2.mp3";
        Media gameMusic = new Media(new File(gameMusicFile).toURI().toString());
        MediaPlayer gameMusicPlayer = new MediaPlayer(gameMusic);

        String shopMusicFile = MUSIC_SRC + "shop.mp3";
        Media shopMusic = new Media(new File(shopMusicFile).toURI().toString());
        MediaPlayer shopMusicPlayer = new MediaPlayer(shopMusic);

        // Load other music files
        String openEffectFile = "open_effect.mp3";
        AudioClip openEffectClip = getAudioClip(openEffectFile);

        String enterShopEffectFile = "enter_shop.mp3";
        AudioClip enterShopEffect = getAudioClip(enterShopEffectFile);

        String victoryFile = "victory.mp3";
        AudioClip victoryClip = getAudioClip(victoryFile);

        String losingFile = "losing2.mp3";
        AudioClip losingClip = getAudioClip(losingFile);

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("complex_goal.json");
        mainController = loopManiaLoader.loadController();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        Parent gameRoot = gameLoader.load();

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        // create new scene with the main menu (so we start with the main menu)
        Scene scene = new Scene(mainMenuRoot);

        // load the victory screen
        VictoryController victoryController = new VictoryController();
        FXMLLoader victoryLoader = new FXMLLoader(getClass().getResource("VictoryView.fxml"));
        victoryLoader.setController(victoryController);
        Parent victoryRoot = victoryLoader.load();

        // load the victory screen
        VictoryController losingController = new VictoryController();
        FXMLLoader losingLoader = new FXMLLoader(getClass().getResource("LosingView.fxml"));
        losingLoader.setController(losingController);
        Parent losingRoot = losingLoader.load();

        // load the shop screen
        ShopController shopController = new ShopController(mainController.getWorld().getShop());
        FXMLLoader shopLoader = new FXMLLoader(getClass().getResource("ShopView.fxml"));
        shopLoader.setController(shopController);
        Parent shopRoot = shopLoader.load();
        
        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        
        // Set Game Switchers
        mainController.setMainMenuSwitcher(() -> {
            mediaPlayer.play();
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        mainController.setVictorySwitcher(() -> {
            victoryClip.play();
            switchToRoot(scene, victoryRoot, primaryStage);
        });
        mainController.setLostGameSwitcher(() -> {
            losingClip.play();
            switchToRoot(scene, losingRoot, primaryStage);
        });
        mainController.setShopSwitcher(() -> {
            gameMusicPlayer.stop();
            enterShopEffect.play();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                System.err.println("Pause interrupted");
            }

            shopMusicPlayer.play();
            shopController.updateDetailsOnNewShop();
            switchToRoot(scene, shopRoot, primaryStage);
        });

        // Set Main Menu Switchers
        mainMenuController.setGameSwitcher(() -> {
            mediaPlayer.stop();
            openEffectClip.play();
            gameMusicPlayer.play();
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        // Set Victory Switchers
        victoryController.setMainMenuSwitcher(() -> {
            victoryClip.stop();
            mediaPlayer.play();
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        
        // Set Losing Switchers
        losingController.setMainMenuSwitcher(() -> {
            losingClip.stop();
            mediaPlayer.play();
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });

        // Set Shop Switchers
        shopController.setMenuSwitcher(() -> {
            gameMusicPlayer.play();
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.updateEquippedItems();
            mainController.startTimer();
        });

        // deploy the main onto the stage
        //gameRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates and returns an audio clip of a given mp3 file path.
     * @param pathname
     * @return
     */
    private AudioClip getAudioClip(String pathname) {
        return new AudioClip(new File(MUSIC_SRC + pathname).toURI().toString());
    }

    @Override
    public void stop(){
        // wrap up activities when exit program 
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage){
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
