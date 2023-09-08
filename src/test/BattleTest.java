package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import unsw.loopmania.ally.Ally;
import unsw.loopmania.battle.Battle;
import unsw.loopmania.enemy.BasicEnemy;
import unsw.loopmania.enemy.DoggieEnemy;
import unsw.loopmania.enemy.ElanMuskeEnemy;
import unsw.loopmania.enemy.SlugEnemy;
import unsw.loopmania.enemy.VampireEnemy;
import unsw.loopmania.enemy.ZombieEnemy;
import unsw.loopmania.item.Armour;
import unsw.loopmania.item.OneRing;
import unsw.loopmania.item.Staff;
import unsw.loopmania.item.Sword;
import unsw.loopmania.main.Card;
import unsw.loopmania.main.Character;
import unsw.loopmania.main.GameMode;
import unsw.loopmania.main.LoopManiaWorld;
import unsw.loopmania.map.PathPosition;
import unsw.loopmania.building.CampfireBuilding;
import unsw.loopmania.building.TowerBuilding;

public class BattleTest {

    private static final long LESS_THAN_50_SEED = 15; // 40 < x < 50
    private static final long GREATER_THAN_50_SEED = 12;

    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath;
    private int width;
    private int height;

    @BeforeEach
    private void createBasicLoop() {
        // Creates an ordered path in a 10 by 10 grid of path tiles
        // The path in this grid is the same pattern used in the setup
        //      ^ > > > v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ x x x v
        //      ^ < < < <
        this.width = 10;
        this.height = 10;
        this.orderedPath = new ArrayList<>();

        orderedPath.add(new Pair<Integer, Integer>(0,0));
        
        // Sets a simple path loop
        for (int y = 0; y < height; y++){
            if (y == 0) {
                for (int x = 0; x < width; x++) {
                    orderedPath.add(new Pair<Integer, Integer>(x,y));
                }
            }

            else if (y == height - 1) {
                for (int x = width - 1; x >= 0; x--) {
                    orderedPath.add(new Pair<Integer, Integer>(x,y));
                }
            }

            else {
                orderedPath.add(new Pair<Integer, Integer>(width - 1,y));
            }
        }

        // The first column is the last to be added to the path in reverse
        for (int y = height - 1; y >= 0; y--){
            orderedPath.add(new Pair<Integer, Integer>(0,y));
        }
    }

    @Test
    @DisplayName("Testing battle loop with all elements except bosses")
    public void battleLoopTest(){
        List<BasicEnemy> defeatedEnemies = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
            
            Character character = new Character(new PathPosition(0, orderedPath));
            Ally ally1 = new Ally(character.x(), character.y());
            Ally ally2 = new Ally(character.x(), character.y());
            Ally ally3 = new Ally(character.x(), character.y());
            Staff staff = new Staff(character.x(), character.y());
            OneRing oneRing = new OneRing(character.x(), character.y());

            character.addItemToInventory(staff);
            character.equipItem(staff);
            character.addItemToInventory(oneRing);
            character.addPartyMember(ally1);
            character.addPartyMember(ally2);
            character.addPartyMember(ally3);

            this.world.setCharacter(character);

            SlugEnemy slug = new SlugEnemy(new PathPosition(1, orderedPath));
            ZombieEnemy zombie = new ZombieEnemy(new PathPosition(2, orderedPath));
            VampireEnemy vampire = new VampireEnemy(new PathPosition(orderedPath.size() - 1, orderedPath));
            VampireEnemy vampire2 = new VampireEnemy(new PathPosition(4, orderedPath));

            this.world.addEnemy(slug);
            this.world.addEnemy(zombie);
            this.world.addEnemy(vampire);
            this.world.addEnemy(vampire2);

            Card towerCard = world.loadCard(new TowerBuilding());
            Card campfireCard = world.loadCard(new CampfireBuilding());
            world.convertCardToBuildingByCoordinates(towerCard.getX(), towerCard.getY(), 1, 1);
            world.convertCardToBuildingByCoordinates(campfireCard.getX(), campfireCard.getY(), 1, 2);

            Battle battle = new Battle();
            battle.setCharacter(this.world.getCharacter());
            battle.setBattleEnemies(this.world.getEnemies());
            battle.setBuildings(this.world.getBuildings());

            defeatedEnemies = battle.runBattleLoop();

            assertTrue(defeatedEnemies.contains(slug));
            assertTrue(defeatedEnemies.contains(zombie));
            assertTrue(defeatedEnemies.contains(vampire));
        }
    }

    @Test
    @DisplayName("Testing battle with elanMuske boss")
    public void battleElanMuskeTest(){
        List<BasicEnemy> defeatedEnemies = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
            
            Character character = new Character(new PathPosition(0, orderedPath));
            Ally ally1 = new Ally(character.x(), character.y());
            Ally ally2 = new Ally(character.x(), character.y());
            Ally ally3 = new Ally(character.x(), character.y());

            Staff staff = new Staff(character.x(), character.y());
            OneRing oneRing = new OneRing(character.x(), character.y());

            character.addItemToInventory(staff);
            character.equipItem(staff);
            character.addItemToInventory(oneRing);
            character.addPartyMember(ally1);
            character.addPartyMember(ally2);
            character.addPartyMember(ally3);

            this.world.setCharacter(character);

            ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));

            this.world.addEnemy(elanMuske);

            Card towerCard = world.loadCard(new TowerBuilding());
            Card campfireCard = world.loadCard(new CampfireBuilding());
            world.convertCardToBuildingByCoordinates(towerCard.getX(), towerCard.getY(), 1, 1);
            world.convertCardToBuildingByCoordinates(campfireCard.getX(), campfireCard.getY(), 1, 2);
            
            Battle battle = new Battle();
            battle.setCharacter(this.world.getCharacter());
            battle.setBattleEnemies(this.world.getEnemies(), LESS_THAN_50_SEED);
            battle.setBuildings(this.world.getBuildings());

            defeatedEnemies = battle.runBattleLoop();

            assertTrue(defeatedEnemies.contains(elanMuske));
        }
    }

    @Test
    @DisplayName("Testing battle can not contain elanMuske even if he is in range")
    public void battleElanMuskeFightChanceTest(){
        List<BasicEnemy> defeatedEnemies = new ArrayList<>();
        this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
        
        Character character = new Character(new PathPosition(0, orderedPath));

        this.world.setCharacter(character);

        ElanMuskeEnemy elanMuske = new ElanMuskeEnemy(new PathPosition(0, orderedPath));

        this.world.addEnemy(elanMuske);
        
        Battle battle = new Battle();
        battle.setCharacter(this.world.getCharacter());
        battle.setBattleEnemies(this.world.getEnemies(), GREATER_THAN_50_SEED);
        battle.setBuildings(this.world.getBuildings());

        defeatedEnemies = battle.runBattleLoop();

        assertTrue(!defeatedEnemies.contains(elanMuske));
    }

    @Test
    @DisplayName("Testing battle with doggie boss")
    public void battleDoggieTest(){
        List<BasicEnemy> defeatedEnemies = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            this.world = new LoopManiaWorld(width, height, orderedPath, GameMode.NORMAL);
            
            Character character = new Character(new PathPosition(0, orderedPath));

            Sword sword = new Sword(character.x(), character.y());
            Armour armour = new Armour(character.x(), character.y());
            OneRing oneRing = new OneRing(character.x(), character.y());

            character.addItemToInventory(sword);
            character.addItemToInventory(armour);
            character.equipItem(sword);
            character.equipItem(armour);
            character.addItemToInventory(oneRing);

            this.world.setCharacter(character);

            DoggieEnemy doggie = new DoggieEnemy(new PathPosition(0, orderedPath));

            this.world.addEnemy(doggie);

            Card towerCard = world.loadCard(new TowerBuilding());

            world.convertCardToBuildingByCoordinates(towerCard.getX(), towerCard.getY(), 1, 1);
            
            Battle battle = new Battle();
            battle.setCharacter(this.world.getCharacter());
            battle.setBattleEnemies(this.world.getEnemies());
            battle.setBuildings(this.world.getBuildings());

            defeatedEnemies = battle.runBattleLoop();

            assertTrue(defeatedEnemies.contains(doggie));
        }
    }
}
