package com.dtteam.dttwilightforest.trees;

import com.dtteam.dynamictrees.api.registry.RegistryHandler;
import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.sapling.DynamicSaplingBlock;
import com.dtteam.dynamictrees.item.Seed;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import com.dtteam.dynamictreesplus.tree.HugeMushroomSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;

import javax.annotation.Nonnull;

public class MushgloomSpecies extends HugeMushroomSpecies {

    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultMushroomType(MushgloomSpecies::new);

    public MushgloomSpecies(ResourceLocation resourceLocation, Family family, CapProperties leavesProperties) {
        super(resourceLocation, family, leavesProperties);
    }

    @Override
    public Species generateSeed() {
        return !this.shouldGenerateSeed() || this.seed != null ? this :
                this.setSeed(RegistryHandler.addItem(getSeedName(), () -> new Seed(this){
                    @Override
                    public boolean doPlanting(@Nonnull Level level,@Nonnull BlockPos pos, @Nullable Player planter,@Nonnull ItemStack seedStack) {
                        if (super.doPlanting(level, pos, planter, seedStack)){
                            if (level.getBlockState(pos.below()).is(TFBlocks.UBEROUS_SOIL.get())){
                                level.setBlock(pos.below(), Blocks.MYCELIUM.defaultBlockState(), 3);
                                getSpecies().transitionToTree(level, pos);
                                level.levelEvent(2005, pos, 0);
                            }
                            return true;
                        }
                        return false;
                    }
                }));
    }

    @Override
    public Species generateSapling() {
        return !this.shouldGenerateSapling() || this.saplingBlock != null ? this :
                this.setSapling(RegistryHandler.addBlock(this.getSaplingRegName(), () -> new DynamicSaplingBlock(this){
                    @Override
                    public VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
                        return Shapes.empty();
                    }

                    @Override
                    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                        return 3;
                    }
                }));
    }
}
