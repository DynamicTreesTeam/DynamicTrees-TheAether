package com.dtteam.dttwilightforest.growthlogic;

import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.systems.growthlogic.GrowthLogicKit;
import com.dtteam.dttwilightforest.DynamicTreesTheTwilightForest;

public class DTTFGrowthLogicKits {

    public static final GrowthLogicKit CANOPY = new CanopyLogic(DynamicTreesTheTwilightForest.location("canopy"));
    public static final GrowthLogicKit MINING = new MiningTreeLogic(DynamicTreesTheTwilightForest.location("mining"));

    public static void register(final Registry<GrowthLogicKit> registry) {
        registry.registerAll(CANOPY, MINING);
    }

}
