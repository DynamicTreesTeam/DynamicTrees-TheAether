package maxhyper.dtaether.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.data.provider.DTLootTableProvider;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class DropBlocksCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(DropBlocksCapProperties::new);

    public DropBlocksCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public LootTable.Builder createBlockDrops() {
        return DTLootTableProvider.BlockLoot.createWartBlockDrops(primitiveCap.getBlock());
    }

    public LootTable.Builder createDrops() {
        return DTLootTableProvider.BlockLoot.createWartDrops(primitiveCap.getBlock());
    }

}
