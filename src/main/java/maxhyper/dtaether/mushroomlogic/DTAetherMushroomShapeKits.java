package maxhyper.dtaether.mushroomlogic;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictreesplus.DynamicTreesPlus;
import com.dtteam.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import maxhyper.dtaether.DynamicTreesAether;

public class DTAetherMushroomShapeKits {
    public static final MushroomShapeKit BALL_MUSHROOM_SHAPE = new BallShape(DynamicTreesAether.location("ball"));

    public DTAetherMushroomShapeKits() {
    }

    public static void register(Registry<MushroomShapeKit> registry) {
        registry.register(BALL_MUSHROOM_SHAPE);
    }
}
