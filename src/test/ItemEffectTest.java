package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import unsw.loopmania.battle.Fighter;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.item.AndurilSword;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.SpecialEffectAttack;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.item.TreeStump;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.Inventory;
import unsw.loopmania.map.PathPosition;

public class ItemEffectTest {

    private Character character;
    private Inventory inventory;

    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setup() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        this.character = new Character(new PathPosition(0, orderedPath));

        this.inventory = new Inventory();
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Individual Equipped Items with Effect on Attack

    @Test
    @DisplayName("Test swords increase attack by 12 damage points")
    public void swordIncreaseAttack() {
        Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(sword, character);
        inventory.equipItem(sword);

        assertEquals(12, inventory.applyEquippedAttacking());
    }

    @Test
    @DisplayName("Test stakes increase attack by 10 damage points")
    public void stakesIncreaseAttack() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        assertEquals(10, inventory.applyEquippedAttacking());
    }

    @Test
    @DisplayName("Test staffs increase attack by 8 damage points")
    public void staffIncreasesAttack() {
        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(staff, character);
        inventory.equipItem(staff);

        assertEquals(8, inventory.applyEquippedAttacking());
    }

    @Test
    @DisplayName("Test helmets decrease attack by 2 damage points")
    public void helmetDecreasesAttack() {
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);
        inventory.equipItem(helmet);

        assertEquals(-2, inventory.applyEquippedAttacking());
    }

    @Test
    @DisplayName("Test armour and shield have no effect on damage points")
    public void armourAndShieldDecreasesAttack() {
        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);
        inventory.equipItem(armour);

        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        assertEquals(0, inventory.applyEquippedAttacking());
    }

    @Test
    @DisplayName("Equipping and unequipping weapons do not persist effects")
    public void equippingAndEquippingWeaponEffects() {
        // Manually unequip items to mimic how the world/controller does it.
        // Sword -> Stake -> Staff -> Sword
        Item oldSword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(oldSword, character);
        inventory.equipItem(oldSword);
        inventory.unequipItem(oldSword);

        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);
        assertEquals(10, inventory.applyEquippedAttacking());
        inventory.unequipItem(stake);

        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(staff, character);
        inventory.equipItem(staff);
        assertEquals(8, inventory.applyEquippedAttacking());
        inventory.unequipItem(staff);

        Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(sword, character);
        inventory.equipItem(sword);
        assertEquals(12, inventory.applyEquippedAttacking());

        // Now test combination w/ Helmet -> Stake -> Staff
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);
        inventory.equipItem(helmet);
        assertEquals(10, inventory.applyEquippedAttacking());

        inventory.unequipItem(sword);
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);
        assertEquals(8, inventory.applyEquippedAttacking());

        inventory.unequipItem(stake);
        inventory.addItemToInventory(staff, character);
        inventory.equipItem(staff);
        assertEquals(6, inventory.applyEquippedAttacking());
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Combined Effects of Equipped Attack Items
//// Given sword, staff and stake are equiped on the same part
//// they can only interact effects with helmet

@Test
@DisplayName("Effects of sword and helmet combine to increase damage by 10 points")
public void swordAndHelmetEffectOnAttack() {
    Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
    inventory.addItemToInventory(helmet, character);
    inventory.equipItem(helmet);

    Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
    inventory.addItemToInventory(sword, character);
    inventory.equipItem(sword);

    assertEquals(10, inventory.applyEquippedAttacking());
}

@Test
@DisplayName("Effects of stake and helmet combine to increase damage by 8 points")
public void stakeAndHelmetEffectOnAttack() {
    Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
    inventory.addItemToInventory(helmet, character);
    inventory.equipItem(helmet);

    Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
    inventory.addItemToInventory(stake, character);
    inventory.equipItem(stake);

    assertEquals(8, inventory.applyEquippedAttacking());
}

@Test
@DisplayName("Effects of staff and helmet combine to increase damage by 6 points")
public void staffAndHelmetEffectOnAttack() {
    Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
    inventory.addItemToInventory(helmet, character);
    inventory.equipItem(helmet);

    Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
    inventory.addItemToInventory(staff, character);
    inventory.equipItem(staff);

    assertEquals(6, inventory.applyEquippedAttacking());
}

///////////////////////////////////////////////////////////////////////////////////////
//// Individual Equipped Items with Effect on Defence

    @Test
    @DisplayName("Test shields reduce incoming damage by 2 points")
    public void shieldDecreasesEnemyDamage() {
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        assertEquals(3, inventory.applyEquippedProtection(5));
    }

    @Test
    @DisplayName("Test armour halves incoming damage")
    public void armourHalvesEnemyDamage() {
        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);
        inventory.equipItem(armour);

        assertEquals(10, inventory.applyEquippedProtection(20));
    }

    @Test
    @DisplayName("Test helmets reduce incoming damage by 2 points")
    public void helmetDecreasesEnemyDamage() {
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);
        inventory.equipItem(helmet);

        assertEquals(8, inventory.applyEquippedProtection(10));
    }

    @Test
    @DisplayName("Test stakes and swords don't reduce incoming damage")
    public void stakesAndSwordsDoNotAffectEnemyDamage() {
        Item stakes = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stakes, character);
        inventory.equipItem(stakes);

        assertEquals(10, inventory.applyEquippedProtection(10));

        Item swords = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(swords, character);
        inventory.equipItem(swords);

        assertEquals(10, inventory.applyEquippedProtection(10));
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Combined Effects of Equipped Defence Items

    @Test
    @DisplayName("Effects of helmet and armour combine to reduce damage by half and then 2 points")
    public void helmetAndArmourDefenceEffectTest() {
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);
        inventory.equipItem(helmet);

        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);
        inventory.equipItem(armour);

        assertEquals(8, inventory.applyEquippedProtection(20));

        inventory.unequipItem(helmet);
        inventory.equipItem(helmet);

        assertEquals(8, inventory.applyEquippedProtection(20));
    }

    @Test
    @DisplayName("Effects of shield and armour combine to reduce damage by half and then 2 points")
    public void shieldAndArmourDefenceEffectTest() {
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);
        inventory.equipItem(armour);

        assertEquals(8, inventory.applyEquippedProtection(20));

        inventory.unequipItem(shield);
        inventory.equipItem(shield);

        assertEquals(8, inventory.applyEquippedProtection(20));
    }

    @Test
    @DisplayName("Effects of helmet and shield combine to reduce damage 4 points")
    public void helmetAndShieldDefenceEffectTest() {
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);
        inventory.equipItem(helmet);

        assertEquals(16, inventory.applyEquippedProtection(20));

        inventory.unequipItem(shield);
        inventory.equipItem(shield);

        assertEquals(16, inventory.applyEquippedProtection(20));
    }

    @Test
    @DisplayName("Combined helmet, shield and armour to halve damage and then reduce by 4 points")
    public void helmetShieldAndArmourCombinedDefenceEffect() {
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(helmet, character);
        inventory.equipItem(helmet);

        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(armour, character);
        inventory.equipItem(armour);

        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(shield, character);
        inventory.equipItem(shield);

        assertEquals(6, inventory.applyEquippedProtection(20));

        inventory.unequipItem(helmet);
        inventory.equipItem(helmet);

        assertEquals(6, inventory.applyEquippedProtection(20));

        inventory.unequipItem(armour);
        inventory.equipItem(armour);

        assertEquals(6, inventory.applyEquippedProtection(20));
    }

///////////////////////////////////////////////////////////////////////////////////////
//// Special Effects of Equipped Items

    @Test
    @DisplayName("Stakes deal an additional 10 critical damage to Vampires")
    public void stakesDealAdditionalDamageToVampires() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        ((SpecialEffectAttack) stake).applySpecialEffectAttack((Fighter) vampire);

        assertEquals(50, vampire.getHp());
    }

    @Test
    @DisplayName("Stakes deal no additional damage to other enemies")
    public void stakesDealNoAdditionalDamageToOtherEnemies() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(stake, character);
        inventory.equipItem(stake);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        ZombieEnemy zombie = new ZombieEnemy(new PathPosition(0, orderedPath));
        ((SpecialEffectAttack) stake).applySpecialEffectAttack((Fighter) zombie);

        assertEquals(20, zombie.getHp());

        SlugEnemy slug = new SlugEnemy(new PathPosition(0, orderedPath));
        ((SpecialEffectAttack) stake).applySpecialEffectAttack((Fighter) slug);

        assertEquals(10, slug.getHp());
    }

    @Test
    @DisplayName("Staffs have a chance to trance enemies, and can trance")
    public void staffsCausesTrances() {
        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(staff, character);
        inventory.equipItem(staff);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        ((Staff) staff).applySpecialEffectAttack((Fighter) vampire, 10);

        assertTrue(vampire.isTranced());

        assertEquals(60, vampire.getHp());
    }

    @Test
    @DisplayName("Staffs have a chance to trance enemies, but will miss trance")
    public void staffsMissesTrances() {
        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(staff, character);
        inventory.equipItem(staff);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        VampireEnemy vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        ((Staff) staff).applySpecialEffectAttack((Fighter) vampire, 20);

        assertTrue(!vampire.isTranced());

        assertEquals(60, vampire.getHp());
    }

//////////////////////////////////////////////////////////////////////////////////////////////
///// Rare items (Anduril Sword, Tree Stump)
    @Test
    @DisplayName("Anduril deals 12 HP damage")
    public void andurilSwordDealAttackDamageTest() {
        Item anduril = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(anduril, character);
        inventory.equipItem(anduril);

        assertEquals(inventory.applyEquippedAttacking(), 12);
    }
    
    @Test
    @DisplayName("Anduril has no defence effect")
    public void andurilSwordNoDefenceProtectionTest() {
        Item anduril = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(anduril, character);
        inventory.equipItem(anduril);

        assertEquals(inventory.applyEquippedProtection(20), 20);
    }

    @Test
    @DisplayName("Anduril has special effect of dealing additional 24 HP damage to bosses")
    public void andurilSwordDealsTripleDamageToBossesTest() {
        Item anduril = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(anduril, character);
        inventory.equipItem(anduril);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        ((AndurilSword) anduril).applySpecialEffectAttack((Fighter) elanMuske);
        assertEquals(elanMuske.getHp(), 76);

        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));
        ((AndurilSword) anduril).applySpecialEffectAttack((Fighter) doggie);
        assertEquals(doggie.getHp(), 56);
    }

    @Test
    @DisplayName("Anduril has no special effect on standard enemies")
    public void andurilSwordDealsNoAdditionalDamageToBasicEnemyTest() {
        Item anduril = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(anduril, character);
        inventory.equipItem(anduril);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        SlugEnemy slug = new SlugEnemy(new PathPosition(0, orderedPath));
        ((AndurilSword) anduril).applySpecialEffectAttack((Fighter) slug);
        assertEquals(slug.getHp(), 10);
    }

    @Test
    @DisplayName("TreeStump has no attack effect")
    public void treeStumpDealsNoDamageTest() {
        Item treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(treeStump, character);
        inventory.equipItem(treeStump);

        assertEquals(inventory.applyEquippedAttacking(), 0);
    }

    @Test
    @DisplayName("TreeStump has basic 5 HP defence effect")
    public void treeStumpDefenceEffectTest() {
        Item treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(treeStump, character);
        inventory.equipItem(treeStump);

        assertEquals(inventory.applyEquippedProtection(10), 5);
    }

    @Test
    @DisplayName("TreeStump has additional 3 HP defence effect from Boss Enemy attacks")
    public void treeStumpAdditionalDefenceAgainstBossesTest() {
        Item treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(treeStump, character);
        inventory.equipItem(treeStump);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        Fighter doggie = new DoggieEnemy(new PathPosition(0, orderedPath));
        int doggieDamage = inventory.applyEquippedSpecialDefence((Fighter) doggie, doggie.getDamage());
        
        assertEquals(doggieDamage, 9);

        Fighter elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        int elanMuskeDamage = inventory.applyEquippedSpecialDefence((Fighter) elanMuske, elanMuske.getDamage());

        assertEquals(elanMuskeDamage, 12);
    }

    @Test
    @DisplayName("TreeStump has no additional HP defence effect for normal enemy attacks")
    public void treeStumpNoAdditionalDefenceEffectAgainstNormalEnemiesTest() {
        Item treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        inventory.addItemToInventory(treeStump, character);
        inventory.equipItem(treeStump);

        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));

        Fighter vampire = new VampireEnemy(new PathPosition(0, orderedPath));
        int damage = inventory.applyEquippedSpecialDefence(vampire, vampire.getDamage());

        assertEquals(damage, 10);
    }

//////////////////////////////////////////////////////////////////////////////////////////////
///// Protective Item Test
    @Test
    @DisplayName("Test if items return the correct boolean for isProtectiveItem")
    public void isProtectiveItemTest() {
        Item treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());

        assertTrue(treeStump.isProtectiveItem());
        assertTrue(shield.isProtectiveItem());
        assertTrue(armour.isProtectiveItem());
        assertTrue(helmet.isProtectiveItem());

        Item andurilSword = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertTrue(!andurilSword.isProtectiveItem());
    }
}
