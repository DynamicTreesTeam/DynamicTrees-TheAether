package maxhyper.dtaether.mushroomlogic;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictreesplus.DynamicTreesPlus;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import maxhyper.dtaether.DynamicTreesAether;

public class DTAetherMushroomShapeKits {
    public static final MushroomShapeKit BALL_MUSHROOM_SHAPE = new BallShape(DynamicTreesAether.location("ball"));

    public DTAetherMushroomShapeKits() {
    }

    public static void register(Registry<MushroomShapeKit> registry) {
        registry.register(BALL_MUSHROOM_SHAPE);
    }
}
