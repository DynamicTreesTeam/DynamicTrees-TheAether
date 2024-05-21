package maxhyper.dtaether.blocks;

import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.loot.AetherLoot;
import com.aetherteam.aether.loot.AetherLootContexts;
import com.ferreusveritas.dynamictrees.block.branch.ThickBranchBlock;
import com.ferreusveritas.dynamictrees.loot.DTLootParameterSets;
import com.ferreusveritas.dynamictrees.loot.function.MultiplyCount;
import com.ferreusveritas.dynamictrees.loot.function.MultiplyLogsCount;
import com.ferreusveritas.dynamictrees.loot.function.MultiplySticksCount;
import com.ferreusveritas.dynamictrees.systems.nodemapper.NetVolumeNode;
import maxhyper.dtaether.trees.ImbuedLogFamily;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

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
        if (heldItem.is(AetherTags.Items.GOLDEN_AMBER_HARVESTERS) && level.getServer() != null && level instanceof ServerLevel serverLevel){
            LootContext.Builder lootContext = (new LootContext.Builder(serverLevel)).withParameter(LootContextParams.TOOL, heldItem);
            LootTable lootTable = level.getServer().getLootTables().get(((ImbuedLogFamily)getFamily()).getStripLootLocation());
            List<ItemStack> list = lootTable.getRandomItems(lootContext.create(AetherLootContexts.STRIPPING));

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
    public LootTable.Builder createBranchDrops() {
        return LootTable.lootTable()
                //Golden oak log when silktouch is used
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(this.getPrimitiveImbuedLog().get())
                                .apply(MultiplyLogsCount.multiplyLogsCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.ANY))))
                        ))
                //regular skyroot log when silktouch is not used
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(this.getPrimitiveLog().get())
                                .apply(MultiplyLogsCount.multiplyLogsCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(InvertedLootItemCondition.invert(
                                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.ANY)))
                                ))
                        ))
                //also include amber when silktouch is not used.
                // This has an extra condition for the tool to be an amber harvester,
                // and a fortune bonus
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(this.getImbuedDropItem().get())
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                .apply(MultiplyLogsCount.multiplyLogsCount())
                                .apply(() -> new MultiplyCount(new LootItemCondition[0], 1.5F))//With large amounts of 'random between 1 and 2', it ends up being the same as multiplying by 1.5
                                .apply(ApplyExplosionDecay.explosionDecay())
                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(AetherTags.Items.GOLDEN_AMBER_HARVESTERS)))
                                .when(InvertedLootItemCondition.invert(
                                        MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.ANY)))
                                ))
                        ))
                //sticks
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(getFamily().getStick(1).getItem())
                                .apply(MultiplySticksCount.multiplySticksCount())
                                .apply(ApplyExplosionDecay.explosionDecay())
                        )
                )
                .setParamSet(DTLootParameterSets.BRANCHES);

    }
}
