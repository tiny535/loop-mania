package unsw.loopmania.main;

import java.util.List;

import unsw.loopmania.ally.Ally;
import unsw.loopmania.battle.Fighter;
import unsw.loopmania.battle.SpecialAttacker;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.Potion;
import unsw.loopmania.map.PathPosition;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity implements Fighter, SpecialAttacker{

    private static final String IMAGE_SOURCE = "hero";

    public static final int STARTING_GOLD = 100;

    private static final int STARTING_HP = 100;
    private static final int STARTING_DAMAGE = 5;
    private static final int STARTING_DEFENSE = 0;
    private static final int STARTING_XP = 0;
    public static final int BASE_LEVEL_UP_BOUNDARY = 100;

    private PathPosition startingPosition;

    private int maxHp;
    private int currentHp;

    private int level;
    private int xp; 

    private int damage;
    private int defense;

    private Inventory inventory;
    private Deck deck;
    private Party party;

    private boolean isStunned;
    private int stunDuration;


    public Character(PathPosition position) {
        super(position);
        this.startingPosition = position;
        this.inventory = new Inventory();
        this.deck = new Deck();
        this.party = new Party();
        initialise();
    }

    /**
     * Reset character's starting ability as such
     */
    public void initialise() {
        this.currentHp = Character.STARTING_HP;
        this.maxHp = Character.STARTING_HP;
        this.xp = Character.STARTING_XP;
        this.level = 1;

        this.damage = Character.STARTING_DAMAGE;
        this.defense = Character.STARTING_DEFENSE;
        this.isStunned = false;

        this.inventory.empty();
        this.deck.clear();
        this.party.reset();
        super.setPosition(startingPosition);
    }

    /**
     * Get how much gold the character currently possesses
     * @return
     */
    public int getGoldQuantity() {
        return inventory.getGoldQuantity();
    }
    
    /**
     * Adds gold to the character's gold collection
     * @param gold
     */
    public void addGold(Gold gold) {
        inventory.addGold(gold);
    }
    public void addGold(int gold) {
        inventory.addGold(gold);
    }

    /**
     * Get how much xp the character currently has
     * @return
     */
    public int getXPQuantity() {
        return this.xp;
    }

    public int getDoggieCoinQuantity() {
        return this.inventory.getDoggieCoinQuantity();
    }

    /**
     * Adds experience to the character's experience 
     * @param experience
     */
    public void gainExperience(int experience) {
        this.xp += experience;
        if (checkCharacterLevelUp()) characterLevelUp();
    }

    public int getLevel(){
        return this.level;
    }

    public int getMaxHp(){
        return this.maxHp;
    }

    private boolean checkCharacterLevelUp(){
        return this.xp >= BASE_LEVEL_UP_BOUNDARY * (this.level * this.level);
    }

    private void characterLevelUp(){
        this.level++;
        this.maxHp += 5;
        this.currentHp = this.maxHp;
        this.damage += 2;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Add new item to the character's inventory. If the inventory is full, the oldest item in the inventory will be traded for gold and XP
     * before the new item is added. 
     * @param item
     */
    public void addItemToInventory(Item item) {
        this.inventory.addItemToInventory(item, this);
    }

    /**
     * Removes an existing item from the character's inventory. 
     * @param item
     */
    public void removeItemFromInventory(Item item) {
        this.inventory.removeItemFromInventory(item);
    }

    /**
     * Equips the character with an equipable item in the inventory. 
     * @param item
     */
    public void equipItem(Item item) {
        this.inventory.equipItem(item);
    }

    /**
     * Unequips an item from the character and returns it to the inventory as an unequipped item. 
     * @param item
     */
    public void unequipItem(Item item) {
        this.inventory.unequipItem(item);
    }

    @Override
    public int getHp(){
        return this.currentHp;
    }

    public int getDefense(){
        return this.defense;
    }

    public boolean isInventoryEmpty(){
        return this.inventory.isInventoryEmpty();
    }

    public boolean isDeckEmpty(){
        return this.deck.isDeckEmpty();
    }

    public boolean isPartyEmpty(){
        return this.party.isPartyEmpty();
    }

    @Override
    public void takeDamage(int damage){
        int newDamageTaken = this.inventory.applyEquippedProtection(damage);
        this.currentHp = this.currentHp - newDamageTaken;
    }

    @Override
    public int getDamage(){
        int bonusDamage = this.inventory.applyEquippedAttacking();
        return this.damage + bonusDamage;
    }

    @Override
    public void specialAttack(Fighter fighter, long seed){
        this.inventory.applyEquippedSpecialAttack(fighter, seed);
    }

    @Override
    public void specialAttack(Fighter fighter){
        this.specialAttack(fighter, System.currentTimeMillis());
    }

    public int specialDefence(Fighter fighter, int damage) {
        return this.inventory.applyEquippedSpecialDefence(fighter, damage);
    }

    public boolean isShieldEquipped(){
        return this.inventory.isShieldEquipped();
    }

    public void addCard(Card card){
        this.deck.addCard(card);
    }

    public int getDeckSize(){
        return this.deck.getSize();
    }

    public Card getCardByCordinates(int x, int y){
        return this.deck.getCardByCordinates(x, y);
    }

    public void removeCard(Card card){
        this.deck.removeCard(card);
    }

    public int getPartySize(){
        return this.party.getSize();
    }

    public Ally getAlly(int index){
        return this.party.getAlly(index);
    }

    public void removeAlly(Fighter allyToBeRemoved){
        this.party.removeAlly(allyToBeRemoved);
    }

    public boolean checkPartyContains(Fighter ally){
        return this.party.checkAllyContained(ally);
    }

    public List<Ally> getAllies(){
        return this.party.getAllies();
    }
    
    /**
     * Getter for party
     * @return
     */
    public Party getParty() {
        return party;
    }

    /**
     * Adds an ally to the party
     * @param ally
     */
    public void addPartyMember(Ally ally) {
        this.party.addAlly(ally);
    }

    /**
     * Heals character by healHp points
     */
    public void healCharacter(int healHp) {
        this.currentHp += healHp;
        if (currentHp > this.maxHp) {
            this.currentHp = this.maxHp;
        }
    }

    public Item getUnequippedItemByClass(Class<? extends Item> itemClass) {
        return inventory.getUnequippedItemByClass(itemClass);
    }

    /**
     * Attempt to consume a given item. Returns success or failure.
     * @param item - item to consume
     * @return
     */
    public boolean consumeItem(Item item) {
        if (item == null) return false;

        if (item.getClass() == Potion.class) {
            consumeHealthPotion();
            return true;
        }
        
        return false;
    }

    public void consumeOneRing() {
        int newHp = this.currentHp + this.inventory.consumeOneRing();
        this.currentHp = newHp <= 100 ? newHp : this.maxHp;
    }

    public void consumeHealthPotion() {
        int newHp = this.currentHp + this.inventory.consumeHealthPotion();
        this.currentHp = newHp < 100 ? newHp : this.maxHp;
    }

    public boolean inventoryContainsOneRing() {
        return this.inventory.inventoryContainsOneRing(); 
    }

    @Override
    public String getImageSrc() {
        return Character.IMAGE_SOURCE;
    }

    public List<Item> getUnequippedItems(){
        return this.inventory.getUnequippedItems();
    }

    public boolean isUnequippedInventoryFull(){
        return this.inventory.unequippedInventoryIsFull();
    }

    public boolean isDeckFull(){
        return this.deck.isDeckFull();
    }

    public Card getOldestCard(){
        return this.deck.getOldestCard();
    }

    public boolean isCardContained(Card card){
        return this.deck.isContained(card);
    }

    public void stun(int stunDuration){
        this.isStunned = true;
        this.stunDuration = stunDuration;
    }

    public boolean getIsStunned(){
        return this.isStunned;
    }

    public void decrementStunRounds(){
        this.stunDuration--;
        this.isStunned = (this.stunDuration == 0) ? false : true;
    }

    public void removeEquippedItem(Item item){
        this.inventory.removeEquippedItem(item);
    }

    public Item getEquippedItemByCoordinate(int x, int y){
        return this.inventory.getEquippedItemByCoordinate(x, y);
    }
}
