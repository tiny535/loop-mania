package unsw.loopmania;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import unsw.loopmania.item.AndurilSword;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.BodyPart;
import unsw.loopmania.item.EquipableItem;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.item.TreeStump;
import unsw.loopmania.main.Inventory;
import unsw.loopmania.main.Shop;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.File;
import java.util.List;

public class ShopController {
    private static final String BUY = "BUY";
    private static final String SELL = "SELL";

    /**
     * The world's instance of Shop
     */
    private Shop shop;

    /**
     * Holds the item currently "selected" and visible in the info panel
     */
    private Item selectedItem;

    /**
     * Checks if currently selected item is doggieCoin
     */
    private boolean isSelectedItemDoggieCoin;

    /**
     * String path to images folder src
     */
    private String imageRoot = "src/images/";

    /**
     * facilitates switching to back to game UI
     */
    private MenuSwitcher menuSwitcher;

///////////////////// TOP PANEL //////////////////////
    @FXML
    private Label goldLabel;

    @FXML
    private Label cycleLabel;
//////////////////////////////////////////////////////

///////////////////// INFO PANEL /////////////////////
    @FXML
    private Label selectedItemType;

    @FXML
    private Label selectedItemPrice;

    @FXML
    private Label selectedItemAttack;

    @FXML
    private Label selectedItemDefence;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label quantityNumber;

    @FXML
    private Label selectedItemDescription;

    @FXML
    private ImageView selectedItemImage;

    @FXML
    private Button selectedItemAction;
//////////////////////////////////////////////////////

///////////////////// SELL PANEL /////////////////////
    @FXML
    private GridPane unequippedInventory;

    @FXML
    private GridPane equippedInventory;
//////////////////////////////////////////////////////

    public ShopController(Shop shop) {
        this.shop = shop;

        goldLabel = new Label();
        cycleLabel = new Label();

        selectedItemType = new Label();
        selectedItemPrice = new Label();
        selectedItemAttack = new Label();
        selectedItemDefence = new Label();
        quantityLabel = new Label("QUANTITY:");
        quantityNumber = new Label();
        selectedItemDescription = new Label();
        selectedItemImage = new ImageView();

        this.selectedItem = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        this.isSelectedItemDoggieCoin = false;

        unequippedInventory = new GridPane();
        equippedInventory = new GridPane();

        updateTopPanel();
        updateSellPanel();
    }

    @FXML
    public void initialize() {
        updateDetailsOnNewShop();
    }

    public void updateDetailsOnNewShop() {
        this.selectedItem = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        this.isSelectedItemDoggieCoin = false;
        updateTopPanel();
        updateSellPanel();
        updateInfoPanel(selectedItem.getBuyPrice(), selectedItem);
        setQuantityVisibility(false);
        shop.emptyBoughtItems();
    }

    private void updateQuantityLabel() {
        StringProperty itemQuantity = new SimpleStringProperty();
        itemQuantity.set(Integer.toString(shop.getCharacter().getInventory().getDoggieCoinQuantity()));
        quantityNumber.textProperty().bind(itemQuantity);
    }

    private void setQuantityVisibility(boolean bool) {
        quantityLabel.setVisible(bool);
        quantityNumber.setVisible(bool);
    }

    private void updateTopPanel() {
        StringProperty gold = new SimpleStringProperty();
        gold.set(Integer.toString(this.shop.getCharacter().getGoldQuantity()));
        goldLabel.textProperty().bind(gold);

        StringProperty cycle = new SimpleStringProperty();
        cycle.set(Integer.toString(this.shop.getCyclesToNextShop()) + " cycles(s)");
        cycleLabel.textProperty().bind(cycle);
    }

    private void updateInfoPanel(int price, Item item) {
        StringProperty itemName = new SimpleStringProperty();
        itemName.set(item.getName());
        selectedItemType.textProperty().bind(itemName);

        StringProperty itemPrice = new SimpleStringProperty();
        itemPrice.set(Integer.toString(price));
        selectedItemPrice.textProperty().bind(itemPrice);

        StringProperty itemAttack = new SimpleStringProperty();
        itemAttack.set(Integer.toString(item.getAttackEffect()));
        selectedItemAttack.textProperty().bind(itemAttack);

        StringProperty itemDefence = new SimpleStringProperty();
        itemDefence.set(Double.toString(item.getDefenceEffect()));
        selectedItemDefence.textProperty().bind(itemDefence);

        StringProperty itemDescription = new SimpleStringProperty();
        itemDescription.set(item.getDescription());
        selectedItemDescription.textProperty().bind(itemDescription);

        onLoad(item, selectedItemImage);
        this.selectedItem = item;
        this.isSelectedItemDoggieCoin = false;
        setQuantityVisibility(false);
    }

    private void updateSellPanel() {
        Image inventorySlotImage = new Image((new File("src/images/empty_slot.png")).toURI().toString());
        for (int x = 0; x < Inventory.INVENTORY_WIDTH; x++){
            for (int y = 0; y < Inventory.INVENTORY_HEIGHT; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                emptySlotView.setFitHeight(85);
                emptySlotView.setFitWidth(85);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        Image handSlotImage = new Image((new File("src/images/slot_hand.png")).toURI().toString());
        loadEquippedInventoryImage(new ImageView(handSlotImage), 0, 0);

        Image headSlotImage = new Image((new File("src/images/slot_head.png")).toURI().toString());
        loadEquippedInventoryImage(new ImageView(headSlotImage), 1, 0);

        Image bodySlotImage = new Image((new File("src/images/slot_body.png")).toURI().toString());
        loadEquippedInventoryImage(new ImageView(bodySlotImage), 2, 0);

        Image armSlotImage = new Image((new File("src/images/slot_arm.png")).toURI().toString());
        loadEquippedInventoryImage(new ImageView(armSlotImage), 3, 0);

        Inventory inventory = shop.getCharacter().getInventory();
        for (Item item : inventory.getEquippedItems()) {
            EquipableItem eItem = (EquipableItem)item;
            switch (eItem.getBodyPart()){
                case ARM:
                    loadEquippedInventoryImage(getItemImageView(item), 3, 0);
                    break;
                case HAND:
                    loadEquippedInventoryImage(getItemImageView(item), 0, 0);
                    break;
                case HEAD:
                    loadEquippedInventoryImage(getItemImageView(item), 1, 0);
                    break;
                case TORSO:
                    loadEquippedInventoryImage(getItemImageView(item), 2, 0);
                    break;
                default:
                    break;
            }
        }

        List<Item> uInventory = inventory.getUnequippedItems();
        int i = 0;
        for (int x = 0; x < Inventory.INVENTORY_WIDTH; x++){
            if (i == uInventory.size()) {
                break;
            }
            for (int y = 0; y < Inventory.INVENTORY_HEIGHT; y++){
                if (i == uInventory.size()) {
                    break;
                }
                ImageView view = getItemImageView(uInventory.get(i));
                view.setFitHeight(85);
                view.setFitWidth(85);
                unequippedInventory.add(view, y, x);
                i++;
            }
        }
    }

    private void loadEquippedInventoryImage(ImageView iView, int x, int y) {
        iView.setFitHeight(80);
        iView.setFitWidth(80);
        equippedInventory.add(iView, x, y);
    }

    /**
     * load an item into the specified ImageView object.
     * @param item
     */
    private void onLoad(Item item, ImageView iView) {
        Image image = new Image((new File(imageRoot + item.getImageSrc() + ".png")).toURI().toString());
        iView.setImage(image);
    }

    /**
     * Returns an ImageView object for the specified image
     * @param item
     */
    private ImageView getItemImageView(Item item) {
        Image image = new Image((new File(imageRoot + item.getImageSrc() + ".png")).toURI().toString());
        return new ImageView(image);
    }

    public void setGameSwitcher(MenuSwitcher menuSwitcher) {
        this.menuSwitcher = menuSwitcher;
    }

    @FXML
    public void populateSwordDetails() {
        Sword item = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateStakeDetails() {
        Stake item = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateStaffDetails() {
        Staff item = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateHelmetDetails() {
        Helmet item = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateShieldDetails() {
        Shield item = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateArmourDetails() {
        Armour item = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populatePotionDetails() {
        Potion item = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateOneRingDetails() {
        OneRing item = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateAndurilSwordDetails() {
        AndurilSword item = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateTreeStumpDetails() {
        TreeStump item = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        updateInfoPanel(item.getBuyPrice(), item);
        selectedItemAction.setText(BUY);
    }

    @FXML
    public void populateUnequippedInventorySellDetails(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        Integer cIndex = GridPane.getColumnIndex(node);
        Integer rIndex = GridPane.getRowIndex(node);
        int x = cIndex == null ? 0 : cIndex;
        int y = rIndex == null ? 0 : rIndex;

        int inventoryIndex = Inventory.INVENTORY_WIDTH * y + x;
        Inventory inventory = shop.getCharacter().getInventory();
        if (inventoryIndex >= inventory.getUnequippedItems().size()) {
            return;
        }

        Item item = inventory.getUnequippedItems().get(inventoryIndex);
        updateInfoPanel(item.getSellPrice(), item);
        selectedItemAction.setText(SELL);
    }

    @FXML
    public void populateEquippedInventorySellDetails(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        Integer cIndex = GridPane.getColumnIndex(node);
        int x = cIndex == null ? 0 : cIndex;

        switch(x) {
            case(0):
                populateEquippedItemDetails(BodyPart.HAND);
                break;
            case(1):
                populateEquippedItemDetails(BodyPart.HEAD);
                break;
            case(2):
                populateEquippedItemDetails(BodyPart.TORSO);
                break;
            case(3):
                populateEquippedItemDetails(BodyPart.ARM);
                break;
        }
    }

    private void populateEquippedItemDetails(BodyPart bodyPart) {
        Inventory inventory = shop.getCharacter().getInventory();
        for (Item item : inventory.getEquippedItems()) {
            EquipableItem eItem = (EquipableItem)item;
            if (eItem.getBodyPart() == bodyPart){
                updateInfoPanel(item.getSellPrice(), item);
                selectedItemAction.setText(SELL);
                break;
            }
        }
    }

    @FXML
    public void populateDoggieCoinDetails() {
        this.isSelectedItemDoggieCoin = true;

        StringProperty itemName = new SimpleStringProperty();
        itemName.set("DOGGIE COIN");
        selectedItemType.textProperty().bind(itemName);

        StringProperty itemPrice = new SimpleStringProperty();
        itemPrice.set(Integer.toString(shop.getDoggieCoinValue()));
        selectedItemPrice.textProperty().bind(itemPrice);

        StringProperty itemAttack = new SimpleStringProperty();
        itemAttack.set("-");
        selectedItemAttack.textProperty().bind(itemAttack);

        StringProperty itemDefence = new SimpleStringProperty();
        itemDefence.set("-");
        selectedItemDefence.textProperty().bind(itemDefence);

        StringProperty itemDescription = new SimpleStringProperty();
        itemDescription.set("A revolutionary asset type, which randomly fluctuates in sellable price to an extraordinary extent");
        selectedItemDescription.textProperty().bind(itemDescription);

        updateQuantityLabel();
        Image image = new Image((new File(imageRoot + "doggie_coin.png")).toURI().toString());
        selectedItemImage.setImage(image);
        selectedItemAction.setText(SELL);
        setQuantityVisibility(true);
    }

    @FXML
    public void clickShopAction() {
        if (isSelectedItemDoggieCoin) {
            shop.sellDoggieCoin(1);
        }
        else if (selectedItemAction.getText().equals(BUY)) {
            shop.buyItem(selectedItem);
        }
        else if (selectedItemAction.getText().equals(SELL)) {
            shop.sellItem(selectedItem);
        }
        updateQuantityLabel();
        updateTopPanel();
        updateSellPanel();
    }

    @FXML
    public void startNextLoop() {
        shop.incrementCyclesToNextShop();
        menuSwitcher.switchMenu();
    }

    public void setMenuSwitcher(MenuSwitcher menuSwitcher){
        this.menuSwitcher = menuSwitcher;
    }
}
