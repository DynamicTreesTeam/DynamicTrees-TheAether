package maxhyper.dtaether.blocks;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.loot.AetherLoot;
import com.aetherteam.aether.loot.AetherLootContexts;
import com.dtteam.dynamictrees.block.branch.ThickBranchBlock;
import com.dtteam.dynamictrees.loot.DTLootParameterSets;
import com.dtteam.dynamictrees.loot.function.MultiplyCount;
import com.dtteam.dynamictrees.loot.function.MultiplyByLogsCount;
import com.dtteam.dynamictrees.loot.function.MultiplyBySticksCount;
import com.dtteam.dynamictrees.systems.nodemapper.NetVolumeNode;
import com.dtteam.dynamictrees.utility.ItemUtils;
import maxhyper.dtaether.trees.ImbuedLogFamily;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class ImbuedBranchBlock extends ThickBranchBlock {

    public ImbuedBranchBlock(ResourceLocation name, Properties properties) {
        super(name, properties);
    }

    @Override
    public void stripBranch(BlockState state, Level level, BlockPos pos, Player player, ItemStack heldItem) {
        int radius = this.getRadius(state);
        this.damageAxe(player, heldItem, radius / 2, new NetVolumeNode.Volume(radius * radius * 64 / 2), false);
        if (heldItem.is(AetherTags.Items.GOLDEN_AMBER_HARVESTERS) && level.getServer() != null && level instanceof ServerLevel serverLevel) {
            LootParams.Builder lootParamsBuilder = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.TOOL, heldItem)
                    .withParameter(LootContextParams.ORIGIN, new Vec3(pos.getX(), pos.getY(), pos.getZ()));

            LootParams lootParams = lootParamsBuilder.create(AetherLootContexts.STRIPPING);

            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(
                    ResourceKey.create(Registries.LOOT_TABLE, ((ImbuedLogFamily) getFamily()).getStripLootLocation())
            );
            List<ItemStack> list = lootTable.getRandomItems(lootParams);

            for (ItemStack itemStack : list) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
        }
        this.stripBranch(state, level, pos, radius);
    }
    public Optional<Block> getPrimitiveImbuedLog() {
        if (getFamily() instanceof ImbuedLogFamily imbuedLogFamily)
            return imbuedLogFamily.getPrimitiveImbuedLog();
        return super.getPrimitiveLog();
    }
    public Optional<Item> getImbuedDropItem() {
        if (getFamily() instanceof ImbuedLogFamily imbuedLogFamily)
            return imbuedLogFamily.getImbuedDropItem();
        return Optional.empty();
    }
    @Override
    public LootTable.Builder createBranchDrops(HolderLookup.Provider registries) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);
        LootItemCondition.Builder hasSilkTouch = MatchTool.toolMatches(
                ItemPredicate.Builder.item().withSubPredicate(
                        ItemSubPredicates.ENCHANTMENTS,
                        ItemEnchantmentsPredicate.enchantments(List.of(
                                new EnchantmentPredicate(enchantments.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))
                        ))
                )
        );
        return LootTable.lootTable()
                //Golden oak log when silktouch is used
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(this.getPrimitiveImbuedLog().get())
                                .apply(MultiplyByLogsCount.multiplyByLogsCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(hasSilkTouch)
                        ))
                //regular skyroot log when silktouch is not used
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(this.getPrimitiveLog().get())
                                .apply(MultiplyByLogsCount.multiplyByLogsCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(hasSilkTouch.invert())
                        ))
                //also include amber when silktouch is not used.
                // This has an extra condition for the tool to be an amber harvester,
                // and a fortune bonus
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(this.getImbuedDropItem().get())
                                .apply(ApplyBonusCount.addOreBonusCount(ItemUtils.getEnchantment(Enchantments.FORTUNE, registries)))
                                .apply(MultiplyByLogsCount.multiplyByLogsCount())
                                .apply(() -> new MultiplyCount(List.of(), 1.5F))//With large amounts of 'random between 1 and 2', it ends up being the same as multiplying by 1.5
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(AetherTags.Items.GOLDEN_AMBER_HARVESTERS)))
                                .when(hasSilkTouch.invert())
                        ))
                //sticks
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(getFamily().getStick(1).getItem())
                                .apply(MultiplyBySticksCount.multiplyBySticksCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                        )
                )
                .setParamSet(DTLootParameterSets.BRANCHES);

    }
}
