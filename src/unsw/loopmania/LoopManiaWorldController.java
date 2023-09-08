package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.scene.control.Label;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import unsw.loopmania.building.Building;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.item.BodyPart;
import unsw.loopmania.item.EquipableItem;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.Potion;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Entity;
import unsw.loopmania.main.Inventory;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.main.Party;

import java.util.EnumMap;
import javafx.scene.layout.StackPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.IOException;


/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE{
    CARD,
    ITEM
}

/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application thread:
 *     https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 *     Note in https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html under heading "Threading", it specifies animation timelines are run in the application thread.
 * This means that the starter code does not need locks (mutexes) for resources shared between the timeline KeyFrame, and all of the  event handlers (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 *     using Task https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by itself or within a Service https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 *     Tasks ensure that any changes to public properties, change notifications for errors or cancellation, event handlers, and states occur on the JavaFX Application thread,
 *         so is a better alternative to using a basic Java Thread: https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 *     The Service class is used for executing/reusing tasks. You can run tasks without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need to implement locks on resources shared with the application thread (i.e. Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using Platform.runLater https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 *     This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass, buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    @FXML
    private Label goalsLabel;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    @FXML
    private Label hpLabel;

    @FXML
    private Label goldLabel;

    @FXML
    private Label doggieCoinLabel;

    @FXML
    private Label xpLabel;

    @FXML
    private Label cyclesLabel;

    @FXML
    private Label allyLabel;

    @FXML
    private Label levelLabel;

    /**
     * dragHereToConsumeStackPane is for consuming items
     */
    @FXML
    private StackPane dragHereToConsumeStackPane;


    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop location if appropriate.
     */
    // TODO = it would be a good idea for you to instead replace this with the building/item which should be dropped
    private ImageView currentlyDraggedImage;
    
    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged into the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged outside of the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    // RANI_NOTE - May need a handler specific for stackpane? And looks like potentially for each armour slot...

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;

    /**
     * object handling switching to the victory screen
     */
    private MenuSwitcher victorySwitcher;

    /**
     * object handling switching to the victory screen
     */
    private MenuSwitcher lostSwitcher;

    /**
     * object handling switching to shop screen
     */
    private MenuSwitcher shopSwitcher;

    /**
     * String path to images folder src
     */
    private final String imageRoot = "src/images/";

    /**
     * @param world world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);
        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);

        goalsLabel = new Label();
        hpLabel = new Label();
        xpLabel = new Label();
        goldLabel = new Label();
        doggieCoinLabel = new Label();
        cyclesLabel = new Label();
        allyLabel = new Label();
        levelLabel = new Label();

        liveUpdates();
    }

    /**
     * Updated the display to show the current character and game statistics 
     */
    private void liveUpdates(){
        StringProperty goals = new SimpleStringProperty();
        goals.set(world.getGoals().asString(world));
        goalsLabel.textProperty().bind(goals);
        
        StringProperty hp = new SimpleStringProperty();

        hp.set(this.world.getCharacter().getHp() + "/" + this.world.getCharacter().getMaxHp());
        hpLabel.textProperty().bind(hp);
        
        StringProperty xp = new SimpleStringProperty();
        xp.set(Integer.toString(this.world.getCharacter().getXPQuantity()));
        xpLabel.textProperty().bind(xp);

        StringProperty gold = new SimpleStringProperty();
        gold.set(Integer.toString(this.world.getCharacter().getGoldQuantity()));
        goldLabel.textProperty().bind(gold);

        StringProperty doggieCoin = new SimpleStringProperty();
        doggieCoin.set(Integer.toString(this.world.getCharacter().getDoggieCoinQuantity()));
        doggieCoinLabel.textProperty().bind(doggieCoin);

        StringProperty cycles = new SimpleStringProperty();
        cycles.set(Integer.toString(this.world.getCycleCount()));
        cyclesLabel.textProperty().bind(cycles);

        StringProperty allies = new SimpleStringProperty();
        allies.set((this.world.getCharacter().getPartySize()) + "/" + Party.MAX_PARTY_SIZE);
        allyLabel.textProperty().bind(allies);

        StringProperty level = new SimpleStringProperty();
        level.set(Integer.toString(this.world.getCharacter().getLevel()));
        levelLabel.textProperty().bind(level);
    }

    /**
     * TODO
     * Reloads equipped items into the GUI
     */
    public void updateEquippedItems() {
        List<Item> equippedInventory = world.getCharacter().getInventory().getEquippedItems();
        List<BodyPart> filledSlots = new ArrayList<>();
        Image image;
        for (Item item : equippedInventory) {
            EquipableItem equipped = (EquipableItem) item;
            filledSlots.add(equipped.getBodyPart());
            switch(equipped.getBodyPart()) {
                case HAND:
                    image = new Image((new File(imageRoot + item.getImageSrc() + ".png")).toURI().toString());
                    this.equippedItems.add(new ImageView(image), 0, 0);
                    break;
                case HEAD:
                    image = new Image((new File(imageRoot + item.getImageSrc() + ".png")).toURI().toString());
                    this.equippedItems.add(new ImageView(image), 1, 0);
                    break;
                case TORSO:
                    image = new Image((new File(imageRoot + item.getImageSrc() + ".png")).toURI().toString());
                    this.equippedItems.add(new ImageView(image), 2, 0);
                    break;
                case ARM:
                    image = new Image((new File(imageRoot + item.getImageSrc() + ".png")).toURI().toString());
                    this.equippedItems.add(new ImageView(image), 3, 0);
                    break;
            }
        }

        List<BodyPart> normalSlots = new ArrayList<BodyPart>(){{
            add(BodyPart.ARM);
            add(BodyPart.HAND);
            add(BodyPart.HEAD);
            add(BodyPart.TORSO);
        }};
        normalSlots.removeAll(filledSlots);
        for (BodyPart slot : normalSlots) {
            switch(slot) {
                case HAND:
                    Image handSlotImage = new Image((new File("src/images/slot_hand.png")).toURI().toString());
                    this.equippedItems.add(new ImageView(handSlotImage), 0, 0);
                    break;
                case HEAD:
                    Image headSlotImage = new Image((new File("src/images/slot_head.png")).toURI().toString());
                    this.equippedItems.add(new ImageView(headSlotImage), 1, 0);
                    break;
                case TORSO:
                    Image bodySlotImage = new Image((new File("src/images/slot_body.png")).toURI().toString());
                    this.equippedItems.add(new ImageView(bodySlotImage), 2, 0);
                    break;
                case ARM:
                    Image armSlotImage = new Image((new File("src/images/slot_arm.png")).toURI().toString());
                    this.equippedItems.add(new ImageView(armSlotImage), 3, 0);
                    break;
            }
        } 
    }

    @FXML
    public void initialize() {
        // TODO = load more images/entities during initialization
        
        Image pathTilesImage = new Image((new File(imageRoot + "32x32GrassAndDirtPath.png")).toURI().toString());
        Image inventorySlotImage = new Image((new File(imageRoot + "empty_slot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages){
            squares.getChildren().add(entity);
        }
        
        // add the ground underneath the cards
        for (int x=0; x<world.getWidth(); x++){
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x = 0; x < Inventory.INVENTORY_WIDTH; x++){
            for (int y = 0; y < Inventory.INVENTORY_HEIGHT; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);
    }

    /**
     * create and run the timer
     */
    public void startTimer(){
        
        System.out.println("starting timer");
        isPaused = false;
        // trigger adding code to process main game logic to queue. JavaFX will target framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.3F), event -> {
            liveUpdates();
            List<Item> boughtItems = world.getShop().getBoughtItems();
            if (!boughtItems.isEmpty()) {
                for (Item item : boughtItems) onLoad(item);
                world.getShop().emptyBoughtItems();
            }

            world.runTickMoves();

            // If Character is at hero's castle and the correct number of cycles have passed for next shop
            if (world.isItShopTime()) {
                try {
                    switchToShopScreen();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            // TODO - could add a flash animation when village/barracks effect is applied
            // have function return the gridpane coordinates to flash
            world.possiblyApplyVillageTile();
            world.possiblyApplyBarracksTile();

            List<BasicEnemy> defeatedEnemies = new ArrayList<>();
            defeatedEnemies.addAll(world.possiblyApplyTrapTile());
            defeatedEnemies.addAll(world.runBattles());
            liveUpdates();

            // Check if after this iteration we've lost the game
            if (!world.isCharacterAlive()) {
                try {
                    world.resetWorld();
                    switchToLosingScreen();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            
            // First Load all rare item types
            for (Class<? extends Item> rareItemClass : world.getRareItemsAvailable(defeatedEnemies.size())) {
                loadItem(rareItemClass);
            }
            
            // Now load all other drops
            for (BasicEnemy e: defeatedEnemies){
                reactToEnemyDefeat(e);
            }
            
            List<BasicEnemy> newEnemies = world.possiblySpawnEnemies();
            for (BasicEnemy newEnemy: newEnemies){
                onLoad(newEnemy);
            }

            // Check if after this iteration we've won the game
            if (world.isGameWon()) {
                try {
                    world.resetWorld();
                    switchToVictoryScreen();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            printThreadingNotes("HANDLED TIMER");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause(){
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    public void terminate(){
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * @param entity backend entity to be paired with view
     * @param view frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a card from the world, and pair it with an image in the GUI
     */
    private void loadCard(Class<? extends Building> buildingClass) {
        Card card = world.addCard(buildingClass);
        if (card == null){
            // Give the character either gold, xp, or an item
            convertOldestCard();
            card = world.addCard(buildingClass);
        }
        onLoad(card);
    }

    private void convertOldestCard(){
        List<Class<? extends Item>> items = world.convertOldestCard();
        if (items != null){
            for (Class<? extends Item> item: items){
                loadItem(item);
            }
        }
    }

    /**
     * load a item from the world, and pair it with an image in the GUI
     */
    private void loadItem(Class<? extends Item> itemType){
        // start by getting first available coordinates
        Item item = world.addUnequippedItem(itemType);
        if (item == null){
            // Give some cash/experience rewards for the discarding of the oldest item
            world.convertOldestItem();
            item = world.addUnequippedItem(itemType);
        }
        assert item != null;
        onLoad(item);
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning items/experience/gold
     * @param enemy defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(BasicEnemy enemy){

        int xpGained = enemy.getXPDrop();
        this.world.rewardXP(xpGained);
        
        int goldGained = enemy.getGoldDrop();
        this.world.rewardGold(goldGained);

        // Load each type of item that is rewards
        List<Class<? extends Item>> itemRewards = enemy.getItemDrops();
        for (Class<? extends Item> itemType : itemRewards) {
            loadItem(itemType);
        }
        
        // Load each type of building that is gained as a card
        List<Class<? extends Building>> cardRewards = enemy.getCardDrops();
        for (Class<? extends Building> buildingType : cardRewards) {
            loadCard(buildingType);
        }
    }

    /**
     * load a card into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
     * @param vampireCastleCard
     */
    private void onLoad(Card card) {

        ImageView view = new ImageView(getEntityImageView(card));

        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(card, view);
        cards.getChildren().add(view);
    }

    /**
     * Create an Image for an entity based on it's path
     * All image types must be PNGs
     * @param entity - to get the image for
     * @return Image related to the entity
     */
    private Image getEntityImageView(Entity entity) {
        assert entity != null;
        return new Image((new File(imageRoot + entity.getImageSrc() + ".png")).toURI().toString());
    }

    /**
     * load an item into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * @param item
     */
    private void onLoad(Item item) {
        ImageView view = new ImageView(getEntityImageView(item));
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(item, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load an enemy into the GUI
     * @param enemy
     */
    private void onLoad(BasicEnemy enemy) {
        ImageView view = new ImageView(getEntityImageView(enemy));
        addEntity(enemy, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * @param building
     */
    private void onLoad(Building building){
        ImageView view = new ImageView(getEntityImageView(building));
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * @param draggableType the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /*
                 *you might want to design the application so dropping at an invalid location drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType){
                    // problem = event is drop completed is false when should be true...
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != targetGridPane && db.hasImage()){

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        boolean objectPlaced = true;
                        switch (draggableType){
                            case CARD:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                if (newBuilding == null) {
                                    objectPlaced = false;
                                }
                                else {
                                    onLoad(newBuilding);
                                }
                                break;
                            case ITEM:
                                if (!canEquipItemByCoordinate(x, y, nodeX, nodeY)) {
                                    objectPlaced = false;
                                    break;
                                }
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);

                                Item oldItemInSlot = getEquippedItemByCoordinate(x, y);
                                if (oldItemInSlot != null){
                                    removeEquippedItem(oldItemInSlot);
                                }
                                equipItemByCoordinate(nodeX, nodeY);
                                targetGridPane.add(image, x, y, 1, 1);

                                break;
                            default:
                                break;
                        }

                        // TODO: implement better fix for visual glitch of remaining white square when dragged
                        node.setOpacity(1);

                        if (!objectPlaced) {
                            currentlyDraggedImage.setVisible(true);
                        }
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>(){
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    if(event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null){
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != anchorPaneRoot && db.hasImage()){
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                        
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    private void removeEquippedItem(Item item){
        this.world.removeEquippedItem(item);
    }

    private Item getEquippedItemByCoordinate(int x, int y){
        return this.world.getEquippedItemByCoordinate(x, y);
    }

    private void equipItemByCoordinate(int x, int y){
        this.world.equipItemByCoordinate(x, y);
    }

    /**
     * 
     * @param slotX  Slot in the equippiedInventory that item is being placed in
     * @param slotY  ^
     * @param nodeX  The current item's x coordinate
     * @param nodeY  ^
     * @return
     */
    private boolean canEquipItemByCoordinate(int slotX, int slotY, int nodeX, int nodeY){
        Item item = this.world.getUnequippedInventoryItemEntityByCoordinates(nodeX, nodeY);
        if (!(item instanceof EquipableItem)) return false;
        EquipableItem eItem = (EquipableItem)item;
        switch (eItem.getBodyPart()){
            case ARM:
                return slotX == 3;
            case HAND:
                return slotX == 0;
            case HEAD:
                return slotX == 1;
            case TORSO:
                return slotX == 2;
            default:
                return false;
        }
    }

    /**
     * remove the card from the world, and spawn and return a building instead where the card was dropped
     * @param cardNodeX the x coordinate of the card which was dragged, from 0 to width-1
     * @param cardNodeY the y coordinate of the card which was dragged (in starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card, where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card, where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * add drag event handlers to an ImageView
     * @param view the view to attach drag event handlers to
     * @param draggableType the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be dragged
     * @param targetGridPane the relevant gridpane to which the entity would be dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can detect it...
                currentlyDraggedType = draggableType;
                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);
    
                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                switch (draggableType){
                    case CARD:
                        draggedEntity.setImage(view.getImage());
                        break;
                    case ITEM:
                        draggedEntity.setImage(view.getImage());
                        break;
                    default:
                        break;
                }
                
                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n: targetGridPane.getChildren()){
                    // events for entering and exiting are attached to squares children because that impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = be more selective about whether highlighting changes - if it cannot be dropped in the location, the location shouldn't be highlighted!
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                //The drag-and-drop gesture entered the target
                                //show the user that it is an actual gesture target
                                if(event.getGestureSource() != n && event.getDragboard().hasImage()){
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you could program the game so if the new highlight location is invalid the highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                n.setOpacity(1);
                            }
                
                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
            
        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * @param draggableType either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane){
        // remove event handlers from nodes in children squares, from anchorPaneRoot, and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n: targetGridPane.getChildren()){
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        // TODO = handle additional key presses, e.g. for consuming a health potion
        switch (event.getCode()) {
        case SPACE:
            if (isPaused){
                startTimer();
            }
            else{
                pause();
            }
            break;
        case H:
            Item healthPotion = world.getUnequippedItemByClass(Potion.class);
            world.characterConsume(healthPotion);
            break;
        default:
            break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pause();
        mainMenuSwitcher.switchMenu();
    }

    public void setVictorySwitcher(MenuSwitcher victorySwitcher){
        this.victorySwitcher = victorySwitcher;
    }

    /**
     * this method is triggered when click button to go to victory screen in FXML
     * @throws IOException
     */
    private void switchToVictoryScreen() throws IOException {
        // TODO: may become destory the current instance of the game
        pause();
        victorySwitcher.switchMenu();
    }

    public void setLostGameSwitcher(MenuSwitcher lostSwitcher){
        this.lostSwitcher = lostSwitcher;
    }

    /**
     * this method is triggered when click button to go to victory screen in FXML
     * @throws IOException
     */
    private void switchToLosingScreen() throws IOException {
        // TODO: may become destory the current instance of the game
        pause();
        lostSwitcher.switchMenu();
    }

    public void setShopSwitcher(MenuSwitcher shopSwitcher) {
        this.shopSwitcher = shopSwitcher;
    }

    private void switchToShopScreen() throws IOException {
        // TODO: may become destory the current instance of the game
        pause();
        shopSwitcher.switchMenu();
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So it is vital this is handled in this Controller class
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        // TODO = tweak this slightly to remove items from the equipped inventory?
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                                               .onAttach((o, l) -> o.addListener(xListener))
                                               .onDetach((o, l) -> {
                                                    o.removeListener(xListener);
                                                    entityImages.remove(node);
                                                    squares.getChildren().remove(node);
                                                    cards.getChildren().remove(node);
                                                    equippedItems.getChildren().remove(node);
                                                    unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                                               .onAttach((o, l) -> o.addListener(yListener))
                                               .onDetach((o, l) -> {
                                                   o.removeListener(yListener);
                                                   entityImages.remove(node);
                                                   squares.getChildren().remove(node);
                                                   cards.getChildren().remove(node);
                                                   equippedItems.getChildren().remove(node);
                                                   unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here, position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is running on the application thread.
     * By running everything on the application thread, you will not need to worry about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel){
        System.out.println("\n###########################################");
        System.out.println("current method = "+currentMethodLabel);
        System.out.println("In application thread? = "+Platform.isFxApplicationThread());
        System.out.println("Current system time = "+java.time.LocalDateTime.now().toString().replace('T', ' '));
        System.out.println(this.world.worldState());
    }

    /**
     * Getter for LoopManiaWorld
     * @return
     */
    public LoopManiaWorld getWorld() {
        return this.world;
    }
}
