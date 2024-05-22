package maxhyper.dtaether.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import maxhyper.dtaether.DynamicTreesAether;

public class DTAetherGrowthLogicKits {

    public static final GrowthLogicKit CONBERRY = new ConberryLogic(DynamicTreesAether.location("conberry"));
    public static final GrowthLogicKit YAGROOT_ROOTS = new YagrootRootsLogic(DynamicTreesAether.location("yagroot_roots"));
    public static final GrowthLogicKit BLIGHTWILLOW = new BlightwillowLogic(DynamicTreesAether.location("blightwillow"));
    public static final GrowthLogicKit FIELDSPROOT = new FieldsprootLogic(DynamicTreesAether.location("fieldsproot"));

    public static void register(final Registry<GrowthLogicKit> registry) {
        registry.registerAll(CONBERRY, YAGROOT_ROOTS, BLIGHTWILLOW, FIELDSPROOT);
    }

}
