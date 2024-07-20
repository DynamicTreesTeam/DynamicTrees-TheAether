package maxhyper.dtaether.trees;

import com.ferreusveritas.dynamictrees.api.data.BranchStateGenerator;
import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.branch.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.compat.waila.WailaOther;
import com.ferreusveritas.dynamictrees.data.provider.BranchLoaderBuilder;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import com.ferreusveritas.dynamictrees.data.provider.DTLangProvider;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.util.MutableLazyValue;
import com.ferreusveritas.dynamictrees.util.Optionals;
import com.ferreusveritas.dynamictrees.util.ResourceLocationUtils;
import maxhyper.dtaether.blocks.ImbuedBranchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ImbuedLogFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(ImbuedLogFamily::new);

    protected Supplier<BranchBlock> imbuedBranch;
    protected Block primitiveImbuedLog;
    protected Item imbuedDropItem;
    protected ResourceLocation imbuedBranchName;
    protected ResourceLocation stripLootLocation;
    protected final MutableLazyValue<Generator<DTBlockStateProvider, Family>> imbuedBranchStateGenerator;

    public ImbuedLogFamily(ResourceLocation name) {
        super(name);
        imbuedBranchStateGenerator = MutableLazyValue.supplied(ImbuedBranchStateGenerator::new);
    }

    @Override
    public void setupBlocks() {
        super.setupBlocks();
        this.imbuedBranch = setupBranch(createImbuedBranch(getBranchName("imbued_")), true);
    }

    @Override
    public void generateLangData(DTLangProvider provider) {
        super.generateLangData(provider);
    }

    public void setStripLootLocation(ResourceLocation stripLootLocation) {
        this.stripLootLocation = stripLootLocation;
    }

    public ResourceLocation getStripLootLocation() {
        return stripLootLocation;
    }

    protected Supplier<BranchBlock> createImbuedBranch(ResourceLocation name) {
        return RegistryHandler.addBlock(ResourceLocationUtils.suffix(name, this.getBranchNameSuffix()), () -> this.createImbuedBranchBlock(name));
    }

    public boolean stripBranch(BlockState state, Level level, BlockPos pos, Player player, ItemStack heldItem) {
        if (this.hasStrippedBranch()) {
            BranchBlock branch = state.getBlock() instanceof ImbuedBranchBlock ?
                    getImbuedBranch().orElse(null) :
                    getBranch().orElse(null);
            if (branch != null && !branch.isStrippedBranch()){
                branch.stripBranch(state, level, pos, player, heldItem);
                if (level.isClientSide) {
                    level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
                    WailaOther.invalidateWailaPosition();
                }

            };
            return this.getBranch().isPresent();
        } else {
            return false;
        }
    }

    protected BranchBlock createImbuedBranchBlock(ResourceLocation name) {
        BasicBranchBlock branch = new ImbuedBranchBlock(name, this.getProperties());
        if (this.isFireProof()) {
            branch.setFireSpreadSpeed(0).setFlammability(0);
        }

        return branch;
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

    public static class ImbuedBranchStateGenerator extends BranchStateGenerator{
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

    @Override
    public void addGeneratedBlockTags(Function<TagKey<Block>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> tagAppender) {
        super.addGeneratedBlockTags(tagAppender);
        this.getImbuedBranch().ifPresent((branch) -> {
            this.tierTag(this.getDefaultBranchHarvestTier(), tagAppender).ifPresent((tagBuilder) -> {
                tagBuilder.add(branch);
            });
            this.defaultBranchTags().forEach((tag) -> {
                if (!this.isOnlyIfLoaded()) {
                    tagAppender.apply(tag).add(branch);
                } else {
                    tagAppender.apply(tag).addOptional(BuiltInRegistries.BLOCK.getKey(branch));
                }

            });
        });
    }
}