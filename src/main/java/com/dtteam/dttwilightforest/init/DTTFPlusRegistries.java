package com.dtteam.dttwilightforest.init;

import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.data.GatherDataHelper;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.block.CommonVoxelShapes;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import com.dtteam.dttwilightforest.DynamicTreesTheTwilightForest;
import com.dtteam.dttwilightforest.trees.GigaSpruceSpecies;
import com.dtteam.dttwilightforest.trees.MushgloomSpecies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class DTTFPlusRegistries {

    public static final VoxelShape SHROOM_AGE0 = Shapes.create(0, 0, 0, 1, 0.75, 1);
    public static final VoxelShape MUSHROOM_CAP_SHORT_ROUND = Block.box(5D, 3D, 5D, 11D, 7D, 11D);
    public static final VoxelShape ROUND_SHORT_MUSHROOM = Shapes.or(CommonVoxelShapes.SAPLING_TRUNK, MUSHROOM_CAP_SHORT_ROUND);

    public static void setup() {
        CommonVoxelShapes.SHAPES.put(DynamicTreesTheTwilightForest.location("mushgloom_age0").toString(), SHROOM_AGE0);
        CommonVoxelShapes.SHAPES.put(DynamicTreesTheTwilightForest.location("round_short_mushroom").toString(), ROUND_SHORT_MUSHROOM);
    }

    @SubscribeEvent
    public static void registerSpeciesTypes(final TypeRegistryEvent<Species> event) {
        if (event.isEntryOfType(Species.class)) {
            event.registerType(DynamicTreesTheTwilightForest.location("mushgloom"), MushgloomSpecies.TYPE);
        }
    }

    public static void gatherData(final GatherDataEvent event) {
        GatherDataHelper.gatherAllData(DynamicTreesTheTwilightForest.MOD_ID, event,
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY,
                CapProperties.REGISTRY);
    }

}
