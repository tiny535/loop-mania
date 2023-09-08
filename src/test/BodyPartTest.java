package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.item.AndurilSword;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.BodyPart;
import unsw.loopmania.item.Helmet;
import unsw.loopmania.item.Shield;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Stake;
import unsw.loopmania.item.Sword;
import unsw.loopmania.item.TreeStump;

public class BodyPartTest {
    @Test
    @DisplayName("Testing Staff body part")
    public void staffImageSrcTest() {
        Staff staff = new Staff(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(staff.getBodyPart(), BodyPart.HAND);
    }

    @Test
    @DisplayName("Testing Stake body part")
    public void stakeImageSrcTest() {
        Stake stake = new Stake(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(stake.getBodyPart(), BodyPart.HAND);
    }

    @Test
    @DisplayName("Testing Sword body part")
    public void swordImageSrcTest() {
        Sword sword = new Sword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(sword.getBodyPart(), BodyPart.HAND);
    }

    @Test
    @DisplayName("Testing Armour body part")
    public void armourImageSrcTest() {
        Armour armour = new Armour(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(armour.getBodyPart(), BodyPart.TORSO);
    }

    @Test
    @DisplayName("Testing Helmet body part")
    public void helmetImageSrcTest() {
        Helmet helmet = new Helmet(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(helmet.getBodyPart(), BodyPart.HEAD);
    }

    @Test
    @DisplayName("Testing Shield body part")
    public void shieldImageSrcTest() {
        Shield shield = new Shield(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(shield.getBodyPart(), BodyPart.ARM);
    }

    @Test
    @DisplayName("Testing TreeStump body part")
    public void treeStumpImageSrcTest() {
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(treeStump.getBodyPart(), BodyPart.ARM);
    }

    @Test
    @DisplayName("Testing AndurilSword body part")
    public void andurilSwordImageSrcTest() {
        AndurilSword andurilSword = new AndurilSword(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(andurilSword.getBodyPart(), BodyPart.HAND);
    }

    @Test
    @DisplayName("Testing getName method of Item")
    public void getNameTest() {
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(treeStump.getName(), "TREE STUMP");
    }

    @Test
    @DisplayName("Testing getDefenceEffect method of Item")
    public void getDefenceEffectTest() {
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(treeStump.getDefenceEffect(), 5);
    }

    @Test
    @DisplayName("Testing getDescription method of Item")
    public void getDescriptionTest() {
        TreeStump treeStump = new TreeStump(new SimpleIntegerProperty(), new SimpleIntegerProperty());
        assertEquals(treeStump.getDescription(), "An especially powerful shield, which provides higher defence against bosses");
    }
}
