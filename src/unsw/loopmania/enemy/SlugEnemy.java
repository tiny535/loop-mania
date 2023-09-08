package unsw.loopmania.enemy;

import unsw.loopmania.map.PathPosition;

public class SlugEnemy extends BasicEnemy{

    private static final String IMAGE_SOURCE = "slug";

    private static final int BATTLE_RADIUS = 2;
    private static final int SUPPORT_RADIUS = 2;

    private static final int STARTING_HP = 10;
    private static final int STARTING_DMG = 5;

    public SlugEnemy(PathPosition pathPosition){
        super(pathPosition, BATTLE_RADIUS, SUPPORT_RADIUS, STARTING_HP, STARTING_DMG);
    }

    @Override
    public String getImageSrc() {
        return SlugEnemy.IMAGE_SOURCE;
    }
}
