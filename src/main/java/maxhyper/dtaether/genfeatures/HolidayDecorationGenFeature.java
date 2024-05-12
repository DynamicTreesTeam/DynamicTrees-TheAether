package maxhyper.dtaether.genfeatures;
import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.world.treedecorator.HolidayTreeDecorator;
import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.FullGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.worldgen.GenerationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.Calendar;
import java.util.Random;

/**
 * @author Max Hyper
 */
public class HolidayDecorationGenFeature extends GenFeature {

    public static final ConfigurationProperty<Float> GIFT_CHANCE = ConfigurationProperty.floatProperty("gift_chance");
    public static final ConfigurationProperty<Integer> MAX_DEPTH = ConfigurationProperty.integer("max_depth");
    public static final ConfigurationProperty<Integer> SNOW_RADIUS = ConfigurationProperty.integer("snow_radius");

    public HolidayDecorationGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(GIFT_CHANCE, 0.1f)
                .with(MAX_DEPTH, 4)
                .with(SNOW_RADIUS, 10);
    }

    @Override
    protected void registerProperties() {
        this.register(GIFT_CHANCE, MAX_DEPTH, SNOW_RADIUS);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        int tipHeight = 0;
        BlockPos.MutableBlockPos tipPos = context.pos().above(tipHeight).mutable();
        for (int i=0; i<32; i++){
            tipPos.move(0, 1, 0);
            BlockState testState = context.level().getBlockState(tipPos);
            if (!TreeHelper.isBranch(testState) && !TreeHelper.isLeaves(testState)){
                tipHeight = i;
                break;
            }
        }
        if (tipHeight <= 1) return false; //If its 1 or 0 then something failed
        placeCircle(configuration, context.level(), context.random(), tipPos, tipHeight);
        return true;
    }

    /**
     * Code shamelessly stolen from {@link HolidayTreeDecorator}.
     * Slightly modified.
     */
    private void placeCircle(GenFeatureConfiguration configuration, LevelAccessor level, RandomSource random, BlockPos pos, int tipHeight) {
        this.placeBlockAt(configuration, level, random, pos, 0.0F, tipHeight);
        int radius = configuration.get(SNOW_RADIUS);

        for(int z = 1; z < radius; ++z) {
            for(int x = 0; x < radius; ++x) {
                if (Mth.square(x) + Mth.square(z) <= Mth.square(radius)) {
                    float distance = (float)Math.sqrt((Mth.square(x) + Mth.square(z))) / (float)Mth.square(radius);
                    this.placeBlockAt(configuration, level, random, pos.offset(x, 0, z), distance, tipHeight);
                    this.placeBlockAt(configuration, level, random, pos.offset(-x, 0, -z), distance, tipHeight);
                    this.placeBlockAt(configuration, level, random, pos.offset(-z, 0, x), distance, tipHeight);
                    this.placeBlockAt(configuration, level, random, pos.offset(z, 0, -x), distance, tipHeight);
                }
            }
        }

    }

    private void placeBlockAt(GenFeatureConfiguration configuration, LevelAccessor level, RandomSource random, BlockPos pos, float distance, int tipHeight) {
        for(int i = 0; i >= -(tipHeight + configuration.get(MAX_DEPTH)); --i) {
            BlockPos blockPos = pos.above(i);
            if (level.isEmptyBlock(blockPos.above())
                    && (level.isStateAtPosition(blockPos, HolidayDecorationGenFeature::isAetherGrass) || level.isStateAtPosition(blockPos, HolidayDecorationGenFeature::isLeaves) || Feature.isGrassOrDirt(level, blockPos))
                    && level.isEmptyBlock(blockPos.above(4))
                    && distance <= random.nextFloat() / 2.0F * (1.0F - distance)) {
                if (level.isStateAtPosition(blockPos, HolidayDecorationGenFeature::isLeaves)) {
                    level.setBlock(blockPos.above(), Blocks.SNOW.defaultBlockState(), 3);
                } else {
                    level.setBlock(blockPos.above(), getSnowOrGift(random, configuration.get(GIFT_CHANCE)), 3);
                }
                break;
            }
        }

    }

    private static BlockState getSnowOrGift(RandomSource random, float chance){
        return random.nextFloat() < chance ? AetherBlocks.PRESENT.get().defaultBlockState() : Blocks.SNOW.defaultBlockState();
    }

    private static boolean isAetherGrass(BlockState state) {
        return state.is(com.aetherteam.aether.AetherTags.Blocks.AETHER_DIRT);
    }

    private static boolean isLeaves(BlockState state) {
        return state.is(BlockTags.LEAVES);
    }


}