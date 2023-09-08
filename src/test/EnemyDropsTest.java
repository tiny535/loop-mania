package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import unsw.loopmania.battle.DropTable;
import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.building.ZombiePitBuilding;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class EnemyDropsTest {

    private List<Pair<Integer, Integer>> orderedPath;
    private PathPosition simplePosition;
    private DropTable dropTable;
    
    @BeforeEach
    public void setup() {
        this.orderedPath = new ArrayList<>();
        this.orderedPath.add(new Pair<Integer, Integer>(0,0));

        // Set up a simple path position we can use to construct enemies
        this.simplePosition = new PathPosition(0, orderedPath);

        this.dropTable = new DropTable();
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Drop Table and Enemy Drops for Gold and Experience

//// Drop Table Drops

    @Test
    @DisplayName("Drop table returns right amount of xp")
    public void baseXPFromDropTable(){
        assertEquals(10, this.dropTable.getXPDrop());
    }

    @Test
    @DisplayName("Drop table returns right amount of gold")
    public void baseGoldFromDropTable(){
        assertEquals(25, this.dropTable.getGoldDrop());
    }

//// Enemy Drops

@Test
@DisplayName("Each enemy drops the right amount of xp")
public void basicXPDropAcrossEnemies() {
    BasicEnemy slug = new SlugEnemy(this.simplePosition);
    assertEquals(10, slug.getXPDrop());

    BasicEnemy zombie = new ZombieEnemy(this.simplePosition);
    assertEquals(10, zombie.getXPDrop());

    BasicEnemy vampire = new VampireEnemy(this.simplePosition);
    assertEquals(10, vampire.getXPDrop());
}

@Test
@DisplayName("Each enemy drops the right amount of gold")
public void basicGoldDropAcrossEnemies() {
    BasicEnemy slug = new SlugEnemy(this.simplePosition);
    assertEquals(25, slug.getGoldDrop());

    BasicEnemy zombie = new ZombieEnemy(this.simplePosition);
    assertEquals(25, zombie.getGoldDrop());

    BasicEnemy vampire = new VampireEnemy(this.simplePosition);
    assertEquals(25, vampire.getGoldDrop());
}

///////////////////////////////////////////////////////////////////////////////////////
//// The next section uses generated seeds to test all combinations of items are possible

/*
    // To generate more seeds you can use the following code below:

    @Test
    public void testingDifferentSeedsForDrops() {

        for (int i = 200; i < 250; i++) {
            System.out.println("Seed is " + i);
            this.dropTable.getItemDrops(10, i);
        }
        
    }
*/

///////////////////////////////////////////////////////////////////////////////////////
//// Item Drops

    @Test
    @DisplayName("Items drops when luck is terrible i.e. > 50")
    public void getItemDropsTerribleLuck(){
        // As luck is terrible we have no chance of getting anything
        List<Class<? extends Item>> expectedDrops = new ArrayList<>();

        assertEquals(expectedDrops, dropTable.getItemDrops(51, 0));
        assertEquals(expectedDrops, dropTable.getItemDrops(51, 7));
        assertEquals(expectedDrops, dropTable.getItemDrops(51, 58));
        assertEquals(expectedDrops, dropTable.getItemDrops(51, 71));
        assertEquals(expectedDrops, dropTable.getItemDrops(51, 29));
        assertEquals(expectedDrops, dropTable.getItemDrops(51, 91));
        assertEquals(expectedDrops, dropTable.getItemDrops(51, 24));
    }

    @Test
    @DisplayName("Items drops when luck is bad i.e. 40 > luck <= 50")
    public void getItemDropsBadLuck(){
        // As luck is bad we have no chance of getting anything but a sword
        List<Class<? extends Item>> expectedDrops = new ArrayList<>() {
            {
                add(Sword.class);
            }
        };

        assertEquals(expectedDrops, dropTable.getItemDrops(41, 0));
        assertEquals(expectedDrops, dropTable.getItemDrops(41, 7));
        assertEquals(expectedDrops, dropTable.getItemDrops(41, 58));
        assertEquals(expectedDrops, dropTable.getItemDrops(41, 71));
        assertEquals(expectedDrops, dropTable.getItemDrops(41, 29));
        assertEquals(expectedDrops, dropTable.getItemDrops(41, 91));
        assertEquals(expectedDrops, dropTable.getItemDrops(41, 24));

        assertEquals(expectedDrops, dropTable.getItemDrops(50, 0));
        assertEquals(expectedDrops, dropTable.getItemDrops(50, 7));
        assertEquals(expectedDrops, dropTable.getItemDrops(50, 58));
        assertEquals(expectedDrops, dropTable.getItemDrops(50, 71));
        assertEquals(expectedDrops, dropTable.getItemDrops(50, 29));
        assertEquals(expectedDrops, dropTable.getItemDrops(50, 91));
        assertEquals(expectedDrops, dropTable.getItemDrops(50, 24));
    }    

//// Complex item drops

    @Test
    @DisplayName("Items drops when luck is okay i.e. 30 > luck <= 40")
    public void getItemDropsOkayLuck(){
        // As luck is okay we can get a sword and a helmet two orders

        // Seed 0 = [Sword, Stake, Shield, Helmet, Staff, Potion, Armour]
        assertEquals(swordThenHelmet, dropTable.getItemDrops(31, 0));
        assertEquals(swordThenHelmet, dropTable.getItemDrops(40, 0));

        // Seed is 6 = [Stake, Helmet, Sword, Shield, Armour, Potion, Staff]
        assertEquals(helmetThenSword, dropTable.getItemDrops(31, 6));
        assertEquals(helmetThenSword, dropTable.getItemDrops(40, 6));
       
    }

    @Test
    @DisplayName("Items drops when luck is good i.e. 20 > luck <= 30")
    public void getItemDropsGoodLuck(){
        // Good luck items = Potion, Helmet, Sword

        // Seed is 5 = [Stake, Sword, Helmet, Potion, Armour, Shield, Staff]
        assertEquals(swordThenHelmet, dropTable.getItemDrops(21, 5));
        assertEquals(swordThenHelmet, dropTable.getItemDrops(30, 5));
        
        // Seed is 1 = [Stake, Potion, Armour, Staff, Sword, Helmet, Shield]
        assertEquals(potionThenSword, dropTable.getItemDrops(21, 1));
        assertEquals(potionThenSword, dropTable.getItemDrops(30, 1));

        // Seed is 8 = [Shield, Potion, Staff, Helmet, Armour, Sword, Stake]
        assertEquals(potionThenHelmet, dropTable.getItemDrops(21, 8));
        assertEquals(potionThenHelmet, dropTable.getItemDrops(30, 8));
        
        // Seed is 3 = [Shield, Helmet, Sword, Staff, Stake, Potion, Armour]
        assertEquals(helmetThenSword, dropTable.getItemDrops(21, 3));
        assertEquals(helmetThenSword, dropTable.getItemDrops(30, 3));

        // Seed is 41 = [Armour, Shield, Helmet, Stake, Potion, Staff, Sword]
        assertEquals(helmetThenPotion, dropTable.getItemDrops(21, 41));
        assertEquals(helmetThenPotion, dropTable.getItemDrops(30, 41));

        // Seed is 13 = [Sword, Staff, Potion, Stake, Shield, Armour, Helmet]
        assertEquals(swordThenPotion, dropTable.getItemDrops(21, 13));
        assertEquals(swordThenPotion, dropTable.getItemDrops(30, 13));

    }

    @Test
    @DisplayName("Items drops when luck is great i.e. 10 > luck <= 20")
    public void getItemDropsGreatLuck(){
        // Great luck items = Potion, Helmet, Sword, Stake, Shield
        int lowBoundLuck = 11;
        int highBoundLuck = 20;
        
        // Seed is 44 = [Sword, Stake, Potion, Armour, Staff, Shield, Helmet]
        int seed = 44;
        assertEquals(swordThenStake, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenStake, dropTable.getItemDrops(highBoundLuck, seed));
        
        // Seed is 0 = [Armour, Potion, Sword, Staff, Helmet, Stake, Shield]
        seed = 0;
        assertEquals(potionThenSword, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenSword, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 34 = [Potion, Stake, Sword, Shield, Staff, Armour, Helmet]
        seed = 34;
        assertEquals(potionThenStake, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenStake, dropTable.getItemDrops(highBoundLuck, seed));
        
        // Seed is 25 = [Helmet, Stake, Potion, Sword, Staff, Shield, Armour]
        seed = 25;
        assertEquals(helmetThenStake, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(helmetThenStake, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 4 = [Shield, Helmet, Staff, Sword, Armour, Stake, Potion]
        seed = 4;
        assertEquals(shieldThenHelmet, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(shieldThenHelmet, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 50 = [Sword, Potion, Helmet, Shield, Staff, Armour, Stake]
        seed = 50;
        assertEquals(swordThenPotion, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenPotion, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 5 = [Stake, Sword, Helmet, Potion, Armour, Shield, Staff]
        seed = 5;
        assertEquals(stakeThenSword, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(stakeThenSword, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 9 = [Shield, Sword, Staff, Helmet, Armour, Stake, Potion]
        seed = 9;
        assertEquals(shieldThenSword, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(shieldThenSword, dropTable.getItemDrops(highBoundLuck, seed));
        
        // Seed is 12 = [Helmet, Shield, Armour, Stake, Potion, Sword, Staff]
        seed = 12;
        assertEquals(helmetThenShield, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(helmetThenShield, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 2 = [Sword, Shield, Staff, Potion, Armour, Helmet, Stake]
        seed = 2;
        assertEquals(swordThenShield, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenShield, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 115 = [Potion, Shield, Stake, Sword, Staff, Helmet, Armour]
        seed = 115;
        assertEquals(potionThenShield, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenShield, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 67 = [Shield, Potion, Sword, Stake, Staff, Armour, Helmet]
        seed = 67;
        assertEquals(shieldThenPotion, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(shieldThenPotion, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 87 = [Shield, Stake, Staff, Potion, Sword, Helmet, Armour]
        seed = 87;
        assertEquals(shieldThenStake, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(shieldThenStake, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 93 = [Stake, Helmet, Sword, Shield, Potion, Staff, Armour]
        seed = 93;
        assertEquals(stakeThenHelmet, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(stakeThenHelmet, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 82 = [Stake, Potion, Sword, Shield, Armour, Staff, Helmet]
        seed = 82;
        assertEquals(stakeThenPotion, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(stakeThenPotion, dropTable.getItemDrops(highBoundLuck, seed));

    }

    @Test
    @DisplayName("Items drops when luck is the best i.e. 0 >= luck <= 10")
    public void getItemDropsBestLuck(){
        // All items = Potion, Helmet, Sword, Stake, Shield, Armour, Staff
        int lowBoundLuck = 0;
        int highBoundLuck = 10;
        
        // Seed is 44 = [Sword, Stake, Potion, Armour, Staff, Shield, Helmet]
        int seed = 44;
        assertEquals(swordThenStake, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenStake, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 64 = [Sword, Armour, Shield, Stake, Potion, Staff, Helmet]
        seed = 64;
        assertEquals(swordThenArmour, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenArmour, dropTable.getItemDrops(highBoundLuck, seed));
        
        // Seed is 0 = [Armour, Potion, Sword, Staff, Helmet, Stake, Shield]
        seed = 0;
        assertEquals(armourThenPotion, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenPotion, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 21 = [Potion, Armour, Helmet, Staff, Stake, Sword, Shield]
        seed = 21;
        assertEquals(potionThenArmour, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenArmour, dropTable.getItemDrops(highBoundLuck, seed));
        
        // Seed is 14 = [Armour, Helmet, Sword, Stake, Staff, Shield, Potion]
        seed = 14;
        assertEquals(armourThenHelmet, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenHelmet, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 52 = [Shield, Armour, Stake, Staff, Potion, Helmet, Sword]
        seed = 52;
        assertEquals(shieldThenArmour, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(shieldThenArmour, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 50 = [Sword, Potion, Helmet, Shield, Staff, Armour, Stake]
        seed = 50;
        assertEquals(swordThenPotion, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenPotion, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 72 = [Armour, Sword, Potion, Stake, Staff, Shield, Helmet]
        seed = 72;
        assertEquals(armourThenSword, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenSword, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 13 = [Sword, Staff, Potion, Stake, Shield, Armour, Helmet]
        seed = 13;
        assertEquals(swordThenStaff, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenStaff, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 56 = [Helmet, Staff, Potion, Sword, Armour, Shield, Stake]
        seed = 56;
        assertEquals(helmetThenStaff, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(helmetThenStaff, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 38 = [Helmet, Armour, Potion, Staff, Stake, Sword, Shield]
        seed = 38;
        assertEquals(helmetThenArmour, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(helmetThenArmour, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 119 = [Staff, Sword, Armour, Shield, Potion, Stake, Helmet]
        seed = 119;
        assertEquals(staffThenSword, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(staffThenSword, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 86 = [Staff, Stake, Potion, Sword, Helmet, Shield, Armour]
        seed = 86;
        assertEquals(staffThenStake, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(staffThenStake, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 10 = [Staff, Helmet, Armour, Stake, Potion, Sword, Shield]
        seed = 10;
        assertEquals(staffThenHelmet, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(staffThenHelmet, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 41 = [Armour, Shield, Helmet, Stake, Potion, Staff, Sword]
        seed = 41;
        assertEquals(armourThenShield, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenShield, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 70 = [Potion, Staff, Stake, Sword, Helmet, Shield, Armour]
        seed = 70;
        assertEquals(potionThenStaff, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenStaff, dropTable.getItemDrops(highBoundLuck, seed));
        
        // Seed is 29 = [Shield, Staff, Helmet, Potion, Armour, Sword, Stake]
        seed = 29;
        assertEquals(shieldThenStaff, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(shieldThenStaff, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 51 = [Stake, Armour, Helmet, Shield, Potion, Staff, Sword]
        seed = 51;
        assertEquals(stakeThenArmour, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(stakeThenArmour, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 206 = [Stake, Staff, Sword, Helmet, Potion, Shield, Armour]
        seed = 206;
        assertEquals(stakeThenStaff, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(stakeThenStaff, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 46 = [Armour, Stake, Sword, Helmet, Shield, Staff, Potion]
        seed = 46;
        assertEquals(armourThenStake, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenStake, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 130 = [Armour, Staff, Stake, Shield, Sword, Potion, Helmet]
        seed = 130;
        assertEquals(armourThenStaff, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenStaff, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 22 = [Staff, Potion, Helmet, Shield, Armour, Sword, Stake]
        seed = 22;
        assertEquals(staffThenPotion, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(staffThenPotion, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 33 = [Staff, Shield, Sword, Potion, Helmet, Armour, Stake]
        seed = 33;
        assertEquals(staffThenShield, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(staffThenShield, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 24 = [Staff, Armour, Stake, Potion, Helmet, Sword, Shield]
        seed = 24;
        assertEquals(staffThenArmour, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(staffThenArmour, dropTable.getItemDrops(highBoundLuck, seed));

        // Seed is 58 = [Stake, Shield, Potion, Staff, Sword, Helmet, Armour]
        seed = 58;
        assertEquals(stakeThenShield, dropTable.getItemDrops(lowBoundLuck, seed));
        assertEquals(stakeThenShield, dropTable.getItemDrops(highBoundLuck, seed));
    }

//// Test one level up passing in a seed works
    @Test
    @DisplayName("Enemies will drops items when luck is the best i.e. 0 >= luck <= 10")
    public void getEnemiesItemDropsBestLuck(){
        // All items are possible (Potion, Helmet, Sword, Stake, Shield, Armour, Staff)
        BasicEnemy slug = new SlugEnemy(simplePosition);
        BasicEnemy zombie = new ZombieEnemy(simplePosition);
        BasicEnemy vampire = new VampireEnemy(simplePosition);
        
        int lowBoundLuck = 0;
        int highBoundLuck = 10;
        
        // Seed is 44 = [Sword, Stake, Potion, Armour, Staff, Shield, Helmet]
        int seed = 44;
        assertEquals(swordThenStake, slug.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenStake, slug.getItemDrops(highBoundLuck, seed));

        assertEquals(swordThenStake, zombie.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenStake, zombie.getItemDrops(highBoundLuck, seed));

        assertEquals(swordThenStake, vampire.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenStake, vampire.getItemDrops(highBoundLuck, seed));

        // Seed is 64 = [Sword, Armour, Shield, Stake, Potion, Staff, Helmet]
        seed = 64;
        assertEquals(swordThenArmour, slug.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenArmour, slug.getItemDrops(highBoundLuck, seed));

        assertEquals(swordThenArmour, zombie.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenArmour, zombie.getItemDrops(highBoundLuck, seed));

        assertEquals(swordThenArmour, vampire.getItemDrops(lowBoundLuck, seed));
        assertEquals(swordThenArmour, vampire.getItemDrops(highBoundLuck, seed));
        
        // Seed is 0 = [Armour, Potion, Sword, Staff, Helmet, Stake, Shield]
        seed = 0;
        assertEquals(armourThenPotion, slug.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenPotion, slug.getItemDrops(highBoundLuck, seed));

        assertEquals(armourThenPotion, zombie.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenPotion, zombie.getItemDrops(highBoundLuck, seed));

        assertEquals(armourThenPotion, vampire.getItemDrops(lowBoundLuck, seed));
        assertEquals(armourThenPotion, vampire.getItemDrops(highBoundLuck, seed));

        // Seed is 21 = [Potion, Armour, Helmet, Staff, Stake, Sword, Shield]
        seed = 21;
        assertEquals(potionThenArmour, slug.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenArmour, slug.getItemDrops(highBoundLuck, seed));

        assertEquals(potionThenArmour, zombie.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenArmour, zombie.getItemDrops(highBoundLuck, seed));

        assertEquals(potionThenArmour, vampire.getItemDrops(lowBoundLuck, seed));
        assertEquals(potionThenArmour, vampire.getItemDrops(highBoundLuck, seed));
        
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Building Drops

/*
    Seeds used in the next section

    Seed is 0 = [BarracksBuilding, TowerBuilding, ZombiePitBuilding, VampireCastleBuilding, CampfireBuilding, VillageBuilding, TrapBuilding]
    Seed is 1 = [VillageBuilding, TowerBuilding, BarracksBuilding, VampireCastleBuilding, ZombiePitBuilding, CampfireBuilding, TrapBuilding]
    Seed is 2 = [ZombiePitBuilding, TrapBuilding, VampireCastleBuilding, TowerBuilding, BarracksBuilding, CampfireBuilding, VillageBuilding]
    Seed is 3 = [TrapBuilding, CampfireBuilding, ZombiePitBuilding, VampireCastleBuilding, VillageBuilding, TowerBuilding, BarracksBuilding]
    Seed is 10 = [VampireCastleBuilding, CampfireBuilding, BarracksBuilding, VillageBuilding, TowerBuilding, ZombiePitBuilding, TrapBuilding]
    Seed is 11 = [CampfireBuilding, VillageBuilding, TrapBuilding, ZombiePitBuilding, TowerBuilding, BarracksBuilding, VampireCastleBuilding]
    Seed is 21 = [TowerBuilding, BarracksBuilding, CampfireBuilding, VampireCastleBuilding, VillageBuilding, ZombiePitBuilding, TrapBuilding]

*/
    private int cardSeed0 = 0;
    private int cardSeed1 = 1;
    private int cardSeed2 = 2;
    private int cardSeed3 = 3;
    private int cardSeed4 = 10;
    private int cardSeed5 = 11;
    private int cardSeed6 = 21;


    @Test
    @DisplayName("Buildings drops when luck is terrible i.e. 50 > luck")
    public void getCardDropsTerribleLuck(){
        // No cards available
        List<Class<? extends Building>> expectedDrops = new ArrayList<>();

        int luck = 51;
        assertEquals(expectedDrops, dropTable.getCardDrops(luck, cardSeed0));
        assertEquals(expectedDrops, dropTable.getCardDrops(luck, cardSeed1));
        assertEquals(expectedDrops, dropTable.getCardDrops(luck, cardSeed2));
        assertEquals(expectedDrops, dropTable.getCardDrops(luck, cardSeed3));
        assertEquals(expectedDrops, dropTable.getCardDrops(luck, cardSeed4));
        assertEquals(expectedDrops, dropTable.getCardDrops(luck, cardSeed5));
        assertEquals(expectedDrops, dropTable.getCardDrops(luck, cardSeed6));
    }

    @Test
    @DisplayName("Buildings drops when luck is bad i.e. 40 > luck <= 50")
    public void getCardDropsBadLuck(){
        // Cards available - village and vampire

        int lowerBoundLuck = 41;
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed6));

        int highBoundLuck = 50;
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed6));
        
    }

    @Test
    @DisplayName("Buildings drops when luck is okay i.e. 30 > luck <= 40")
    public void getCardDropsOkayLuck(){
        // Cards available - village, vampire, trap
        
        int lowerBoundLuck = 31;
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(trap, dropTable.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed6));

        int highBoundLuck = 40;
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(trap, dropTable.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed6));
    }

    @Test
    @DisplayName("Buildings drops when luck is good i.e. 20 > luck <= 30")
    public void getCardDropsGoodLuck(){
        // Cards available - village, vampire, trap, barracks

        int lowerBoundLuck = 21;
        assertEquals(barracks, dropTable.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(trap, dropTable.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(barracks, dropTable.getCardDrops(lowerBoundLuck, cardSeed6));
        
        int highBoundLuck = 30;
        assertEquals(barracks, dropTable.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(trap, dropTable.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(barracks, dropTable.getCardDrops(highBoundLuck, cardSeed6));
    }

    @Test
    @DisplayName("Buildings drops when luck is great i.e. 10 > luck <= 20")
    public void getCardDropsGreatLuck(){
        // Cards available - village, vampire, trap, barracks, zombie

        int lowerBoundLuck = 11;
        assertEquals(barracks, dropTable.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(zombiePit, dropTable.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(barracks, dropTable.getCardDrops(lowerBoundLuck, cardSeed6));
        
        int highBoundLuck = 20;
        assertEquals(barracks, dropTable.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(zombiePit, dropTable.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(barracks, dropTable.getCardDrops(highBoundLuck, cardSeed6));
    }

    @Test
    @DisplayName("Buildings drops when luck is best i.e. 0 >= luck <= 10")
    public void getCardDropsBestLuck(){
        // All Cards - village, vampire, trap, barracks, zombie, tower, campfire
        
        int lowerBoundLuck = 0;
        assertEquals(barracks, dropTable.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(zombiePit, dropTable.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(campfire, dropTable.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(tower, dropTable.getCardDrops(lowerBoundLuck, cardSeed6));
        
        int highBoundLuck = 10;
        assertEquals(barracks, dropTable.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, dropTable.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(zombiePit, dropTable.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(trap, dropTable.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, dropTable.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(campfire, dropTable.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(tower, dropTable.getCardDrops(highBoundLuck, cardSeed6));
    }

//// Test one level up enemy drops are working

    @Test
    @DisplayName("Enemies will drops cards when luck is the best i.e. 0 >= luck <= 10")
    public void getEnemiesCardDropsBestLuck(){
        // All items are possible (Potion, Helmet, Sword, Stake, Shield, Armour, Staff)
        BasicEnemy slug = new SlugEnemy(simplePosition);
        BasicEnemy zombie = new ZombieEnemy(simplePosition);
        BasicEnemy vampire = new VampireEnemy(simplePosition);
        
        int lowerBoundLuck = 0;
        assertEquals(barracks, slug.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, slug.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(zombiePit, slug.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(trap, slug.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, slug.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(campfire, slug.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(tower, slug.getCardDrops(lowerBoundLuck, cardSeed6));

        assertEquals(barracks, zombie.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, zombie.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(zombiePit, zombie.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(trap, zombie.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, zombie.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(campfire, zombie.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(tower, zombie.getCardDrops(lowerBoundLuck, cardSeed6));

        assertEquals(barracks, vampire.getCardDrops(lowerBoundLuck, cardSeed0));
        assertEquals(village, vampire.getCardDrops(lowerBoundLuck, cardSeed1));
        assertEquals(zombiePit, vampire.getCardDrops(lowerBoundLuck, cardSeed2));
        assertEquals(trap, vampire.getCardDrops(lowerBoundLuck, cardSeed3));
        assertEquals(vampireCastle, vampire.getCardDrops(lowerBoundLuck, cardSeed4));
        assertEquals(campfire, vampire.getCardDrops(lowerBoundLuck, cardSeed5));
        assertEquals(tower, vampire.getCardDrops(lowerBoundLuck, cardSeed6));

        int highBoundLuck = 10;
        assertEquals(barracks, slug.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, slug.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(zombiePit, slug.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(trap, slug.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, slug.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(campfire, slug.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(tower, slug.getCardDrops(highBoundLuck, cardSeed6));

        assertEquals(barracks, zombie.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, zombie.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(zombiePit, zombie.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(trap, zombie.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, zombie.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(campfire, zombie.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(tower, zombie.getCardDrops(highBoundLuck, cardSeed6));

        assertEquals(barracks, vampire.getCardDrops(highBoundLuck, cardSeed0));
        assertEquals(village, vampire.getCardDrops(highBoundLuck, cardSeed1));
        assertEquals(zombiePit, vampire.getCardDrops(highBoundLuck, cardSeed2));
        assertEquals(trap, vampire.getCardDrops(highBoundLuck, cardSeed3));
        assertEquals(vampireCastle, vampire.getCardDrops(highBoundLuck, cardSeed4));
        assertEquals(campfire, vampire.getCardDrops(highBoundLuck, cardSeed5));
        assertEquals(tower, vampire.getCardDrops(highBoundLuck, cardSeed6));

    }

///////////////////////////////////////////////////////////////////////////////////////
//// Variables for Complex Card Drops

    private List<Class<? extends Building>> barracks = new ArrayList<>() {
        {
            add(BarracksBuilding.class);
        }
    }; 

    private List<Class<? extends Building>> zombiePit = new ArrayList<>() {
        {
            add(ZombiePitBuilding.class);
        }
    }; 
    private List<Class<? extends Building>> village = new ArrayList<>() {
        {
            add(VillageBuilding.class);
        }
    }; 
    
    private List<Class<? extends Building>> campfire = new ArrayList<>() {
        {
            add(CampfireBuilding.class);
        }
    }; 
    private List<Class<? extends Building>> vampireCastle = new ArrayList<>() {
        {
            add(VampireCastleBuilding.class);
        }
    }; 
    
    private List<Class<? extends Building>> trap = new ArrayList<>() {
        {
            add(TrapBuilding.class);
        }
    }; 
    private List<Class<? extends Building>> tower = new ArrayList<>() {
        {
            add(TowerBuilding.class);
        }
    }; 

///////////////////////////////////////////////////////////////////////////////////////
//// Variables for Complex Item Drops

    // Sword first combinations
    private List<Class<? extends Item>> swordThenHelmet = new ArrayList<>() {
        {
            add(Sword.class);
            add(Helmet.class);
        }
    };
    private List<Class<? extends Item>> swordThenPotion = new ArrayList<>() {
        {
            add(Sword.class);
            add(Potion.class);
        }
    };
    private List<Class<? extends Item>> swordThenShield = new ArrayList<>() {
        {
            add(Sword.class);
            add(Shield.class);
        }
    };
    private List<Class<? extends Item>> swordThenStake = new ArrayList<>() {
        {
            add(Sword.class);
            add(Stake.class);
        }
    };
    private List<Class<? extends Item>> swordThenArmour = new ArrayList<>() {
        {
            add(Sword.class);
            add(Armour.class);
        }
    };
    private List<Class<? extends Item>> swordThenStaff = new ArrayList<>() {
        {
            add(Sword.class);
            add(Staff.class);
        }
    };

    // Helmet first combinations
    private List<Class<? extends Item>> helmetThenSword = new ArrayList<>() {
        {
            add(Helmet.class);
            add(Sword.class);
        }
    };
    private List<Class<? extends Item>> helmetThenPotion = new ArrayList<>() {
        {
            add(Helmet.class);
            add(Potion.class);
        }
    };
    private List<Class<? extends Item>> helmetThenShield = new ArrayList<>() {
        {
            add(Helmet.class);
            add(Shield.class);
        }
    };
    private List<Class<? extends Item>> helmetThenStake = new ArrayList<>() {
        {
            add(Helmet.class);
            add(Stake.class);
        }
    };
    private List<Class<? extends Item>> helmetThenArmour = new ArrayList<>() {
        {
            add(Helmet.class);
            add(Armour.class);
        }
    };
    private List<Class<? extends Item>> helmetThenStaff = new ArrayList<>() {
        {
            add(Helmet.class);
            add(Staff.class);
        }
    };

    // Potion first combinations
    private List<Class<? extends Item>> potionThenSword = new ArrayList<>() {
        {
            add(Potion.class);
            add(Sword.class);
        }
    };
    private List<Class<? extends Item>> potionThenHelmet = new ArrayList<>() {
        {
            add(Potion.class);
            add(Helmet.class);
        }
    };
    private List<Class<? extends Item>> potionThenShield = new ArrayList<>() {
        {
            add(Potion.class);
            add(Shield.class);
        }
    };
    private List<Class<? extends Item>> potionThenStake = new ArrayList<>() {
        {
            add(Potion.class);
            add(Stake.class);
        }
    };
    private List<Class<? extends Item>> potionThenArmour = new ArrayList<>() {
        {
            add(Potion.class);
            add(Armour.class);
        }
    };
    private List<Class<? extends Item>> potionThenStaff = new ArrayList<>() {
        {
            add(Potion.class);
            add(Staff.class);
        }
    };

    // Shield first combinations
    private List<Class<? extends Item>> shieldThenSword = new ArrayList<>() {
        {
            add(Shield.class);
            add(Sword.class);
        }
    };
    private List<Class<? extends Item>> shieldThenHelmet = new ArrayList<>() {
        {
            add(Shield.class);
            add(Helmet.class);
        }
    };
    private List<Class<? extends Item>> shieldThenPotion = new ArrayList<>() {
        {
            add(Shield.class);
            add(Potion.class);
        }
    };
    private List<Class<? extends Item>> shieldThenStake = new ArrayList<>() {
        {
            add(Shield.class);
            add(Stake.class);
        }
    };
    private List<Class<? extends Item>> shieldThenArmour = new ArrayList<>() {
        {
            add(Shield.class);
            add(Armour.class);
        }
    };
    private List<Class<? extends Item>> shieldThenStaff = new ArrayList<>() {
        {
            add(Shield.class);
            add(Staff.class);
        }
    };

    // Stake first combinations
    private List<Class<? extends Item>> stakeThenSword = new ArrayList<>() {
        {
            add(Stake.class);
            add(Sword.class);
        }
    };
    private List<Class<? extends Item>> stakeThenHelmet = new ArrayList<>() {
        {
            add(Stake.class);
            add(Helmet.class);
        }
    };
    private List<Class<? extends Item>> stakeThenPotion = new ArrayList<>() {
        {
            add(Stake.class);
            add(Potion.class);
        }
    };
    private List<Class<? extends Item>> stakeThenShield = new ArrayList<>() {
        {
            add(Stake.class);
            add(Shield.class);
        }
    };
    private List<Class<? extends Item>> stakeThenArmour = new ArrayList<>() {
        {
            add(Stake.class);
            add(Armour.class);
        }
    };
    private List<Class<? extends Item>> stakeThenStaff = new ArrayList<>() {
        {
            add(Stake.class);
            add(Staff.class);
        }
    };

    // Armour first combinations
    private List<Class<? extends Item>> armourThenSword = new ArrayList<>() {
        {
            add(Armour.class);
            add(Sword.class);
        }
    };
    private List<Class<? extends Item>> armourThenHelmet = new ArrayList<>() {
        {
            add(Armour.class);
            add(Helmet.class);
        }
    };
    private List<Class<? extends Item>> armourThenPotion = new ArrayList<>() {
        {
            add(Armour.class);
            add(Potion.class);
        }
    };
    private List<Class<? extends Item>> armourThenShield = new ArrayList<>() {
        {
            add(Armour.class);
            add(Shield.class);
        }
    };
    private List<Class<? extends Item>> armourThenStake = new ArrayList<>() {
        {
            add(Armour.class);
            add(Stake.class);
        }
    };
    private List<Class<? extends Item>> armourThenStaff = new ArrayList<>() {
        {
            add(Armour.class);
            add(Staff.class);
        }
    };

    // Staff first combinations
    private List<Class<? extends Item>> staffThenSword = new ArrayList<>() {
        {
            add(Staff.class);
            add(Sword.class);
        }
    };
    private List<Class<? extends Item>> staffThenHelmet = new ArrayList<>() {
        {
            add(Staff.class);
            add(Helmet.class);
        }
    };
    private List<Class<? extends Item>> staffThenPotion = new ArrayList<>() {
        {
            add(Staff.class);
            add(Potion.class);
        }
    };
    private List<Class<? extends Item>> staffThenShield = new ArrayList<>() {
        {
            add(Staff.class);
            add(Shield.class);
        }
    };
    private List<Class<? extends Item>> staffThenStake = new ArrayList<>() {
        {
            add(Staff.class);
            add(Stake.class);
        }
    };
    private List<Class<? extends Item>> staffThenArmour = new ArrayList<>() {
        {
            add(Staff.class);
            add(Armour.class);
        }
    };


///////////////////////////////////////////////////////////////////////////////////////
//// Rare Item Drops Test

    @Test
    @DisplayName("Rare items will drop in world given a minimum number of enemies killed")
    public void rareItemDropsTest() {
        LoopManiaWorld world = new LoopManiaWorld(1,1,this.orderedPath, GameMode.NORMAL);
        
        // Potential outcomes
        List<Class<? extends Item>> noDrop = new ArrayList<>();
        List<Class<? extends Item>> drop = new ArrayList<>();
        drop.add(OneRing.class);
        
        // Ensure the one ring can drop in this world
        world.setRareItems(drop);

        // 30 6
        int seed = 30;
        assertEquals(noDrop, world.getRareItemsAvailable(0, seed));
        assertEquals(noDrop, world.getRareItemsAvailable(1, seed));
        assertEquals(noDrop, world.getRareItemsAvailable(2, seed));

        // 37 5
        seed = 37;
        assertEquals(noDrop, world.getRareItemsAvailable(0, seed));
        assertEquals(drop, world.getRareItemsAvailable(1, seed));
        assertEquals(drop, world.getRareItemsAvailable(2, seed));

        // 33 3
        seed = 33;
        assertEquals(noDrop, world.getRareItemsAvailable(0, seed));
        assertEquals(drop, world.getRareItemsAvailable(1, seed));
        assertEquals(drop, world.getRareItemsAvailable(2, seed));
    }

}