package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.block.rooty.SoilHelper;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.FullGenerationContext;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ChangeGrassGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");
    public static final ConfigurationProperty<Integer> MAX_DEPTH = ConfigurationProperty.integer("max_depth");
    public static final ConfigurationProperty<String> REPLACEABLE_SOILS = ConfigurationProperty.string("replaceable_soils");

    public ChangeGrassGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(BLOCK, MAX_DEPTH, REPLACEABLE_SOILS);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BLOCK, Blocks.AIR)
                .with(MAX_DEPTH, 6)
                .with(REPLACEABLE_SOILS, SoilHelper.DIRT_LIKE);
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        if (configuration.get(BLOCK) == Blocks.AIR) return false;
        return SoilHelper.isSoilRegistered(configuration.get(BLOCK));
    }

    @Override
    protected boolean generate(GenFeatureConfiguration configuration, FullGenerationContext context) {

        int rad = context.radius();
        int startH = context.species().getLowestBranchHeight() + 3;
        int layers = startH + configuration.get(MAX_DEPTH);
        BlockPos.MutableBlockPos placePos = context.pos().above(startH).mutable();
        LevelAccessor level = context.level();
        BlockState placeState = configuration.get(BLOCK).defaultBlockState();
        String soilTag = configuration.get(REPLACEABLE_SOILS);

        for (int i=0; i<layers; i++){
            this.placeCircle(level, placePos.west().north(), placeState, soilTag);
            this.placeCircle(level, placePos.east(2).north(), placeState, soilTag);
            this.placeCircle(level, placePos.west().south(2), placeState, soilTag);
            this.placeCircle(level, placePos.east(2).south(2), placeState, soilTag);

            for(int j = 0; j < 5; ++j) {
                int k = context.random().nextInt(48);
                int l = k % rad;
                int i1 = k / rad;
                if (l == 0 || l == 7 || i1 == 0 || i1 == 7) {
                    this.placeCircle(level, placePos.offset(-3 + l, 0, -3 + i1), placeState, soilTag);
                }
            }
            placePos.move(0,-1,0);
        }
        return false;
    }

    private void placeCircle(LevelAccessor level, BlockPos pos, BlockState placeState, String soilTag) {
        for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
                if (Math.abs(i) != 2 || Math.abs(j) != 2) {
                    this.placeBlockAt(level, pos.offset(i, 0, j), placeState, soilTag);
                }
            }
        }

    }

    private void placeBlockAt(LevelAccessor level, BlockPos pos, BlockState placeState, String soilTag) {
        for(int i = 2; i >= -3; --i) {
            BlockPos blockpos = pos.above(i);
            if (this.isAcceptableSoil(level, blockpos, soilTag) && !level.getBlockState(blockpos.above()).isCollisionShapeFullBlock(level, blockpos.above())) {
                level.setBlock(blockpos, placeState, 2);
                break;
            }

            if (!level.isEmptyBlock(blockpos) && i < 0) {
                break;
            }
        }

    }

    public boolean isAcceptableSoil(LevelSimulatedReader level, BlockPos pos, String soilTag) {
        return level.isStateAtPosition(pos, (s)->SoilHelper.isSoilAcceptable(s, SoilHelper.getSoilFlags(soilTag)));
    }

}
