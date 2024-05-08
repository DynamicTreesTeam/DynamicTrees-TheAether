package maxhyper.dtaether.trees;

import com.aetherteam.aether.AetherTags;
import com.ferreusveritas.dynamictrees.api.data.BranchStateGenerator;
import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.ThickBranchBlock;
import com.ferreusveritas.dynamictrees.data.provider.BranchLoaderBuilder;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.loot.DTLootParameterSets;
import com.ferreusveritas.dynamictrees.loot.function.MultiplyCount;
import com.ferreusveritas.dynamictrees.loot.function.MultiplyLogsCount;
import com.ferreusveritas.dynamictrees.loot.function.MultiplySticksCount;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.util.MutableLazyValue;
import com.ferreusveritas.dynamictrees.util.Optionals;
import com.ferreusveritas.dynamictrees.util.ResourceLocationUtils;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ImbuedLogFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(ImbuedLogFamily::new);

    protected Supplier<BranchBlock> imbuedBranch;
    protected Block primitiveImbuedLog;
    protected Item imbuedDropItem;
    protected ResourceLocation imbuedBranchName;
    protected final MutableLazyValue<Generator<DTBlockStateProvider, Family>> imbuedBranchStateGenerator;

    public ImbuedLogFamily(ResourceLocation name) {
        super(name);
        imbuedBranchStateGenerator = MutableLazyValue.supplied(GoldenBranchStateGenerator::new);
    }

    @Override
    public void setupBlocks() {
        super.setupBlocks();
        this.imbuedBranch = setupBranch(createImbuedBranch(Optional.ofNullable(imbuedBranchName).orElse(getBranchName("imbued_"))), true);
    }

    protected BranchBlock createImbuedBranchBlock(ResourceLocation name) {
        BasicBranchBlock branch = new ThickBranchBlock(name, this.getProperties()){
            public void stripBranch(BlockState state, LevelAccessor level, BlockPos pos, int radius) {
                this.getFamily().getStrippedBranch().ifPresent((strippedBranch) -> {
                    strippedBranch.setRadius(level, pos, Math.max(1, radius - ((Boolean) DTConfigs.ENABLE_STRIP_RADIUS_REDUCTION.get() ? 1 : 0)), (Direction)null, 3);
                });
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
                                        .apply(() -> new MultiplyCount(new LootItemCondition[0], 1.5F))//With large amounts of 'random between 1 and 2', it ends up being the same as multiplying by 1.5
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                        .apply(MultiplyLogsCount.multiplyLogsCount())
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
        };
        if (this.isFireProof()) {
            branch.setFireSpreadSpeed(0).setFlammability(0);
        }

        return branch;
    }

    protected Supplier<BranchBlock> createImbuedBranch(ResourceLocation name) {
        return RegistryHandler.addBlock(ResourceLocationUtils.suffix(name, this.getBranchNameSuffix()), () -> this.createImbuedBranchBlock(name));
    }

    public void setImbuedBranchName(ResourceLocation imbuedBranchName) {
        this.imbuedBranchName = imbuedBranchName;
    }

    public Family setPrimitiveImbuedLog(Block primitiveLog) {
        this.primitiveImbuedLog = primitiveLog;
        return this;
    }
    public Family setImbuedDropItem(Item imbuedDropItem) {
        this.imbuedDropItem = imbuedDropItem;
        imbuedBranch.get().setPrimitiveLogDrops(new ItemStack(imbuedDropItem), new ItemStack(getPrimitiveLog().orElse(Blocks.AIR)));
        return this;
    }

    public Optional<BranchBlock> getImbuedBranch() {
        return Optionals.ofBlock(imbuedBranch.get());
    }

    public Optional<Block> getPrimitiveImbuedLog() {
        return Optionals.ofBlock(primitiveImbuedLog);
    }

    public Optional<Item> getImbuedDropItem() {
        return Optionals.ofItem(imbuedDropItem);
    }

    public void generateStateData(DTBlockStateProvider provider) {
        super.generateStateData(provider);
        (this.imbuedBranchStateGenerator.get()).generate(provider, this);
    }

    public void addGoldenBranchTextures(BiConsumer<String, ResourceLocation> textureConsumer, ResourceLocation primitiveLogLocation, Block sourceBlock) {
        ResourceLocation bark = primitiveLogLocation;
        ResourceLocation rings = ResourceLocationUtils.suffix(primitiveLogLocation, "_top");

        if (this.textureOverrides.containsKey("imbued_branch")) {
            bark = this.textureOverrides.get("imbued_branch");
        }
        if (this.textureOverrides.containsKey("branch_top")) {
            rings = this.textureOverrides.get("branch_top");
        }
        textureConsumer.accept("bark", bark);
        textureConsumer.accept("rings", rings);
    }

    public static class GoldenBranchStateGenerator extends BranchStateGenerator{
        @Override
        public @NotNull Dependencies gatherDependencies(@NotNull Family input) {
            if (input instanceof ImbuedLogFamily castedInput)
                return (new Dependencies()).append(BRANCH, castedInput.getImbuedBranch()).append(PRIMITIVE_LOG, castedInput.getPrimitiveImbuedLog());
            return super.gatherDependencies(input);
        }
        @Override
        public void generate(DTBlockStateProvider provider, @NotNull Family input, Generator.Dependencies dependencies) {
            BranchBlock branch = dependencies.get(BRANCH);
            BranchLoaderBuilder builder = (provider.models().getBuilder((Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(branch))).getPath())).customLoader(branch.getFamily().getBranchLoaderConstructor());
            Block block = dependencies.get(PRIMITIVE_LOG);
            Objects.requireNonNull(builder);
            if (input instanceof ImbuedLogFamily goldenLogFamily){
                goldenLogFamily.addGoldenBranchTextures(builder::texture, provider.block(ForgeRegistries.BLOCKS.getKey(block)), block);
                provider.simpleBlock(branch, builder.end());
            }

        }
    }

}