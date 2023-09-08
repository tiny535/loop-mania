# Project Assumptions

### Inventory ###
- There is a limit of 8 cards in the deck
- There is a limit of 20 items in the inventory
- Items cannot be stacked in the inventory
- Cards cannot be stacked in the deck

### Items ###
- Weaponry and protective gear are not perishable 
- Weaponry deal fixed damages to HP  
- When an item is equipped, it is no longer displayed as part of the unequipped inventory
- The player cannot unequip an item when their unequipped inventory is full
- When we pick up an item, it always goes into a player's unequipped inventory first i.e. item/s cannot be auto equipped
- You can sell both equipped and unequipped items
- When converting the oldest item in a full inventory, you can only convert items in your unequipped inventory
- If you equip and unequip an item, it is now the newest item
- Rare items require an unlock condition to give them a chance of dropping
- When an item is equipped onto a body part that is already equipped, the previously equipped item disappears and is replaced by the newly equipped item

### Health ###
- Using a health potion or one ring will not result in the player's HP being higher than the level's max HP 

### Shop ###
- Players cannot buy or sell cards
- Players can sell items in their inventory
- The player cannot purchase an item if the unequipped inventory is full
- Rare items can be bought at a high price
- The sell price is lower than the buy price for the same item 

### Buildings ### 
- Buildings will remain on the map permanently, unless it is a single-use building (e.g. Trap)
- Effects of a spawned building will take effect immediately 

### Enemies ### 
- Enemies may move along the path
- All enemies of the same type will have a max HP based on the cycle count
- Enemies' starting HP will depend on the enemy type 
- Drops by enemies will occur at random 
- Drops by enemies are limited to at most two items or cards per enemy 

### Enemy Drops ###
- Enemies will always drop a certain amount of experience when defeated
- Enemies will always drop a certain amount of gold when defeated
- Enemies will drop a range of items depending on a drop chance, with rarer items having a lower drop rate 

### Allies ### 
- Allies do not have special abilities 
- Allies cannot use items
- Players can only have a maximum of 8 allies in their party

### Player ###
- Players can only equip weapons that are in their inventory 
- Players can wield at most one weapon 
- Players can wear multiple protective gear if they are on different parts of the body 
- Players start the game with full HP 
- Players can place cards on the map at any time in the game, except during battles
- Players cannot have a HP higher than their max HP
- The player can level up, hence increasing their max HP and their base damage 
- The player will heal to max HP upon levelling up

### Gold ### 
- Gold collected by the player cannot be lost, unless it is used for purchases

### DoggieCoin ###
- The DoggieCoin value can never be zero
- DoggieCoins collected by the player cannot be lost, unless it is sold at the shop 
- DoggieCoins can only be dropped by the Doggie enemy 
- DoggieCoins cannot be purchased at the shop 

### Bosses ### 
- A Doggie boss will spawn every 20th cycle
- An Elan Muske boss will spawn every 40th cycle, assuming that the Character has reached 10,000 XP 

### Experience ### 
- XP increases for each enemy defeated 
- Amount of XP gained by defeating enemies will vary depending on the enemy type 
- XP gained by the player cannot be lost 

### Cards ### 
- When cards are placed on incompatible tiles, no building is spawned and the card remains in the deck

### Battles ###
- Each party involved in battles take turns attacking the opposing side 
- Each party involved can only attack one member of the opposing side on each turn, unless otherwise specified
- Equipped items on the player cannot be changed once a battle commences 
- Enemies will attack allies before the character
- The character and all their allies will attack, and then all the enemies will attack
- The character can have any number of Campfire buffs in a battle
- An enemy or ally cannot attack nor be attacked after being killed
- A trancedEnemy can be zombified
- Zombification is prioritized over an enemy becoming untranced

### Other ###
- Players can complete an unlimited number of cycles until the game is won or lost

### Controls ###
- Keybinds can be set for keyboard shortcuts
- When the game is paused, all actions on the map are paused 

### Extension ###
- The player can level up after gaining a certain level of XP 
- Enemies will level up every cycle, hence increasing their max HP and base damage
