package com.dtteam.dttwilightforest;

import com.dtteam.dynamictrees.data.GatherDataHelper;
import com.dtteam.dynamictrees.registry.NeoForgeRegistryHandler;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dttwilightforest.init.DTTFClient;
import com.dtteam.dttwilightforest.init.DTTFPlusRegistries;
import com.dtteam.dttwilightforest.init.DTTFRegistries;
import com.dtteam.dttwilightforest.loot.LootModifiers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesTheTwilightForest.MOD_ID)
public class DynamicTreesTheTwilightForest {
    public static final String MOD_ID = "dttwilightforest";

    public DynamicTreesTheTwilightForest(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);

        if (ModList.get().isLoaded("dynamictreesplus")){
            modEventBus.register(DTTFPlusRegistries.class);
        }

        LootModifiers.register(modEventBus);

        NeoForgeRegistryHandler.setup(MOD_ID, modEventBus);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTTFClient.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DTTFRegistries.setup();
        if (ModList.get().isLoaded("dynamictreesplus")){
            DTTFPlusRegistries.setup();
        }
    }

    private void gatherData(final GatherDataEvent event) {
        if (ModList.get().isLoaded("dynamictreesplus")) {
            DTTFPlusRegistries.gatherData(event);
        } else {
            GatherDataHelper.gatherAllData(MOD_ID, event,
                    SoilProperties.REGISTRY,
                    Family.REGISTRY,
                    Species.REGISTRY,
                    LeavesProperties.REGISTRY
            );
        }
    }

    public static ResourceLocation location(final String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
