package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import maxhyper.dtaether.compat.CompatHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;

public class LeafPileGenFeature extends PetalsGenFeature {

    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");
    public static final ConfigurationProperty<Integer> MAX_DEPTH = ConfigurationProperty.integer("max_depth");
    public static final ConfigurationProperty<Integer> RADIUS = ConfigurationProperty.integer("radius");
    public static final ConfigurationProperty<Integer> MAX_LAYERS = ConfigurationProperty.integer("max_leaf_layers");

    public LeafPileGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(BLOCK, MAX_DEPTH, RADIUS, PLACE_CHANCE, MAX_COUNT, MAX_LAYERS);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BLOCK, Blocks.AIR)
                .with(MAX_DEPTH, 15)
                .with(RADIUS, 1)
                .with(PLACE_CHANCE, 0.02f)
                .with(MAX_COUNT, 10)
                .with(MAX_LAYERS, 4);
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        if (configuration.get(BLOCK) == Blocks.AIR) return false;
        BlockState state = configuration.get(BLOCK).defaultBlockState();
        return state.hasProperty(layersProperty());
    }

    private IntegerProperty layersProperty (){
        return CompatHandler.blockStateProperties.getLeafPileLayersProperty();
    }

    protected boolean canBePlacedOnBlock (LevelAccessor level, BlockPos pos, Block block){
        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.is(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON)) {
            return false;
        } else if (blockstate.is(BlockTags.SNOW_LAYER_CAN_SURVIVE_ON)) {
            return true;
        } else {
            return Block.isFaceFull(blockstate.getCollisionShape(level, pos), Direction.UP) || blockstate.is(block) && blockstate.getValue(layersProperty()) == 16;
        }
    }

    protected BlockState getPetalsForPlacement (GenFeatureConfiguration configuration, LevelAccessor level, BlockPos pos, BlockState state){
        return state.setValue(layersProperty(), 1+level.getRandom().nextInt(configuration.get(MAX_LAYERS)));
    }

}
