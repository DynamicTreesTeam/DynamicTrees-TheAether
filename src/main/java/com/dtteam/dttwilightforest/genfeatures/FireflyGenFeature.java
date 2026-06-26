package com.dtteam.dttwilightforest.genfeatures;

import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.api.configuration.ConfigurationProperty;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.dtteam.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.dtteam.dynamictrees.systems.genfeature.context.PostGrowContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import oshi.util.tuples.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FireflyGenFeature extends GenFeature {

    public static final ConfigurationProperty<Integer> MIN_RADIUS = ConfigurationProperty.integer("min_radius");
    public static final ConfigurationProperty<Integer> MAX_HEIGHT = ConfigurationProperty.integer("max_height");
    public static final ConfigurationProperty<Integer> WORLDGEN_MAX_COUNT = ConfigurationProperty.integer("worldgen_max_count");
    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");

    public FireflyGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(MAX_HEIGHT, WORLDGEN_MAX_COUNT, BLOCK, PLACE_CHANCE, MIN_RADIUS);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(MAX_HEIGHT, 20)
                .with(WORLDGEN_MAX_COUNT, 2)
                .with(BLOCK, Blocks.TORCH)
                .with(PLACE_CHANCE, 0.06f)
                .with(MIN_RADIUS, 7);
    }


    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (context.fertility() > 0 && context.random().nextFloat() < configuration.get(PLACE_CHANCE)) {
            placeFirefly(configuration, context.level(), context.pos(), 1, context.random());
        }
        return false;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        int count = context.random().nextInt(configuration.get(WORLDGEN_MAX_COUNT)+1);
        if (count > 0)
            return placeFirefly(configuration, context.level(), context.pos(), count, context.random());
        else return false;
    }

    public boolean placeFirefly (GenFeatureConfiguration configuration, LevelAccessor level, BlockPos rootPos, int count, RandomSource rand){
        List<Pair<BlockPos, Direction>> foundValues = findValidPositions(configuration, level, rootPos);
        boolean placed = false;
        if (foundValues.isEmpty()) {
            return false;
        }
        for (int i=0; i<count; i++){
            int num = rand.nextInt(foundValues.size());
            Pair<BlockPos, Direction> pair = foundValues.get(num);
            BlockState placeState = configuration.get(BLOCK).defaultBlockState();
            if (placeState.hasProperty(DirectionalBlock.FACING)){
                placeState = placeState.setValue(DirectionalBlock.FACING, pair.getB());
            }
            BlockPos placePos = pair.getA();
            if (placeState.canSurvive(level, placePos)){
                level.setBlock(placePos, placeState, 3);
                placed = true;
            }
        }
        return placed;
    }

    public List<Pair<BlockPos, Direction>> findValidPositions (GenFeatureConfiguration configuration, LevelAccessor level, BlockPos rootPos){
        List<Pair<BlockPos, Direction>> found = new LinkedList<>();
        boolean branchFound = false;
        for (int i=1; i< configuration.get(MAX_HEIGHT); i++){
            BlockPos testPos = rootPos.above(i);
            if (TreeHelper.isBranch(level.getBlockState(testPos))
                    //Critters can only be placed on radius 8 branches
                    && TreeHelper.getRadius(level, testPos) >= configuration.get(MIN_RADIUS)) {
                branchFound = true;
            } else {
                //We had a branch but now we no longer do, which means we got though the whole trunk
                if (branchFound) break;
            }
            for (Direction dir : Direction.Plane.HORIZONTAL){
                BlockPos offsetPos = testPos.offset(dir.getNormal());
                BlockState state = level.getBlockState(offsetPos);
                if (state.canBeReplaced()){
                    found.add(new Pair<>(offsetPos, dir));
                }
            }
        }
        return found;
    }

}
