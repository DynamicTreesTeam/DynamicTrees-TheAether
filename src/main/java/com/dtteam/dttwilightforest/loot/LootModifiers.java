package com.dtteam.dttwilightforest.loot;

import com.mojang.serialization.MapCodec;
import com.dtteam.dttwilightforest.DynamicTreesTheTwilightForest;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class LootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DynamicTreesTheTwilightForest.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceItemModifier>> REPLACE_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("replace_item", ReplaceItemModifier.CODEC);

    public static void register(IEventBus bus) {
        LOOT_MODIFIER_SERIALIZERS.register(bus);
    }
}
