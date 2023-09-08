package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;

import unsw.loopmania.ally.Ally;
import unsw.loopmania.building.BarracksBuilding;
import unsw.loopmania.building.Building;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.HeroCastleBuilding;
import unsw.loopmania.building.TowerBuilding;
import unsw.loopmania.building.TrapBuilding;
import unsw.loopmania.building.VampireCastleBuilding;
import unsw.loopmania.building.VillageBuilding;
import unsw.loopmania.building.ZombiePitBuilding;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.item.AndurilSword;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.DoggieCoin;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Item;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Potion;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.item.TreeStump;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.Gold;
import unsw.loopmania.map.PathPosition;
import unsw.loopmania.map.PathTile;

public class ImageSourceTest {

    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setup() {
        // Creates a simpple ordered path to add enemies to
        this.orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
    } 

    @Test
    @DisplayName("Testing pathtile image source")
    public void pathTileImageSrcTest() {
        PathTile pathTile = new PathTile(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(pathTile.getImageSrc(), "32x32GrassAndDirtPath");
    }

    @Test
    @DisplayName("Testing card image source")
    public void cardImageSrcTest() {
        Building vampireCastle = new VampireCastleBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        Card card = new Card(new SimpleIntegerProperty(), new SimpleIntegerProperty(), vampireCastle);
        assertEquals(card.getImageSrc(), "vampire_castle_card");
    }

    @Test
    @DisplayName("Testing Sword image source")
    public void swordImageSrcTest() {
        Item sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(sword.getImageSrc(), "sword");
    }

    @Test
    @DisplayName("Testing Stake image source")
    public void stakeImageSrcTest() {
        Item stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(stake.getImageSrc(), "stake");
    }

    @Test
    @DisplayName("Testing Staff image source")
    public void staffImageSrcTest() {
        Item staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(staff.getImageSrc(), "staff");
    }

    @Test
    @DisplayName("Testing Armour image source")
    public void armourImageSrcTest() {
        Item armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(armour.getImageSrc(), "armour");
    }

    @Test
    @DisplayName("Testing Helmet image source")
    public void helmetImageSrcTest() {
        Item helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(helmet.getImageSrc(), "helmet");
    }

    @Test
    @DisplayName("Testing Shield image source")
    public void shieldImageSrcTest() {
        Item shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(shield.getImageSrc(), "shield");
    }

    @Test
    @DisplayName("Testing one ring image source")
    public void oneRingImageSrcTest() {
        Item oneRing = new OneRing(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(oneRing.getImageSrc(), "the_one_ring");
    }

    @Test
    @DisplayName("Testing potion image source")
    public void potionImageSrcTest() {
        Item potion = new Potion(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(potion.getImageSrc(), "potion");
    }

    @Test
    @DisplayName("Testing Gold image source")
    public void goldImageSrcTest() {
        Gold gold = new Gold(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 100);
        assertEquals(gold.getImageSrc(), "gold_pile");
    }

    @Test
    @DisplayName("Testing vampire image source")
    public void vampireImageSrcTest() {
        BasicEnemy VampireEnemy = new VampireEnemy(new PathPosition(0, orderedPath));
        assertEquals(VampireEnemy.getImageSrc(), "vampire");
    }

    @Test
    @DisplayName("Testing zombie image source")
    public void zombieImageSrcTest() {
        BasicEnemy ZombieEnemy = new ZombieEnemy(new PathPosition(0, orderedPath));
        assertEquals(ZombieEnemy.getImageSrc(), "zombie");
    }

    @Test
    @DisplayName("Testing slug image source")
    public void slugImageSrcTest() {
        BasicEnemy SlugEnemy = new SlugEnemy(new PathPosition(0, orderedPath));
        assertEquals(SlugEnemy.getImageSrc(), "slug");
    }

    @Test
    @DisplayName("Testing ally image source")
    public void allyImageSrcTest() {
        Ally ally = new Ally(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(ally.getImageSrc(), "ally");
    }

    @Test
    @DisplayName("Testing character image source")
    public void characterImageSrcTest() {
        Character hero = new Character(new PathPosition(0, orderedPath));
        assertEquals(hero.getImageSrc(), "hero");
    }


    @Test
    @DisplayName("Testing Vampire Castle image source")
    public void vampireCastleImageSrcTest() {
        Building vampireCastle = new VampireCastleBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(vampireCastle.getImageSrc(), "vampire_castle");
    }

    @Test
    @DisplayName("Testing Zombie Pit image source")
    public void zombiePitImageSrcTest() {
        Building zombiePit = new ZombiePitBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(zombiePit.getImageSrc(), "zombie_pit");
    }

    @Test
    @DisplayName("Testing tower image source")
    public void towerImageSrcTest() {
        Building tower = new TowerBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(tower.getImageSrc(), "tower");
    }

    @Test
    @DisplayName("Testing campfire image source")
    public void campfireImageSrcTest() {
        Building CampfireBuilding = new CampfireBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(CampfireBuilding.getImageSrc(), "campfire");
    }

    @Test
    @DisplayName("Testing Hero Castle image source")
    public void heroCastleImageSrcTest() {
        Building heroCastle = new HeroCastleBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(heroCastle.getImageSrc(), "heros_castle");
    }

    @Test
    @DisplayName("Testing Village image source")
    public void villageImageSrcTest() {
        Building village = new VillageBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(village.getImageSrc(), "village");
    }

    @Test
    @DisplayName("Testing barracks image source")
    public void barracksImageSrcTest() {
        Building barracks = new BarracksBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(barracks.getImageSrc(), "barracks");
    }

    @Test
    @DisplayName("Testing trap image source")
    public void trapBuildingImageSrcTest() {
        Building trap = new TrapBuilding(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(trap.getImageSrc(), "trap");
    }

    @Test
    @DisplayName("Testing tree stump image source")
    public void treeStumpImageSrcTest() {
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(treeStump.getImageSrc(), "tree_stump");
    }

    @Test
    @DisplayName("Testing anduril sword image source")
    public void andurilSwordImageSrcTest() {
        AndurilSword andurilSword = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(andurilSword.getImageSrc(), "anduril_sword");
    }

    @Test
    @DisplayName("Testing doggie image source")
    public void doggieEnemyImageSrcTest() {
        DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));
        assertEquals(doggie.getImageSrc(), "doggie");
    }

    @Test
    @DisplayName("Testing elan muske image source")
    public void elanMuskeEnemyImageSrcTest() {
        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));
        assertEquals(elanMuske.getImageSrc(), "elanMuske");
    }

    @Test
    @DisplayName("Testing doggie coin image source")
    public void doggieCoinImageSrcTest() {
        DoggieCoin doggieCoin = new DoggieCoin(new SimpleIntegerProperty(), new SimpleIntegerProperty(), 1);
        assertEquals(doggieCoin.getImageSrc(), "doggie_coin");
    }
}

