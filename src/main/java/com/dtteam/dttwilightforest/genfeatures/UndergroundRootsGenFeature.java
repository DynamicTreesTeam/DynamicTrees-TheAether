package com.dtteam.dttwilightforest.genfeatures;

import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.dtteam.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.dtteam.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFConfiguredFeatures;
import twilightforest.init.TFFeatures;
import twilightforest.util.features.FeatureLogic;
import twilightforest.util.features.FeaturePlacers;
import twilightforest.util.RootPlacer;
import twilightforest.util.iterators.VoxelBresenhamIterator;
import twilightforest.world.components.feature.config.RootConfig;

import javax.annotation.Nonnull;

public class UndergroundRootsGenFeature extends GenFeature {

    public UndergroundRootsGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(FRUITING_RADIUS, 6);
    }
    @Override
    protected void registerProperties() {
        this.register(FRUITING_RADIUS);
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        return super.shouldApply(species, configuration);
    }

    @Override
    protected boolean postGenerate(@Nonnull GenFeatureConfiguration configuration, PostGenerationContext context) {
        placeRootsFeature(context.level(), context.pos().below());
        return true;
    }

    @Override
    protected boolean postGrow(@Nonnull GenFeatureConfiguration configuration, PostGrowContext context) {
        if (context.fertility() == 0 || TreeHelper.getRadius(context.level(), context.treePos()) < configuration.get(FRUITING_RADIUS)) return false;

        placeRootsFeature(context.level(), context.pos().below());
        return true;
    }

    //Placing the feature is a temporal solution. Dynamic roots are WIP
    public boolean placeRootsFeature(LevelAccessor world, BlockPos pos) {
        if (world.isClientSide()) return false;
        RandomSource rand = world.getRandom();

        //if (world.getBlockState(pos).getBlock() != Blocks.STONE) {
        //    return false;
        //} else {
            float length = rand.nextFloat() * 6.0F + rand.nextFloat() * 6.0F + 4.0F;
            if (length > (float)pos.getY()) {
                length = (float)pos.getY();
            }

            float tilt = 0.6F + rand.nextFloat() * 0.3F;
            return this.drawRoot(world, rand, pos, pos, length, rand.nextFloat(), tilt, BlockStateProvider.simple(TFBlocks.ROOT_BLOCK.get()), BlockStateProvider.simple(TFBlocks.LIVEROOT_BLOCK.get()));
        //}
    }

    private boolean drawRoot(LevelAccessor world, RandomSource rand, BlockPos oPos, BlockPos pos, float length, float angle, float tilt, BlockStateProvider rootBlock, BlockStateProvider oreBlock) {
        BlockPos dest = FeatureLogic.translate(pos, (double)length, (double)angle, (double)tilt);
        int limit = 6;
        if (oPos.getX() + limit < dest.getX()) {
            dest = new BlockPos(oPos.getX() + limit, dest.getY(), dest.getZ());
        }

        if (oPos.getX() - limit > dest.getX()) {
            dest = new BlockPos(oPos.getX() - limit, dest.getY(), dest.getZ());
        }

        if (oPos.getZ() + limit < dest.getZ()) {
            dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() + limit);
        }

        if (oPos.getZ() - limit > dest.getZ()) {
            dest = new BlockPos(dest.getX(), dest.getY(), oPos.getZ() - limit);
        }

        if (world.getBlockState(dest).getBlock() != Blocks.STONE) {
            return false;
        } else {
            FeaturePlacers.traceRoot(world, new RootPlacer((checkedPos, rootPlacement) -> {
                world.setBlock(checkedPos, rootPlacement, 3);
            }, 0), rand, rootBlock, new VoxelBresenhamIterator(pos, dest));
            BlockPos ballSrc;
            if (length > 8.0F && rand.nextInt(3) > 0) {
                ballSrc = FeatureLogic.translate(pos, (double)(length / 2.0F), (double)angle, (double)tilt);
                float nextAngle = (angle + 0.25F + rand.nextFloat() * 0.5F) % 1.0F;
                float nextTilt = 0.6F + rand.nextFloat() * 0.3F;
                this.drawRoot(world, rand, oPos, ballSrc, length / 2.0F, nextAngle, nextTilt, rootBlock, oreBlock);
            }

            if (length > 6.0F && rand.nextInt(4) == 0) {
                ballSrc = FeatureLogic.translate(pos, (double)(length / 2.0F), (double)angle, (double)tilt);
                BlockPos ballDest = FeatureLogic.translate(ballSrc, 1.5, (double)((angle + 0.5F) % 1.0F), 0.75);
                this.placeRootBlock(world, ballSrc, oreBlock, rand);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock, rand);
                this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballSrc.getY(), ballSrc.getZ()), oreBlock, rand);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballSrc.getY(), ballDest.getZ()), oreBlock, rand);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock, rand);
                this.placeRootBlock(world, new BlockPos(ballSrc.getX(), ballDest.getY(), ballDest.getZ()), oreBlock, rand);
                this.placeRootBlock(world, new BlockPos(ballDest.getX(), ballDest.getY(), ballSrc.getZ()), oreBlock, rand);
                this.placeRootBlock(world, ballDest, oreBlock, rand);
            }

            return true;
        }
    }

    protected boolean placeRootBlock(LevelAccessor world, BlockPos pos, BlockStateProvider state, RandomSource random) {
        return FeatureLogic.canRootGrowIn(world, pos) && world.setBlock(pos, state.getState(random, pos), 3);
    }

}
