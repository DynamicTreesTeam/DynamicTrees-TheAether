package com.dtteam.dtaether.blocks;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.data.DTLootTableBuilder;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class DropBlocksCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(DropBlocksCapProperties::new);

    public DropBlocksCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public LootTable.Builder createBlockDrops(HolderLookup.Provider registries) {
        return DTLootTableBuilder.createWartBlockDrops(primitiveCap.getBlock(), registries);
    }

    public LootTable.Builder createDrops(HolderLookup.Provider registries) {
        return DTLootTableBuilder.createWartDrops(primitiveCap.getBlock(), registries);
    }

}
