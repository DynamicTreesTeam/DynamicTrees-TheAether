package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import maxhyper.dtaether.compat.CompatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraftforge.common.IPlantable;

import java.util.*;

public class PetalsGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");
    public static final ConfigurationProperty<Integer> MAX_DEPTH = ConfigurationProperty.integer("max_depth");
    public static final ConfigurationProperty<Integer> RADIUS = ConfigurationProperty.integer("radius");

    public PetalsGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(BLOCK, MAX_DEPTH, RADIUS, PLACE_CHANCE, MAX_COUNT);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BLOCK, Blocks.AIR)
                .with(MAX_DEPTH, 15)
                .with(RADIUS, 3)
                .with(PLACE_CHANCE, 0.01f)
                .with(MAX_COUNT, 20);
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        if (configuration.get(BLOCK) == Blocks.AIR) return false;
        if (CompatHandler.blockStateProperties == null) return false;
        BlockState state = configuration.get(BLOCK).defaultBlockState();
        return CompatHandler.blockStateProperties.hasPrismaticness(state) && state.hasProperty(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final LevelAccessor level = context.level();


        int count = level.getRandom().nextInt(configuration.get(MAX_COUNT));

        rainDownPetalsFromEndpoints(configuration, level, context.endPoints(), count);

        return super.postGenerate(configuration, context);
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        //Only spread petals when fertility is not 0, to prevent lag.
        if (!context.natural() || context.fertility() == 0) return false;
        final LevelAccessor level = context.level();
        if (level.getRandom().nextFloat() >= configuration.get(PLACE_CHANCE)) return false;

        final BlockPos rootPos = context.pos();
        final FindEndsNode endFinder = new FindEndsNode();
        TreeHelper.startAnalysisFromRoot(level, rootPos, new MapSignal(endFinder));
        final List<BlockPos> endPoints = endFinder.getEnds();

        rainDownPetalsFromEndpoints(configuration, level, endPoints, 1);

        return super.postGrow(configuration, context);
    }

    protected void rainDownPetalsFromEndpoints(GenFeatureConfiguration configuration, LevelAccessor level, List<BlockPos> endPoints, int placeCount){
        if (endPoints.size() == 0) return;
        int[] place = new int[endPoints.size()];
        for (int i=0;i<placeCount;i++){
            place[level.getRandom().nextInt(endPoints.size())]++;
        }
        int rad = configuration.get(RADIUS);
        for (int i=0; i<endPoints.size(); i++){
            if (place[i] == 0) continue;
            BlockPos end = endPoints.get(i);
            for (BlockPos pos : BlockPos.randomBetweenClosed(level.getRandom(), place[i], end.getX() - rad, end.getY(), end.getZ() - rad, end.getX() + rad, end.getY(), end.getZ() + rad)){
                tryPlacePetal(configuration, level, pos);
            }
        }
    }

    protected boolean tryPlacePetal(GenFeatureConfiguration configuration, LevelAccessor level, BlockPos pos){
        for (int i=1; i<configuration.get(MAX_DEPTH); i++){
            BlockPos testPos = pos.below(i);
            BlockState testState = level.getBlockState(testPos);
            //Ignore leaves and branches
            if (TreeHelper.isTreePart(testState)) continue;
            Block petalsBlock = configuration.get(BLOCK);
            //If we found a solid block or petals before finding a valid state, we can't place petals here.
            if (testState.isCollisionShapeFullBlock(level, testPos) || testState.is(petalsBlock)) break;

            if (level.isEmptyBlock(testPos) && canBePlacedOnBlock(level, testPos.below(), petalsBlock)){
                level.setBlock(testPos, getPetalsForPlacement(configuration, level, testPos, petalsBlock.defaultBlockState()), 3);
                return true;
            }
        }
        return false;
    }

    protected boolean canBePlacedOnBlock (LevelAccessor level, BlockPos pos, Block block){
        return block instanceof IPlantable pPetalsBlock && level.getBlockState(pos).canSustainPlant(level, pos, Direction.UP, pPetalsBlock);
    }

    protected BlockState getPetalsForPlacement (GenFeatureConfiguration configuration, LevelAccessor level, BlockPos pos, BlockState state){
        RandomSource rand = new XoroshiroRandomSource(Mth.getSeed(pos));
        Direction d = Direction.Plane.HORIZONTAL.getRandomDirection(rand);
        BlockState b = this.selectRandomFlowers(pos, level, state);
        return b.setValue(BlockStateProperties.HORIZONTAL_FACING, d);
    }

    private BlockState selectRandomFlowers(BlockPos pos, LevelAccessor level, BlockState state) {
        for (int i = 1; i<=4; i++){
            if (i == 1 || level.getRandom().nextFloat()<0.37){ //0.37 is the magic number to get ~even odds in total
                RandomSource rand = new XoroshiroRandomSource(Mth.getSeed(pos) + i);
                state = CompatHandler.blockStateProperties.setPrismaticness(state, i, rand.nextInt(7));
            }
        }
        return state;
    }

}
