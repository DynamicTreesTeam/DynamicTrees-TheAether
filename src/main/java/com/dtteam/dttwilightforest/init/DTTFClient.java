package com.dtteam.dttwilightforest.init;

import com.dtteam.dttwilightforest.DynamicTreesTheTwilightForest;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = DynamicTreesTheTwilightForest.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class DTTFClient {

    public static void setup() {
        registerRenderLayers();
    }

    private static void registerRenderLayers() {
        Block mangroveRoots = BuiltInRegistries.BLOCK.get(DynamicTreesTheTwilightForest.location("twilight_mangrove_roots"));
        ItemBlockRenderTypes.setRenderLayer(mangroveRoots, RenderType.cutoutMipped());
//        ItemBlockRenderTypes.setRenderLayer(DTTFRegistries.UNDERGROUND_ROOTS.get(), RenderType.cutoutMipped());
    }

    @SubscribeEvent
    private static void registerColorHandlers(RegisterColorHandlersEvent.Block event) {
        Block mangroveRoots = BuiltInRegistries.BLOCK.get(DynamicTreesTheTwilightForest.location("twilight_mangrove_roots"));

        event.register((state, level, pos, tintIndex) -> {
                    if (tintIndex != 0) return 0xFFFFFF;
                    return level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.get(0.5D, 1.0D);
                },
                mangroveRoots);

    }


}
